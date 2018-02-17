/**
 *
 */
package org.jocean.wechat.service;

import javax.inject.Inject;

import org.jocean.http.Feature;
import org.jocean.http.MessageUtil;
import org.jocean.http.TransportException;
import org.jocean.http.client.HttpClient;
import org.jocean.idiom.BeanFinder;
import org.jocean.idiom.jmx.MBeanRegister;
import org.jocean.idiom.jmx.MBeanRegisterAware;
import org.jocean.idiom.rx.RxObservables;
import org.jocean.idiom.rx.RxObservables.RetryPolicy;
import org.jocean.wechat.WechatMinaAPI;
import org.jocean.wechat.spi.Code2SessionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import rx.Observable;
import rx.functions.Func1;


public class DefaultWechatMinaAPI implements WechatMinaAPI, MBeanRegisterAware {
	
    @SuppressWarnings("unused")
    private static final Logger LOG = 
            LoggerFactory.getLogger(DefaultWechatMinaAPI.class);

    @Override
    public void setMBeanRegister(final MBeanRegister register) {
        register.registerMBean("name=wxmina", new MinaInfoMXBean() {
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
    public Observable<Code2SessionResponse> code2session(final String code) {
        try {
            return this._finder.find(HttpClient.class)
                .flatMap(client -> MessageUtil.interaction(client)
                        .feature(Feature.ENABLE_LOGGING_OVER_SSL)
                        .uri("https://api.weixin.qq.com").path("/sns/jscode2session")
                        .paramAsQuery("appid", this._appid).paramAsQuery("secret", this._secret)
                        .paramAsQuery("js_code", code).paramAsQuery("grant_type", "authorization_code")
                        .execution())
                .compose(MessageUtil.responseAs(Code2SessionResponse.class, MessageUtil::unserializeAsJson))
                .retryWhen(retryPolicy())
                .doOnNext(resp -> {
                    if (null != resp.getErrcode()) {
                        throw new RuntimeException(resp.toString());
                    }
                });
        } catch (Exception e) {
            return Observable.error(e);
        }
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
    
    @Value("${mina.name}")
    String _name;
    
    @Value("${mina.appid}")
    String _appid;
    
    @Value("${mina.secret}")
    String _secret;

    @Value("${mina.retrytimes}")
    private int _maxRetryTimes = 3;
    
    @Value("${mina.retryinterval}")
    private int _retryIntervalBase = 100; // 100 ms
}
