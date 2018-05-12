package org.jocean.wechat.service;

import java.util.concurrent.TimeUnit;

import org.jocean.http.Feature;
import org.jocean.http.Interact;
import org.jocean.http.MessageUtil;
import org.jocean.http.TransportException;
import org.jocean.idiom.rx.RxObservables;
import org.jocean.idiom.rx.RxObservables.RetryPolicy;
import org.jocean.wechat.WXCommonAPI;
import org.jocean.wechat.WXProtocol;
import org.jocean.wechat.WXProtocol.UserInfoResponse;
import org.springframework.beans.factory.annotation.Value;

import rx.Observable;
import rx.Observable.Transformer;
import rx.functions.Func1;

public class DefaultWXCommonAPI implements WXCommonAPI {

    @Override
    public Func1<Interact, Observable<UserInfoResponse>> getUserInfo(final String accessToken, final String openid) {
        return interact-> {
            try {
                return interact.feature(Feature.ENABLE_LOGGING_OVER_SSL)
                    .uri("https://api.weixin.qq.com").path("/cgi-bin/user/info")
                    .paramAsQuery("access_token", accessToken)
                    .paramAsQuery("openid", openid)
                    .paramAsQuery("lang", "zh_CN")
                    .execution()
                    .compose(MessageUtil.responseAs(UserInfoResponse.class, MessageUtil::unserializeAsJson))
                    .compose(timeoutAndRetry())
                    .doOnNext(WXProtocol.CHECK_WXRESP);
            } catch (final Exception e) {
                return Observable.error(e);
            }
        };
    }

    @Override
    public Func1<Interact, Observable<UserInfoResponse>> getSnsUserInfo(final String oauth2Token, final String openid) {
        return interact-> {
            try {
                return interact.feature(Feature.ENABLE_LOGGING_OVER_SSL)
                    .uri("https://api.weixin.qq.com").path("/sns/userinfo")
                    .paramAsQuery("access_token", oauth2Token)
                    .paramAsQuery("openid", openid)
                    .paramAsQuery("lang", "zh_CN")
                    .execution()
                    .compose(MessageUtil.responseAs(UserInfoResponse.class, MessageUtil::unserializeAsJson))
                    .compose(timeoutAndRetry())
                    .doOnNext(WXProtocol.CHECK_WXRESP);
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

    @Value("${api.retrytimes}")
    private final int _maxRetryTimes = 3;

    @Value("${api.retryinterval}")
    private final int _retryIntervalBase = 100; // 100 ms

    @Value("${api.timeoutInMs}")
    private final int _timeoutInMS = 10000;
}
