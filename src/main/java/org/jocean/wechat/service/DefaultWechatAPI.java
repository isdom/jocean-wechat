/**
 *
 */
package org.jocean.wechat.service;

import java.net.URI;

import javax.inject.Inject;

import org.jocean.http.Feature;
import org.jocean.http.TransportException;
import org.jocean.http.rosa.SignalClient;
import org.jocean.idiom.rx.RxObservables;
import org.jocean.idiom.rx.RxObservables.RetryPolicy;
import org.jocean.wechat.WechatAPI;
import org.jocean.wechat.spi.UserInfoRequest;
import org.jocean.wechat.spi.UserInfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.handler.ssl.SslContextBuilder;
import rx.Observable;
import rx.functions.Func1;


public class DefaultWechatAPI implements WechatAPI {
	
    private static final Logger LOG = 
            LoggerFactory.getLogger(DefaultWechatAPI.class);
    
    @Override
    public String getAppid() {
        return this._appid;
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
    
    public void setAppid(final String appid) {
        this._appid = appid;
    }
    
    public void setAccessToken(final String accessToken) {
        this._accessToken = accessToken;
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
    
    private String _appid;
    
    private String _accessToken;
    
    private int _maxRetryTimes = 3;
    private int _retryIntervalBase = 100; // 100 ms
}
