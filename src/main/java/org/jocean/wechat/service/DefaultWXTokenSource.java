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
import org.jocean.idiom.Pair;
import org.jocean.j2se.jmx.MBeanRegister;
import org.jocean.j2se.jmx.MBeanRegisterAware;
import org.jocean.wechat.WXTokenSource;
import org.jocean.wechat.spi.FetchAccessTokenRequest;
import org.jocean.wechat.spi.FetchAccessTokenResponse;
import org.jocean.wechat.spi.FetchTicketRequest;
import org.jocean.wechat.spi.FetchTicketResponse;
import org.jocean.wechat.spi.OAuthAccessTokenRequest;
import org.jocean.wechat.spi.OAuthAccessTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.google.common.collect.Lists;

import io.netty.handler.ssl.SslContextBuilder;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;

/**
 * @author isdom
 *
 */
public class DefaultWXTokenSource implements WXTokenSource, MBeanRegisterAware {

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
        return Observable.unsafeCreate(new OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> tokenSubscriber) {
                doGetAccessToken(tokenSubscriber);
            }});
        
    }

    private void doGetAccessToken(final Subscriber<? super String> tokenSubscriber) {
        this._tokenLock.readLock().lock();
        try {
            final Action1<Subscriber<? super String>> policy = this._getTokenPolicy;
            policy.call(tokenSubscriber);
        } finally {
            this._tokenLock.readLock().unlock();
        }
    }
    
    private final Action1<Subscriber<? super String>> WAIT_FOR_UPDATE = 
        new Action1<Subscriber<? super String>>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                addToPendings(subscriber);
            }};
            
    private final Action1<Subscriber<? super String>> USE_CURRENT = 
        new Action1<Subscriber<? super String>>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                pushCurrentToken(subscriber);
            }};
            
    private void startToUpdateToken() throws Exception {
        this._getTokenPolicy = WAIT_FOR_UPDATE;
            
        fetchAccessTokenFromWXOpenAPI()
        .flatMap(new Func1<FetchAccessTokenResponse, 
            Observable<Pair<FetchAccessTokenResponse, FetchTicketResponse>>>() {
                @Override
                public Observable<Pair<FetchAccessTokenResponse, FetchTicketResponse>> call(
                        final FetchAccessTokenResponse tokenResp) {
                    return fetchTicketFromWXOpenAPI(tokenResp.getAccessToken())
                        .map(new Func1<FetchTicketResponse, Pair<FetchAccessTokenResponse, FetchTicketResponse>>() {
                            @Override
                            public Pair<FetchAccessTokenResponse, FetchTicketResponse> call(
                                    final FetchTicketResponse ticketResp) {
                                return Pair.of(tokenResp, ticketResp);
                            }});
                }})
        .subscribe(new Action1<Pair<FetchAccessTokenResponse, FetchTicketResponse>>() {
            @Override
            public void call(final Pair<FetchAccessTokenResponse, FetchTicketResponse> tokenAndTicket) {
                onAccessTokenResponse(tokenAndTicket.first);
                updateMBean(tokenAndTicket.first.getAccessToken(), 
                        tokenAndTicket.second.getTicket(),
                        System.currentTimeMillis() + Long.parseLong(tokenAndTicket.first.getExpiresIn()) * 1000L);
                scheduleUpdateToken(Long.parseLong(tokenAndTicket.first.getExpiresIn()) - 30, TimeUnit.SECONDS);
            }}, new Action1<Throwable>() {
            @Override
            public void call(final Throwable error) {
                onAccessTokenError(error);
                // wait 10s, and retry
                scheduleUpdateToken(10, TimeUnit.SECONDS);
            }})
        ;
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

    private Observable<FetchTicketResponse> fetchTicketFromWXOpenAPI(final String accessToken) {
        final FetchTicketRequest fetchTicketRequest = new FetchTicketRequest();
        
        fetchTicketRequest.setAccessToken(accessToken);
        fetchTicketRequest.setType("jsapi");
        try {
            return _signalClient.interaction().request(fetchTicketRequest)
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
    
    private void scheduleUpdateToken(final long delayInSeconds, final TimeUnit unit) {
        if (this._isActive) {
            this._timer = Observable.timer(delayInSeconds, unit)
            .subscribe(new Action1<Long>() {
                @Override
                public void call(final Long unused) {
                    _timer = null;
                    try {
                        startToUpdateToken();
                    } catch (Exception e) {
                        LOG.warn("exception when start to update, detail: {}",
                            ExceptionUtils.exception2detail(e));
                    }
                }});
        } else {
            LOG.warn("wxtokensouce unactived, ignore scheduleUpdateToken.");
        }
    }

    private void addToPendings(final Subscriber<? super String> subscriber) {
        this._subscribers4accessToken.add(subscriber);
        subscriber.add(Subscriptions.create(new Action0() {
            @Override
            public void call() {
                _subscribers4accessToken.remove(subscriber);
            }}));
    }

    private void updateMBean(final String token,final String ticket, final long expireTime) {
        if (null!=this._register) {
            final String expireTimeAsString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(new Date(expireTime));
            
            this._register.unregisterMBean("info=wechat");
            this._register.registerMBean("info=wechat", new WechatInfoMXBean() {
                
                @Override
                public String getName() {
                    return _wpa;
                }

                @Override
                public String getAccessToken() {
                    return token;
                }

                @Override
                public String getAppid() {
                    return _appid;
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

    private void onAccessTokenResponse(final FetchAccessTokenResponse resp) {
        this._accessToken = resp.getAccessToken();
        this._accessTokenExpireInMs = System.currentTimeMillis() 
                + (Long.parseLong(resp.getExpiresIn()) - 30) * 1000L;
        LOG.info("on fetch access token response {}, update token {} and expires timestamp in {}", 
                resp, _accessToken, new Date(_accessTokenExpireInMs));
        
        this._tokenLock.writeLock().lock();
        try {
            this._getTokenPolicy = USE_CURRENT;
            for (Subscriber<? super String> subscriber : this._subscribers4accessToken) {
                pushCurrentToken(subscriber);
            }
            this._subscribers4accessToken.clear();
        } finally {
            this._tokenLock.writeLock().unlock();
        }
    }

    private void onAccessTokenError(final Throwable error) {
        this._tokenLock.writeLock().lock();
        try {
            for (Subscriber<? super String> subscriber : this._subscribers4accessToken) {
                pushCurrentTokenWithError(subscriber, error);
            }
            this._subscribers4accessToken.clear();
        } finally {
            this._tokenLock.writeLock().unlock();
        }
    }

    private void pushCurrentToken(final Subscriber<? super String> subscriber) {
        if (!subscriber.isUnsubscribed()) {
            try {
                subscriber.onNext(this._accessToken);
                subscriber.onCompleted();
            } catch (Exception e) {
                LOG.warn("exception when invoke {}.onNext, detail: {}",
                        subscriber, ExceptionUtils.exception2detail(e));
            }
        }
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
    
    /* (non-Javadoc)
     * @see org.jocean.wechat.WXTokenSource#getOAuthAccessToken(java.lang.String)
     */
    @Override
    public Observable<OAuthAccessTokenResponse> getOAuthAccessToken(final String code) {
        final OAuthAccessTokenRequest req = new OAuthAccessTokenRequest();
        req.setCode(code);
        req.setAppid(this._appid);
        req.setSecret(this._secret);
        
        try {
            return this._signalClient.interaction().request(req)
                .feature(Feature.ENABLE_LOGGING_OVER_SSL)
                .feature(new Feature.ENABLE_SSL(SslContextBuilder.forClient().build()))
                .feature(new SignalClient.UsingUri(new URI("https://api.weixin.qq.com")))
                .feature(new SignalClient.UsingPath("/sns/oauth2/access_token"))
                .feature(new SignalClient.DecodeResponseBodyAs(OAuthAccessTokenResponse.class))
                .<OAuthAccessTokenResponse>build();
        } catch (Exception e) {
            return Observable.error(e);
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
    
    private final List<Subscriber<? super String>> _subscribers4accessToken = 
            Lists.newCopyOnWriteArrayList();
    
    private volatile Action1<Subscriber<? super String>> _getTokenPolicy = WAIT_FOR_UPDATE;
    
    private ReadWriteLock _tokenLock = new ReentrantReadWriteLock(false);
    
    @Inject
    private SignalClient _signalClient;
    
    private volatile boolean _isActive = true;
    
    private volatile Subscription _timer;
    
    @Value("${wechat.wpa}")
    String _wpa;
    
    @Value("${wechat.appid}")
    String _appid;
    
    @Value("${${wechat.secret}}")
    String _secret;
    
    private MBeanRegister _register;
}
