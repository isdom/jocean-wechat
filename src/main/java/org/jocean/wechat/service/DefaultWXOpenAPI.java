/**
 *
 */
package org.jocean.wechat.service;

import java.util.concurrent.TimeUnit;

import org.jocean.http.TransportException;
import org.jocean.idiom.jmx.MBeanRegister;
import org.jocean.idiom.jmx.MBeanRegisterAware;
import org.jocean.idiom.rx.RxObservables;
import org.jocean.idiom.rx.RxObservables.RetryPolicy;
import org.jocean.wechat.WXOpenAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import rx.Observable;
import rx.Observable.Transformer;
import rx.functions.Func1;


public class DefaultWXOpenAPI implements WXOpenAPI, MBeanRegisterAware {

    @SuppressWarnings("unused")
    private static final Logger LOG =
            LoggerFactory.getLogger(DefaultWXOpenAPI.class);

    @Override
    public void setMBeanRegister(final MBeanRegister register) {
        register.registerMBean("name=wxopen", new WechatOpenMXBean() {
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
            public String getExpireTime() {
                return _expire;
            }

            @Override
            public String getComponentToken() {
                return _componentToken;
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
    public String getComponentToken() {
        return this._componentToken;
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

    @Value("${wxopen.name}")
    String _name;

    @Value("${wxopen.appid}")
    String _appid;

    @Value("${wxopen.secret}")
    String _secret;

    @Value("${wxopen.componentToken}")
    String _componentToken;

    @Value("${token.expire}")
    String _expire;

    @Value("${api.retrytimes}")
    private final int _maxRetryTimes = 3;

    @Value("${api.retryinterval}")
    private final int _retryIntervalBase = 100; // 100 ms

    @Value("${api.timeoutInMs}")
    private final int _timeoutInMS = 10000;
}
