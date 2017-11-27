/**
 * 
 */
package org.jocean.wechat.service;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.inject.Inject;

import org.jocean.http.Feature;
import org.jocean.http.rosa.SignalClient;
import org.jocean.idiom.ExceptionUtils;
import org.jocean.idiom.jmx.MBeanRegister;
import org.jocean.idiom.jmx.MBeanRegisterAware;
import org.jocean.wechat.WXTokenSource;
import org.jocean.wechat.spi.FetchAccessTokenRequest;
import org.jocean.wechat.spi.FetchAccessTokenResponse;
import org.jocean.wechat.spi.FetchTicketRequest;
import org.jocean.wechat.spi.FetchTicketResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.google.common.collect.Lists;

import io.netty.handler.ssl.SslContextBuilder;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;

/**
 * @author isdom
 *
 */
public class DefaultWXTokenSource implements WXTokenSource, MBeanRegisterAware {

    private static final String MBEAN_SUFFIX = "info=wechat";

    private static final Logger LOG = 
            LoggerFactory.getLogger(DefaultWXTokenSource.class);
    
    public void start() throws Exception {
        startToUpdateToken();
    }
    
    public void stop() {
        if (this._isActive) {
            this._isActive = false;
            final Subscription timer = this._timer;
            if (null!=timer) {
                timer.unsubscribe();
                this._timer = null;
            }
        }
    }
    
    /* (non-Javadoc)
     * @see org.jocean.wechat.WXTokenSource#getAppid()
     */
    @Override
    public String getAppid() {
        return this._appid;
    }

    /* (non-Javadoc)
     * @see org.jocean.wechat.WXTokenSource#getAccessToken(boolean)
     */
    @Override
    public Observable<String> getAccessToken(boolean forceRefresh) {
        return Observable.unsafeCreate(subscriber -> {
            if (!subscriber.isUnsubscribed()) {
                while (!this._getTokenPolicy.call(subscriber))
                    ;
            }
        });
    }

    private final Func1<Subscriber<? super String>, Boolean> WAIT_FOR_UPDATE = subscriber->addToPendings(subscriber);
    private final Func1<Subscriber<? super String>, Boolean> PUSH_TOKEN_NOW = subscriber->pushCurrentToken(subscriber);

    private void startToUpdateToken() throws Exception {
        this._subscribers4accessToken = Lists.newCopyOnWriteArrayList();
        this._getTokenPolicy = WAIT_FOR_UPDATE;

        fetchAccessTokenFromWXOpenAPI().doOnNext(resp -> onAccessTokenResponse(resp))
            .flatMap(resp -> fetchTicketFromWXOpenAPI()).subscribe(resp -> {
                updateMBean(resp.getTicket());
                scheduleUpdateToken(Math.max(0L, this._accessTokenExpireInMs - System.currentTimeMillis()),
                        TimeUnit.MILLISECONDS);
            }, error -> {
                onAccessTokenError(error);
                // wait 10s, and retry
                scheduleUpdateToken(10, TimeUnit.SECONDS);
            });
    }
    
    private void scheduleUpdateToken(final long delay, final TimeUnit unit) {
        if (this._isActive) {
            this._timer = Observable.timer(delay, unit)
            .subscribe(unused -> {
                    this._timer = null;
                    try {
                        startToUpdateToken();
                    } catch (Exception e) {
                        LOG.warn("exception when start to update, detail: {}",
                            ExceptionUtils.exception2detail(e));
                    }
                });
        } else {
            LOG.warn("wxtokensouce unactived, ignore scheduleUpdateToken.");
        }
    }

    private boolean addToPendings(final Subscriber<? super String> subscriber) {
        this._subscribersLock.readLock().lock();
        try {
            final List<Subscriber<? super String>> subscribers = this._subscribers4accessToken;
            if (null != subscribers) {
                if (!subscriber.isUnsubscribed()) {
                    subscribers.add(subscriber);
                    subscriber.add(Subscriptions.create(() -> subscribers.remove(subscriber)));
                }
                return true;
            } else {
                return false;
            }
        } finally {
            this._subscribersLock.readLock().unlock();
        }
    }

    private void updateMBean(final String ticket) {
        if (null!=this._register) {
            final String expireTimeAsString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(new Date(this._accessTokenExpireInMs));
            
            this._register.unregisterMBean(MBEAN_SUFFIX);
            this._register.registerMBean(MBEAN_SUFFIX, new WechatInfoMXBean() {
                
                @Override
                public String getName() {
                    return _wpa;
                }

                @Override
                public String getAccessToken() {
                    return _accessToken;
                }

                @Override
                public String getAppid() {
                    return _appid;
                }

                @Override
                public String getSecret() {
                    return _secret;
                }
                
                @Override
                public String getExpireTime() {
                    return expireTimeAsString;
                }

                @Override
                public String getTicket() {
                    return ticket;
                }});
        }
    }

    private Observable<FetchAccessTokenResponse> fetchAccessTokenFromWXOpenAPI() {
        final FetchAccessTokenRequest fetchAccessTokenReq = new FetchAccessTokenRequest();
        
        fetchAccessTokenReq.setAppid(this._appid);
        fetchAccessTokenReq.setSecret(this._secret);
        try {
            return this._signalClient.interaction().request(fetchAccessTokenReq)
                .feature(Feature.ENABLE_LOGGING_OVER_SSL)
                .feature(new Feature.ENABLE_SSL(SslContextBuilder.forClient().build()))
                .feature(new SignalClient.UsingUri(new URI("https://api.weixin.qq.com/cgi-bin")))
                .feature(new SignalClient.UsingPath("/token"))
                .feature(new SignalClient.DecodeResponseBodyAs(FetchAccessTokenResponse.class))
                .<FetchAccessTokenResponse>build()
                .timeout(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            return Observable.error(e);
        }
    }

    private Observable<FetchTicketResponse> fetchTicketFromWXOpenAPI() {
        final FetchTicketRequest fetchTicketRequest = new FetchTicketRequest();
        
        fetchTicketRequest.setAccessToken(this._accessToken);
        fetchTicketRequest.setType("jsapi");
        try {
            return this._signalClient.interaction().request(fetchTicketRequest)
                .feature(Feature.ENABLE_LOGGING_OVER_SSL)
                .feature(new Feature.ENABLE_SSL(SslContextBuilder.forClient().build()))
                .feature(new SignalClient.UsingUri(new URI("https://api.weixin.qq.com/cgi-bin")))
                .feature(new SignalClient.UsingPath("/ticket/getticket"))
                .feature(new SignalClient.DecodeResponseBodyAs(FetchTicketResponse.class))
                .<FetchTicketResponse>build()
                .timeout(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            return Observable.error(e);
        }
    }
    
    private void onAccessTokenResponse(final FetchAccessTokenResponse resp) {
        this._accessToken = resp.getAccessToken();
        this._accessTokenExpireInMs = System.currentTimeMillis() + (Long.parseLong(resp.getExpiresIn()) - 30) * 1000L;
        LOG.info("on fetch access token response {}, update token {} and expires timestamp in {}", 
                resp, _accessToken, new Date(_accessTokenExpireInMs));
        
        this._getTokenPolicy = PUSH_TOKEN_NOW;
        
        final List<Subscriber<? super String>> subscribers = this._subscribers4accessToken;
        
        this._subscribersLock.writeLock().lock();
        this._subscribers4accessToken = null;
        this._subscribersLock.writeLock().unlock();
        
        if (null != subscribers) {
            for (Subscriber<? super String> subscriber : subscribers) {
                pushCurrentToken(subscriber);
            }
            subscribers.clear();
        }
    }

    private void onAccessTokenError(final Throwable error) {
        this._getTokenPolicy = PUSH_TOKEN_NOW;
        
        final List<Subscriber<? super String>> subscribers = this._subscribers4accessToken;
        
        this._subscribersLock.writeLock().lock();
        this._subscribers4accessToken = null;
        this._subscribersLock.writeLock().unlock();
        
        if (null != subscribers) {
            for (Subscriber<? super String> subscriber : subscribers) {
                pushCurrentTokenWithError(subscriber, error);
            }
            subscribers.clear();
        }
    }

    private boolean pushCurrentToken(final Subscriber<? super String> subscriber) {
        if (!subscriber.isUnsubscribed()) {
            try {
                if (null != this._accessToken) {
                    subscriber.onNext(this._accessToken);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new RuntimeException("access token is null"));
                }
            } catch (Exception e) {
                LOG.warn("exception when invoke {}.onNext, detail: {}",
                        subscriber, ExceptionUtils.exception2detail(e));
            }
        }
        return true;
    }

    private void pushCurrentTokenWithError(final Subscriber<? super String> subscriber, 
            final Throwable error) {
        if (!subscriber.isUnsubscribed()) {
            try {
                subscriber.onError(error);
            } catch (Exception e) {
                LOG.warn("exception when invoke {}.onError, detail: {}",
                        subscriber, ExceptionUtils.exception2detail(e));
            }
        }
    }
    
    @Override
    public void setMBeanRegister(final MBeanRegister register) {
        this._register = register;
    }
    
    public String getSecret() {
        return _secret;
    }
    
    private String _accessToken;
    
    private long _accessTokenExpireInMs = 0;
    
    private final ReadWriteLock _subscribersLock = new ReentrantReadWriteLock(false);
    private List<Subscriber<? super String>> _subscribers4accessToken;
    
    private volatile Func1<Subscriber<? super String>, Boolean> _getTokenPolicy;
    
    @Inject
    private SignalClient _signalClient;
    
    private volatile boolean _isActive = true;
    
    private volatile Subscription _timer;
    
    @Value("${wechat.wpa}")
    String _wpa;
    
    @Value("${wechat.appid}")
    String _appid;
    
    @Value("${wechat.secret}")
    String _secret;
    
    private MBeanRegister _register;
}
