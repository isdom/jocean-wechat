/**
 *
 */
package org.jocean.wechat.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import org.jocean.http.ContentUtil;
import org.jocean.http.Feature;
import org.jocean.http.Interact;
import org.jocean.http.MessageBody;
import org.jocean.http.MessageUtil;
import org.jocean.http.TransportException;
import org.jocean.idiom.jmx.MBeanRegister;
import org.jocean.idiom.jmx.MBeanRegisterAware;
import org.jocean.idiom.rx.RxObservables;
import org.jocean.idiom.rx.RxObservables.RetryPolicy;
import org.jocean.wechat.WXProtocol;
import org.jocean.wechat.WXProtocol.CreateQrcodeResponse;
import org.jocean.wechat.WXProtocol.OAuthAccessTokenResponse;
import org.jocean.wechat.WechatAPI;
import org.jocean.wechat.spi.CreateQrcodeRequest;
import org.jocean.wechat.spi.OAuthAccessTokenRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import io.netty.handler.codec.http.HttpMethod;
import rx.Observable;
import rx.Observable.Transformer;
import rx.functions.Func1;


public class DefaultWechatAPI implements WechatAPI, MBeanRegisterAware {

    @SuppressWarnings("unused")
    private static final Logger LOG =
            LoggerFactory.getLogger(DefaultWechatAPI.class);

    @Override
    public void setMBeanRegister(final MBeanRegister register) {
        register.registerMBean("name=wxapi", new WechatInfoMXBean() {
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
                return "***";
            }

            @Override
            public String getAccessToken() {
                return _accessToken;
            }

            @Override
            public String getTicket() {
                return _ticket;
            }

            @Override
            public String getExpireTime() {
                return _expire;
            }});
    }

    @Override
    public String getName() {
        return this._name;
    }

    @Override
    public String getAppid() {
        return this._appid;
    }

    @Override
    public String getJsapiTicket() {
        return this._ticket;
    }

    @Override
    public String getAccessToken() {
        return this._accessToken;
    }

    @Override
    public Func1<Interact, Observable<OAuthAccessTokenResponse>> getOAuthAccessToken(final String code) {
        return interact-> {
            final OAuthAccessTokenRequest reqbean = new OAuthAccessTokenRequest();
            reqbean.setCode(code);
            reqbean.setAppid(this._appid);
            reqbean.setSecret(this._secret);

            try {
                return interact.reqbean(reqbean)
                    .feature(Feature.ENABLE_LOGGING_OVER_SSL).execution()
                    .compose(MessageUtil.responseAs(OAuthAccessTokenResponse.class, MessageUtil::unserializeAsJson))
                    .compose(timeoutAndRetry())
                    .doOnNext(WXProtocol.CHECK_WXRESP);
            } catch (final Exception e) {
                return Observable.error(e);
            }
        };
    }

    @Override
    public Func1<Interact, Observable<String>> createVolatileQrcode(final int expireSeconds, final String scenestr) {
        return interact-> {
            final CreateQrcodeRequest reqAndBody = new CreateQrcodeRequest();

            reqAndBody.setAccessToken(this._accessToken);
            reqAndBody.setActionName("QR_STR_SCENE");
            reqAndBody.setExpireSeconds(expireSeconds);
            reqAndBody.setScenestr(scenestr);

            try {
                return interact.method(HttpMethod.POST)
                    .reqbean(reqAndBody)
                    .body(reqAndBody, ContentUtil.TOJSON)
                    .feature(Feature.ENABLE_LOGGING_OVER_SSL).execution()
                    .compose(MessageUtil.responseAs(CreateQrcodeResponse.class, MessageUtil::unserializeAsJson))
                    .compose(timeoutAndRetry())
                    .doOnNext(WXProtocol.CHECK_WXRESP)
                    .map(resp-> "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + urlencodeAsUtf8(resp.getTicket()));
            } catch (final Exception e) {
                return Observable.error(e);
            }
        };
    }

    private static String urlencodeAsUtf8(final String ticket) {
        try {
            return URLEncoder.encode(ticket, "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Func1<Interact, Observable<MessageBody>> downloadMedia(final String mediaId) {
        return interact-> {
            try {
                return interact.method(HttpMethod.GET)
                    .uri("https://api.weixin.qq.com")
                    .path("/cgi-bin/media/get")
                    .paramAsQuery("access_token", this._accessToken)
                    .paramAsQuery("media_id", mediaId)
                    .feature(Feature.ENABLE_LOGGING_OVER_SSL)
                    .feature(Feature.ENABLE_COMPRESSOR)
                    .execution()
                    .flatMap(interaction->interaction.execute())
                    .retryWhen(retryPolicy())
                    .compose(MessageUtil.asBody())
                    .flatMap(body-> {
                        if (body.contentType().startsWith("application/json")) {
                            // error return as json
                            // TODO
                            return Observable.error(new RuntimeException());
                        } else {
                            return Observable.just(body);
                        }
                    })
                    ;
            } catch (final Exception e) {
                return Observable.error(e);
            }
        };
    }

    private Func1<? super Observable<? extends Throwable>, ? extends Observable<?>> retryPolicy() {
        return RxObservables.retryWith(new RetryPolicy<Integer>() {
            @Override
            public Observable<Integer> call(final Observable<Throwable> errors) {
                return errors.compose(RxObservables.retryIfMatch(TransportException.class))
                        .compose(RxObservables.retryMaxTimes(_maxRetryTimes))
                        .compose(RxObservables.retryDelayTo(_retryIntervalBase))
                        ;
            }});
    }

    private <T> Transformer<T, T> timeoutAndRetry() {
        return org -> org.timeout(this._timeoutInMS, TimeUnit.MILLISECONDS).retryWhen(retryPolicy());
    }

    @Value("${wechat.wpa}")
    String _name;

    @Value("${wechat.appid}")
    String _appid;

    @Value("${wechat.secret}")
    String _secret;

    @Value("${wechat.token}")
    String _accessToken;

    @Value("${wechat.ticket}")
    String _ticket;

    @Value("${token.expire}")
    String _expire;

    @Value("${wechat.retrytimes}")
    private final int _maxRetryTimes = 3;

    @Value("${wechat.retryinterval}")
    private final int _retryIntervalBase = 100; // 100 ms

    @Value("${api.timeoutInMs}")
    private final int _timeoutInMS = 10000;
}
