/**
 *
 */
package org.jocean.wechat.service;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.jocean.http.Feature;
import org.jocean.http.MessageBody;
import org.jocean.http.MessageUtil;
import org.jocean.http.client.HttpClient;
import org.jocean.idiom.BeanFinder;
import org.jocean.idiom.ExceptionUtils;
import org.jocean.idiom.Md5;
import org.jocean.idiom.jmx.MBeanRegister;
import org.jocean.idiom.jmx.MBeanRegisterAware;
import org.jocean.wechat.spi.ComponentAuthMessage;
import org.jocean.wechat.spi.FetchComponentTokenRequest;
import org.jocean.wechat.spi.FetchComponentTokenResponse;
import org.jocean.wechat.spi.WXVerifyRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;

/**
 * @author isdom
 *
 */
@Controller
@Scope("singleton")
@Path("/wxcomponent/")
public class WechatOpenAgent implements MBeanRegisterAware {

    private static final String MBEAN_SUFFIX = "info=wechat";

    private static final Logger LOG =
            LoggerFactory.getLogger(WechatOpenAgent.class);

    @Path("authevent")
    @POST
    public Object onAuthEvent(@BeanParam final WXVerifyRequest verifyreq, final Observable<MessageBody> omb) {
        if (null == verifyreq.getMsgSignature() || verifyreq.getMsgSignature().isEmpty()) {
            return "success";
        }
        if (verifySignature(verifyreq.getSignature(), _verifyToken, verifyreq.getTimestamp(), verifyreq.getNonce())) {
            return omb.flatMap(body->MessageUtil.<ComponentAuthMessage>decodeXmlAs(body, ComponentAuthMessage.class))
                    .doOnNext(processAuthEvent())
                    .map(authmsg->"success");
        } else {
            return "success";
        }

    }

    /**
     * 判断是否加密
     * @param signature
     * @param token
     * @param timestamp
     * @param nonce
     *
     * @return
     */
    private static boolean verifySignature(final String signature, final String token, final String timestamp, final String nonce) {
        LOG.info("###token:{};signature:{};timestamp:{};nonce:{}", token, signature, timestamp, nonce);
        if (signature != null && !signature.equals("") && timestamp != null && !timestamp.equals("") && nonce != null
                && !nonce.equals("")) {
            try {
                final String[] ss = new String[] { token, timestamp, nonce };
                Arrays.sort(ss);
                final String allstr = ss[0] + ss[1] + ss[2];
                final MessageDigest md = MessageDigest.getInstance("SHA1");
                md.update(allstr.getBytes(Charsets.UTF_8));
                final byte[] signatureBytes = md.digest();
                final String localSignature = Md5.bytesToHexString(signatureBytes);

                LOG.info("request's signature {}, calced: {}", signature, localSignature);
                return localSignature.equals(signature);
            } catch (final Exception e) {
                LOG.warn("exception when verifySignature, detail: {}", ExceptionUtils.exception2detail(e));
                return false;
            }
        } else {
            return false;
        }
    }

    private Action1<ComponentAuthMessage> processAuthEvent() {
        return authmsg-> {
            if ("component_verify_ticket".equals(authmsg.getInfoType())) {
                updateTicket(authmsg.getComponentVerifyTicket());
            }
        };
    }

    private void updateTicket(final String componentVerifyTicket) {
        this._componentVerifyTicket = componentVerifyTicket;
    }

    public void start() throws Exception {
        startToUpdateComponentToken();
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
    public String getAppid() {
        return this._appid;
    }

    /* (non-Javadoc)
     * @see org.jocean.wechat.WXTokenSource#getAccessToken(boolean)
     */
    public Observable<String> getAccessToken(final boolean forceRefresh) {
        return Observable.unsafeCreate(subscriber -> {
            if (!subscriber.isUnsubscribed()) {
                while (!this._getTokenPolicy.call(subscriber))
                    ;
            }
        });
    }

    private final Func1<Subscriber<? super String>, Boolean> WAIT_FOR_UPDATE = subscriber->addToPendings(subscriber);
    private final Func1<Subscriber<? super String>, Boolean> PUSH_TOKEN_NOW = subscriber->pushCurrentToken(subscriber);

    private void startToUpdateComponentToken() throws Exception {
        this._subscribers4ComponentToken = Lists.newCopyOnWriteArrayList();
        this._getTokenPolicy = WAIT_FOR_UPDATE;

        fetchComponentTokenFromWXOpenAPI().doOnNext(resp -> onComponentTokenResponse(resp))
            .subscribe(resp -> {
                updateMBean(resp.getComponentToken());
                scheduleUpdateToken(Math.max(0L, this._componentTokenExpireInMs - System.currentTimeMillis()),
                        TimeUnit.MILLISECONDS);
            }, error -> {
                onComponentTokenError(error);
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
                        startToUpdateComponentToken();
                    } catch (final Exception e) {
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
            final List<Subscriber<? super String>> subscribers = this._subscribers4ComponentToken;
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

    private void updateMBean(final String compnentToken) {
        if (null!=this._register) {
            final String expireTimeAsString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(new Date(this._componentTokenExpireInMs));

            /* TBD
            this._register.unregisterMBean(MBEAN_SUFFIX);
            this._register.registerMBean(MBEAN_SUFFIX, new WechatInfoMXBean() {

                @Override
                public String getName() {
                    return _wpa;
                }

                @Override
                public String getAccessToken() {
                    return _componentToken;
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
                    return compnentToken;
                }});
                */
        }
    }

    private Observable<FetchComponentTokenResponse> fetchComponentTokenFromWXOpenAPI() {
        final FetchComponentTokenRequest reqbean = new FetchComponentTokenRequest();

        reqbean.setComponentAppid(this._appid);
        reqbean.setComponentSecret(this._secret);
        reqbean.setComponentVerifyTicket(this._componentVerifyTicket);

        try {
            return this._finder.find(HttpClient.class)
                    .flatMap(client -> MessageUtil.interact(client).reqbean(reqbean)
                            .feature(Feature.ENABLE_LOGGING_OVER_SSL).execution())
                    .compose(MessageUtil.responseAs(FetchComponentTokenResponse.class, MessageUtil::unserializeAsJson))
                    .timeout(10, TimeUnit.SECONDS);
        } catch (final Exception e) {
            return Observable.error(e);
        }
    }

    private void onComponentTokenResponse(final FetchComponentTokenResponse resp) {
        this._componentToken = resp.getComponentToken();
        this._componentTokenExpireInMs = System.currentTimeMillis() + (Long.parseLong(resp.getExpiresIn()) - 30) * 1000L;
        LOG.info("on fetch component token response {}, update token {} and expires timestamp in {}",
                resp, _componentToken, new Date(_componentTokenExpireInMs));

        this._getTokenPolicy = PUSH_TOKEN_NOW;

        final List<Subscriber<? super String>> subscribers = this._subscribers4ComponentToken;

        this._subscribersLock.writeLock().lock();
        this._subscribers4ComponentToken = null;
        this._subscribersLock.writeLock().unlock();

        if (null != subscribers) {
            for (final Subscriber<? super String> subscriber : subscribers) {
                pushCurrentToken(subscriber);
            }
            subscribers.clear();
        }
    }

    private void onComponentTokenError(final Throwable error) {
        this._getTokenPolicy = PUSH_TOKEN_NOW;

        final List<Subscriber<? super String>> subscribers = this._subscribers4ComponentToken;

        this._subscribersLock.writeLock().lock();
        this._subscribers4ComponentToken = null;
        this._subscribersLock.writeLock().unlock();

        if (null != subscribers) {
            for (final Subscriber<? super String> subscriber : subscribers) {
                pushCurrentTokenWithError(subscriber, error);
            }
            subscribers.clear();
        }
    }

    private boolean pushCurrentToken(final Subscriber<? super String> subscriber) {
        if (!subscriber.isUnsubscribed()) {
            try {
                if (null != this._componentToken) {
                    subscriber.onNext(this._componentToken);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new RuntimeException("access token is null"));
                }
            } catch (final Exception e) {
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
            } catch (final Exception e) {
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

    private String _componentToken;

    private long _componentTokenExpireInMs = 0;

    private final ReadWriteLock _subscribersLock = new ReentrantReadWriteLock(false);
    private List<Subscriber<? super String>> _subscribers4ComponentToken;

    private volatile Func1<Subscriber<? super String>, Boolean> _getTokenPolicy;

    @Inject
    private BeanFinder _finder;

    private volatile boolean _isActive = true;

    private volatile Subscription _timer;

    @Value("${openwx.name}")
    String _name;

    @Value("${openwx.appid}")
    String _appid;

    @Value("${openwx.secret}")
    String _secret;

    @Value("${openwx.verify.token}")
    String _verifyToken;

    private volatile String _componentVerifyTicket;

    private MBeanRegister _register;
}
