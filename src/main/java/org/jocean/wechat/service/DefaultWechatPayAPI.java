/**
 *
 */
package org.jocean.wechat.service;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.net.URI;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.net.ssl.KeyManagerFactory;
import javax.ws.rs.POST;

import org.jocean.http.Feature;
import org.jocean.http.TransportException;
import org.jocean.http.rosa.SignalClient;
import org.jocean.idiom.BeanFinder;
import org.jocean.idiom.ExceptionUtils;
import org.jocean.idiom.Md5;
import org.jocean.idiom.Proxys;
import org.jocean.idiom.jmx.MBeanRegister;
import org.jocean.idiom.jmx.MBeanRegisterAware;
import org.jocean.idiom.rx.RxObservables;
import org.jocean.idiom.rx.RxObservables.RetryPolicy;
import org.jocean.wechat.WechatPayAPI;
import org.jocean.wechat.spi.SendRedpackRequest;
import org.jocean.wechat.spi.SendRedpackResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.google.common.base.Charsets;
import com.google.common.io.BaseEncoding;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;

public class DefaultWechatPayAPI implements WechatPayAPI, MBeanRegisterAware {
	
    private static final Logger LOG = 
            LoggerFactory.getLogger(DefaultWechatPayAPI.class);

    @Override
    public void setMBeanRegister(final MBeanRegister register) {
        
    }
    
    private void sign(final SendRedpackRequest req) {
        final Field fields[] = req.getClass().getDeclaredFields();//cHis 是实体类名称
        final List<String> list = new ArrayList<String>();
        try {
            Field.setAccessible(fields, true);
            for (int i = 0; i < fields.length; i++) {
                if ( null != fields[i].get(req) ) {
                    list.add(fields[i].getName() + "=" + fields[i].get(req) );
                }
            }
            Collections.sort(list);
            final StringBuilder sb = new StringBuilder();
            for (String string : list) {
                sb.append(string+"&");
            }
            sb.append("key=");
            sb.append(this._key);
            final MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sb.toString().getBytes(Charsets.UTF_8));
            final byte[] signatureBytes = md.digest();
            final String signature = Md5.bytesToHexString(signatureBytes).toUpperCase();
            req.setSign(signature);
        } catch (Exception e) {
            LOG.warn("exception when sign req {}, detail: {}", req, ExceptionUtils.exception2detail(e));
        }
    }
    
    @Override
    public SendRedpackContext sendRedpack() {
        final SendRedpackRequest req = new SendRedpackRequest();
        final Func0<Observable<SendRedpackResult>> action = new Func0<Observable<SendRedpackResult>>() {
            @Override
            public Observable<SendRedpackResult> call() {
                req.setMchId(_mch_id);
                req.setWxappid(_appid);
                sign(req);
                
                try {
                    final KeyStore ks = KeyStore.getInstance("PKCS12");
                    ks.load(new ByteArrayInputStream(BaseEncoding.base64().decode(_certAsBase64)), 
                            _password.toCharArray());
                    
                    final KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
                    kmf.init(ks, _password.toCharArray());
                    final SslContext ctx = SslContextBuilder.forClient().keyManager(kmf).build();
                    
                    final URI apiuri = new URI("https://api.mch.weixin.qq.com");
                    return  _finder.find(SignalClient.class).flatMap(signal ->
                        signal.interaction().request(req)
                        .feature(new SignalClient.UsingMethod(POST.class))
                        .feature(Feature.ENABLE_LOGGING_OVER_SSL)
                        .feature(new Feature.ENABLE_SSL(ctx))
                        .feature(new SignalClient.UsingUri(apiuri))
                        .feature(new SignalClient.UsingPath("/mmpaymkttransfers/sendredpack"))
                        .feature(new SignalClient.DecodeResponseBodyAs(SendRedpackResponse.class))
                        .<SendRedpackResponse>build()
                        .retryWhen(retryPolicy())
                        .map(resp -> Proxys.delegate(SendRedpackResult.class, resp))
                    );
                } catch (Exception e) {
                    return Observable.error(e);
                }
            }};
        return Proxys.delegate(SendRedpackContext.class, req, action);
    }
    
    @Override
    public String getName() {
        return this._name;
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
    
    @Value("${wechat.mch_id}")
    String _mch_id;

    @Value("${wechat.key}")
    String _key;
    
    @Value("${cert.base64}")
    private String _certAsBase64;
    
    @Value("${cert.password}")
    private String _password;
    
    private int _maxRetryTimes = 3;
    private int _retryIntervalBase = 100; // 100 ms
}
