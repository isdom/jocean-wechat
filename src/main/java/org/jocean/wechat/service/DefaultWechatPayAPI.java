/**
 *
 */
package org.jocean.wechat.service;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.KeyManagerFactory;

import org.jocean.http.ContentUtil;
import org.jocean.http.Feature;
import org.jocean.http.Interact;
import org.jocean.http.MessageUtil;
import org.jocean.idiom.ExceptionUtils;
import org.jocean.idiom.Md5;
import org.jocean.idiom.Proxys;
import org.jocean.idiom.Proxys.RET;
import org.jocean.idiom.jmx.MBeanRegister;
import org.jocean.idiom.jmx.MBeanRegisterAware;
import org.jocean.wechat.WechatPayAPI;
import org.jocean.wechat.spi.SendRedpackRequest;
import org.jocean.wechat.spi.SendRedpackResponse;
import org.jocean.wechat.spi.UnifiedOrderRequest;
import org.jocean.wechat.spi.UnifiedOrderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.google.common.base.Charsets;
import com.google.common.io.BaseEncoding;

import io.netty.handler.codec.http.HttpMethod;
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

    private String signOf(final Object req) {
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
            for (final String string : list) {
                sb.append(string+"&");
            }
            sb.append("key=");
            sb.append(this._key);
            final MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sb.toString().getBytes(Charsets.UTF_8));
            final byte[] signatureBytes = md.digest();
            return Md5.bytesToHexString(signatureBytes).toUpperCase();
        } catch (final Exception e) {
            LOG.warn("exception when sign req {}, detail: {}", req, ExceptionUtils.exception2detail(e));
            throw new RuntimeException(e);
        }
    }

    private final class SendingRedpack implements Func0<Func1<Interact, Observable<SendRedpackResult>>> {
        private final SendRedpackRequest reqAndBody;

        private SendingRedpack(final SendRedpackRequest reqAndBody) {
            this.reqAndBody = reqAndBody;
        }

        @Override
        public Func1<Interact, Observable<SendRedpackResult>> call() {
            return interact-> {
                reqAndBody.setMchId(_mch_id);
                reqAndBody.setWxappid(_appid);
                reqAndBody.setSign( signOf(reqAndBody) );

                try {
                    final KeyStore ks = KeyStore.getInstance("PKCS12");
                    ks.load(new ByteArrayInputStream(BaseEncoding.base64().decode(_certAsBase64)),
                            _certPassword.toCharArray());

                    final KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
                    kmf.init(ks, _certPassword.toCharArray());

                    final SslContext sslctx = SslContextBuilder.forClient().keyManager(kmf).build();

                    return interact.method(HttpMethod.POST)
                            .reqbean(reqAndBody)
                            .body(reqAndBody, ContentUtil.TOXML)
                            .feature(new Feature.ENABLE_SSL(sslctx)).execution()
                            .compose(MessageUtil.responseAs(SendRedpackResponse.class, MessageUtil::unserializeAsXml))
                            .map(resp -> Proxys.delegate(SendRedpackResult.class, resp));
                } catch (final Exception e) {
                    LOG.warn("exception when sendRedpack {}, detail: {}", reqAndBody, ExceptionUtils.exception2detail(e));
                    return Observable.error(e);
                }
        };
        }
    }

    private Func0<Func1<Interact, Observable<SendRedpackResult>>> buildAction(final SendRedpackRequest reqAndBody) {
        return new SendingRedpack(reqAndBody);
    }

    @Override
    public SendRedpackContext sendRedpack() {
        final SendRedpackRequest reqAndBody = new SendRedpackRequest();
        final Func0<Func1<Interact, Observable<SendRedpackResult>>> action = buildAction(reqAndBody);
        return Proxys.delegate(SendRedpackContext.class, new Object[]{reqAndBody, action}, new RET[]{RET.SELF, RET.PASSTHROUGH});
    }

    @Override
    public Func1<Interact, Observable<UnifiedOrderResponse>> unifiedorder(final UnifiedOrderRequest req) {
        return interact-> {
            req.setMchId(_mch_id);
            req.setAppid(_appid);
            req.setSign( signOf(req));

            try {
                return interact.method(HttpMethod.POST)
                        .reqbean(req)
                        .body(req, ContentUtil.TOXML).execution()
                        .compose(MessageUtil.responseAs(UnifiedOrderResponse.class, MessageUtil::unserializeAsXml));
            } catch (final Exception e) {
                LOG.warn("exception when unifiedorder {}, detail: {}", req, ExceptionUtils.exception2detail(e));
                return Observable.error(e);
            }
        };
    }

    @Override
    public String getName() {
        return this._name;
    }

    @Value("${wechat.wpa}")
    String _name;

    @Value("${wechat.appid}")
    String _appid;

    @Value("${wechat.mch_id}")
    String _mch_id;

    @Value("${wechat.key}")
    String _key;

    @Value("${cert.base64}")
    String _certAsBase64;

    @Value("${cert.password}")
    String _certPassword;
}
