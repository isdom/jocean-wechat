/**
 *
 */
package org.jocean.wechat.service;

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
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
import org.jocean.svr.ResponseUtil;
import org.jocean.wechat.spi.ComponentAuthMessage;
import org.jocean.wechat.spi.EncryptedMessage;
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
import com.google.common.io.BaseEncoding;

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
@Path("/wxopen/")
public class WechatOpenAgent implements MBeanRegisterAware {

    private static final String MBEAN_SUFFIX = "info=wxopen";

    private static final Logger LOG =
            LoggerFactory.getLogger(WechatOpenAgent.class);

    @Path("authevent")
    @POST
    public Object onAuthEvent(@BeanParam final WXVerifyRequest verifyreq, final Observable<MessageBody> omb) {
        if (null == verifyreq.getMsgSignature() || verifyreq.getMsgSignature().isEmpty()) {
            return "success";
        }
        if (verifySignature(verifyreq.getSignature(), _verifyToken, verifyreq.getTimestamp(), verifyreq.getNonce())) {
            return omb.flatMap(body->MessageUtil.<EncryptedMessage>decodeXmlAs(body, EncryptedMessage.class))
                    .map(decrypt2auth())
                    .doOnNext(processAuthEvent())
                    .map(authmsg->ResponseUtil.responseAsText(200, "success"));
        } else {
            return "success";
        }
    }

    private Func1<EncryptedMessage, ComponentAuthMessage> decrypt2auth() {
        return encryptmsg -> {
            try {
                LOG.info("encryptmsg:{}", encryptmsg);
                return MessageUtil.unserializeAsXml(
                        new ByteArrayInputStream(
                                decrypt(encryptmsg.getEncrypt(), encryptmsg.getAppId()).getBytes(Charsets.UTF_8)),
                        ComponentAuthMessage.class);
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    /**
     * 对密文进行解密.
     *
     * @param text 需要解密的密文
     * @return 解密得到的明文
     * @throws AesException aes解密失败
     */
    String decrypt(final String text, final String appid) throws AesException {
        final byte[] aesKey = BaseEncoding.base64().decode(this._encodingAesKey + "=");
        byte[] original;
        try {
            // 设置解密模式为AES的CBC模式
            final Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            final SecretKeySpec key_spec = new SecretKeySpec(aesKey, "AES");
            final IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(aesKey, 0, 16));
            cipher.init(Cipher.DECRYPT_MODE, key_spec, iv);

            // 使用BASE64对密文进行解码
            final byte[] encrypted = BaseEncoding.base64().decode(text);

            // 解密
            original = cipher.doFinal(encrypted);
        } catch (final Exception e) {
            LOG.warn("exception when decrypt, detail:{}", ExceptionUtils.exception2detail(e));
            throw new AesException(AesException.DecryptAESError);
        }

        String xmlContent, from_appid;
        try {
            // 去除补位字符
            final byte[] bytes = decode(original);

            // 分离16位随机字符串,网络字节序和AppId
            final byte[] networkOrder = Arrays.copyOfRange(bytes, 16, 20);

            final int xmlLength = recoverNetworkBytesOrder(networkOrder);

            xmlContent = new String(Arrays.copyOfRange(bytes, 20, 20 + xmlLength), Charsets.UTF_8);
            from_appid = new String(Arrays.copyOfRange(bytes, 20 + xmlLength, bytes.length), Charsets.UTF_8);
        } catch (final Exception e) {
            LOG.warn("exception when decrypt, detail:{}", ExceptionUtils.exception2detail(e));
            throw new AesException(AesException.IllegalBuffer);
        }

        // appid不相同的情况
        if (!from_appid.equals(appid)) {
            throw new AesException(AesException.ValidateAppidError);
        }
        return xmlContent;
    }

    /**
     * 删除解密后明文的补位字符
     *
     * @param decrypted 解密后的明文
     * @return 删除补位字符后的明文
     */
    static byte[] decode(final byte[] decrypted) {
        int pad = (int) decrypted[decrypted.length - 1];
        if (pad < 1 || pad > 32) {
            pad = 0;
        }
        return Arrays.copyOfRange(decrypted, 0, decrypted.length - pad);
    }

    // 还原4个字节的网络字节序
    int recoverNetworkBytesOrder(final byte[] orderBytes) {
        int sourceNumber = 0;
        for (int i = 0; i < 4; i++) {
            sourceNumber <<= 8;
            sourceNumber |= orderBytes[i] & 0xff;
        }
        return sourceNumber;
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
            LOG.info("authmsg:{}", authmsg);
            if ("component_verify_ticket".equals(authmsg.getInfoType())) {
                LOG.info("authmsg infotype is component_verify_ticket");
                updateTicket(authmsg.getComponentVerifyTicket());
            }
        };
    }

    private void updateTicket(final String componentVerifyTicket) {
        LOG.info("update component_verify_ticket: {}", componentVerifyTicket);
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
        if (null == this._componentVerifyTicket) {
            //  NOT recv component_verify_ticket
            // TBD, init _getTokenPolicy
            scheduleUpdateToken(60, TimeUnit.SECONDS);
            return;
        }

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

            this._register.unregisterMBean(MBEAN_SUFFIX);
            this._register.registerMBean(MBEAN_SUFFIX, new WechatOpenMXBean() {

                @Override
                public String getName() {
                    return _name;
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
                public String getComponentToken() {
                    return _componentToken;
                }

                @Override
                public String getExpireTime() {
                    return expireTimeAsString;
                }});
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

    @Value("${wxopen.name}")
    String _name;

    @Value("${wxopen.appid}")
    String _appid;

    @Value("${wxopen.secret}")
    String _secret;

    @Value("${verify.token}")
    String _verifyToken;

    @Value("${encoding.aes.key}")
    String _encodingAesKey;

    private volatile String _componentVerifyTicket = null;

    private MBeanRegister _register;
}
