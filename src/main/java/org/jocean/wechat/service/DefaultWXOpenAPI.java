/**
 *
 */
package org.jocean.wechat.service;

import java.util.concurrent.TimeUnit;

import org.jocean.http.ContentUtil;
import org.jocean.http.Feature;
import org.jocean.http.Interact;
import org.jocean.http.MessageUtil;
import org.jocean.http.TransportException;
import org.jocean.idiom.jmx.MBeanRegister;
import org.jocean.idiom.jmx.MBeanRegisterAware;
import org.jocean.idiom.rx.RxObservables;
import org.jocean.idiom.rx.RxObservables.RetryPolicy;
import org.jocean.wechat.WXOpenAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.annotation.JSONField;

import io.netty.handler.codec.http.HttpMethod;
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

    public static class PreAuthCodeReq {
        @JSONField(name = "component_appid")
        public String getComponentAppid() {
            return this._componentAppid;
        }

        @JSONField(name = "component_appid")
        public void setComponentAppid(final String appid) {
            this._componentAppid = appid;
        }

        private String _componentAppid;
    }

    @Override
    public Func1<Interact, Observable<PreAuthCodeResponse>> getPreAuthCode() {
        return interact-> {
            try {
                final PreAuthCodeReq req = new PreAuthCodeReq();
                req.setComponentAppid(this._appid);

                return interact.method(HttpMethod.POST)
                    .feature(Feature.ENABLE_LOGGING_OVER_SSL)
                    .uri("https://api.weixin.qq.com")
                    .path("/cgi-bin/component/api_create_preauthcode")
                    .paramAsQuery("component_access_token", this._componentToken)
                    .body(req, ContentUtil.TOJSON)
                    .execution()
                    .compose(MessageUtil.responseAs(PreAuthCodeResponse.class, MessageUtil::unserializeAsJson))
                    .compose(timeoutAndRetry());
            } catch (final Exception e) {
                return Observable.error(e);
            }
        };
    }

    private <T> Transformer<T, T> timeoutAndRetry() {
        return org -> org.timeout(this._timeoutInMS, TimeUnit.MILLISECONDS).retryWhen(retryPolicy());
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

    @Value("${wxopen.name}")
    String _name;

    @Value("${wxopen.appid}")
    String _appid;

    @Value("${wxopen.secret}")
    String _secret;

    @Value("${wxopen.component.token}")
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
