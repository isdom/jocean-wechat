/**
 *
 */
package org.jocean.wechat.service;

import javax.inject.Inject;

import org.jocean.http.Feature;
import org.jocean.http.MessageBody;
import org.jocean.http.MessageUtil;
import org.jocean.http.TransportException;
import org.jocean.http.client.HttpClient;
import org.jocean.http.util.ParamUtil;
import org.jocean.idiom.BeanFinder;
import org.jocean.idiom.Terminable;
import org.jocean.idiom.jmx.MBeanRegister;
import org.jocean.idiom.jmx.MBeanRegisterAware;
import org.jocean.idiom.rx.RxObservables;
import org.jocean.idiom.rx.RxObservables.RetryPolicy;
import org.jocean.wechat.WechatAPI;
import org.jocean.wechat.spi.DownloadMediaRequest;
import org.jocean.wechat.spi.OAuthAccessTokenRequest;
import org.jocean.wechat.spi.OAuthAccessTokenResponse;
import org.jocean.wechat.spi.UserInfoRequest;
import org.jocean.wechat.spi.UserInfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import rx.Observable;
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
    
    public Observable<UserInfoResponse> getUserInfo(final String openid) {
        try {
            final UserInfoRequest reqbean = new UserInfoRequest();
            reqbean.setAccessToken(this._accessToken);
            reqbean.setOpenid(openid);

            final SslContext sslctx = SslContextBuilder.forClient().build();

            return this._finder.find(HttpClient.class)
                    .flatMap(client -> MessageUtil.interaction(client).reqbean(reqbean)
                            .feature(Feature.ENABLE_LOGGING_OVER_SSL, new Feature.ENABLE_SSL(sslctx))
                            .responseAs(UserInfoResponse.class, ParamUtil::parseContentAsJson));
        } catch (Exception e) {
            return Observable.error(e);
        }
    }
    
    public Observable<UserInfoResponse> getSnsapiUserInfo(final String snsapiAccessToken, final String openid) {
        try {
            final SslContext sslctx = SslContextBuilder.forClient().build();

            return this._finder.find(HttpClient.class)
                    .flatMap(client -> MessageUtil.interaction(client)
                            .feature(Feature.ENABLE_LOGGING_OVER_SSL, new Feature.ENABLE_SSL(sslctx))
                            .uri("https://api.weixin.qq.com")
                            .path("/sns/userinfo")
                            .paramAsQuery("access_token", snsapiAccessToken)
                            .paramAsQuery("openid", openid)
                            .paramAsQuery("lang", "zh_CN")
                            .responseAs(UserInfoResponse.class, ParamUtil::parseContentAsJson));
        } catch (Exception e) {
            return Observable.error(e);
        }
    }
    
    @Override
    public Observable<OAuthAccessTokenResponse> getOAuthAccessToken(final String code) {
        final OAuthAccessTokenRequest reqbean = new OAuthAccessTokenRequest();
        reqbean.setCode(code);
        reqbean.setAppid(this._appid);
        reqbean.setSecret(this._secret);

        try {
            final SslContext sslctx = SslContextBuilder.forClient().build();

            return this._finder.find(HttpClient.class)
                    .flatMap(client -> MessageUtil.interaction(client).reqbean(reqbean)
                            .feature(Feature.ENABLE_LOGGING_OVER_SSL, new Feature.ENABLE_SSL(sslctx))
                            .responseAs(OAuthAccessTokenResponse.class, ParamUtil::parseContentAsJson));
        } catch (Exception e) {
            return Observable.error(e);
        }
    }

    @Override
    public Observable<MessageBody> downloadMedia(final Terminable terminable, final String mediaId) {
        final DownloadMediaRequest reqbean = new DownloadMediaRequest();
        reqbean.setAccessToken(this._accessToken);
        reqbean.setMediaId(mediaId);

        try {
            final SslContext sslctx = SslContextBuilder.forClient().build();
            return this._finder.find(HttpClient.class)
                    .flatMap(client -> MessageUtil.interaction(client).reqbean(reqbean)
                            .feature(Feature.ENABLE_LOGGING_OVER_SSL, new Feature.ENABLE_SSL(sslctx))
                            .feature(Feature.ENABLE_COMPRESSOR)
                            .responseAs(terminable))
                    .retryWhen(retryPolicy()).compose(MessageUtil.asBody());
        } catch (Exception e) {
            return Observable.error(e);
        }
    }
    
    public void setMaxRetryTimes(final int maxRetryTimes) {
        this._maxRetryTimes = maxRetryTimes;
    }

    public void setRetryIntervalBase(final int retryIntervalBase) {
        this._retryIntervalBase = retryIntervalBase;
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

    @Inject
    private BeanFinder _finder;
    
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
            
    private int _maxRetryTimes = 3;
    private int _retryIntervalBase = 100; // 100 ms
}
