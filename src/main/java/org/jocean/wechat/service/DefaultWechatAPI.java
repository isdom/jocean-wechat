/**
 *
 */
package org.jocean.wechat.service;

import java.net.URI;
import java.net.URLEncoder;

import javax.inject.Inject;
import javax.ws.rs.GET;

import org.jocean.http.Feature;
import org.jocean.http.TransportException;
import org.jocean.http.rosa.SignalClient;
import org.jocean.idiom.jmx.MBeanRegister;
import org.jocean.idiom.jmx.MBeanRegisterAware;
import org.jocean.idiom.rx.RxObservables;
import org.jocean.idiom.rx.RxObservables.RetryPolicy;
import org.jocean.wechat.WechatAPI;
import org.jocean.wechat.spi.OAuthAccessTokenRequest;
import org.jocean.wechat.spi.OAuthAccessTokenResponse;
import org.jocean.wechat.spi.UserInfoRequest;
import org.jocean.wechat.spi.UserInfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

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
    
    public Observable<UserInfoResponse> getUserInfo(final String openid) {
        return getUserInfo(this._accessToken, openid);
    }
    
    public Observable<UserInfoResponse> getUserInfo(final String accessToken, final String openid) {
        try {
            final UserInfoRequest req = new UserInfoRequest();
            req.setAccessToken(accessToken);
            req.setOpenid(openid);
            
            return _signalClient.interaction().request(req)
                .feature(Feature.ENABLE_LOGGING_OVER_SSL)
                .feature(new Feature.ENABLE_SSL(SslContextBuilder.forClient().build()))
                .feature(new SignalClient.UsingUri(new URI("https://api.weixin.qq.com/cgi-bin")))
                .feature(new SignalClient.UsingPath("/user/info"))
                .feature(new SignalClient.DecodeResponseBodyAs(UserInfoResponse.class))
                .<UserInfoResponse>build();
        } catch (Exception e) {
            return Observable.error(e);
        }
    }
    
    public Observable<UserInfoResponse> getSnsapiUserInfo(final String snsapiAccessToken, final String openid) {
        try {
            return _signalClient.interaction()
                .feature(Feature.ENABLE_LOGGING_OVER_SSL)
                .feature(new Feature.ENABLE_SSL(SslContextBuilder.forClient().build()))
                .feature(new SignalClient.UsingMethod(GET.class))
                .feature(new SignalClient.UsingUri(new URI("https://api.weixin.qq.com")))
                .feature(new SignalClient.UsingPath("/sns/userinfo?access_token=" 
                        + URLEncoder.encode(snsapiAccessToken, "UTF-8")
                        + "&openid="
                        + URLEncoder.encode(openid, "UTF-8")
                        + "&lang=zh_CN"
                        ))
                .feature(new SignalClient.DecodeResponseBodyAs(UserInfoResponse.class))
                .<UserInfoResponse>build();
        } catch (Exception e) {
            return Observable.error(e);
        }
    }
    
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
    private SignalClient _signalClient;
    
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
