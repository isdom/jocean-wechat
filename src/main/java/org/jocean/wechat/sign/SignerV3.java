package org.jocean.wechat.sign;

import static org.apache.http.HttpHeaders.AUTHORIZATION;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import org.jocean.idiom.DisposableWrapperUtil;
import org.jocean.netty.util.BufsInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.Signer;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpRequest;
import rx.Observable;
import rx.Observable.Transformer;

// https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay4_0.shtml
public class SignerV3 {

    private static final Logger LOG = LoggerFactory.getLogger(SignerV3.class);

    public static Transformer<Object, Object> signRequest(final String merchantId, final String privateKeySerialNumber, final String privateKeyAsPem) {
        return obsreq -> obsreq.toList().concatMap(reqs -> {
            HttpRequest httpreq = null;
            final Object[] objs = reqs.toArray();
            final BufsInputStream<ByteBuf> bufsis = new BufsInputStream<>(bb -> bb.slice());

            for (final Object obj : objs) {
                if (obj instanceof HttpRequest) {
                    httpreq = (HttpRequest)obj;
                } else if (DisposableWrapperUtil.unwrap(obj) instanceof ByteBuf) {
                    bufsis.appendBuf((ByteBuf)DisposableWrapperUtil.unwrap(obj));
                }
            }
            bufsis.markEOS();
            try {
                final byte[] bytes = new byte[bufsis.available()];
                bufsis.read(bytes);
                final String body = new String(bytes, StandardCharsets.UTF_8);

                final String token = getToken(httpreq, body, merchantId, privateKeySerialNumber, privateKeyAsPem);

                final String Authorization = "WECHATPAY2-SHA256-RSA2048" + " " + token;
                // 添加认证信息
                httpreq.headers().add(AUTHORIZATION, Authorization);

                LOG.info("Authorization: {}", Authorization);
            } catch (final IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                bufsis.close();
            } catch (final IOException e) {
            }

            return Observable.from(reqs);
        });
    }

    static long generateTimestamp() {
        return System.currentTimeMillis() / 1000;
    }

    private static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final SecureRandom RANDOM = new SecureRandom();

    static String generateNonceStr() {
        final char[] nonceChars = new char[32];
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(nonceChars);
    }

    private static String getToken(final HttpRequest req, final String body, final String merchantId, final String privateKeySerialNumber, final String privateKeyAsPem) {
        final StringBuilder sb = new StringBuilder();

        // HTTP请求方法\n
        // URL\n
        // 请求时间戳\n
        // 请求随机串\n
        // 请求报文主体\n

        // 第一步，获取HTTP请求的方法（GET,POST,PUT）等
        sb.append(req.method().name());
        sb.append('\n');

        // 第二步，获取请求的绝对URL，并去除域名部分得到参与签名的URL。如果请求中有查询参数，URL末尾应附加有'?'和对应的查询字符串。
        sb.append(req.uri());
        sb.append('\n');

        // 第三步，获取发起请求时的系统当前时间戳，即格林威治时间1970年01月01日00时00分00秒(北京时间1970年01月01日08时00分00秒)
        //      起至现在的总秒数，作为请求时间戳。微信支付会拒绝处理很久之前发起的请求，请商户保持自身系统的时间准确。
        final long timestamp = generateTimestamp();
        sb.append(timestamp);
        sb.append('\n');

        // 第四步，生成一个请求随机串，可参见生成随机数算法。这里，我们使用命令行直接生成一个。
        final String nonceStr = generateNonceStr();
        sb.append(nonceStr);
        sb.append('\n');

        // 第五步，获取请求中的请求报文主体（request body）。

        //  请求方法为GET时，报文主体为空。
        //  当请求方法为POST或PUT时，请使用真实发送的JSON报文。
        //  图片上传API，请使用meta对应的JSON报文。
        sb.append(body);
        sb.append('\n');

        final String message = sb.toString();

        LOG.info("authorization message=[{}]", message);

        final Signer signer = new PrivateKeySigner(privateKeySerialNumber, PemUtil.loadPrivateKey(privateKeyAsPem));

        final Signer.SignatureResult signature = signer.sign(message.getBytes(StandardCharsets.UTF_8));

        final String token = "mchid=\"" + merchantId + "\","
                + "nonce_str=\"" + nonceStr + "\","
                + "timestamp=\"" + timestamp + "\","
                + "serial_no=\"" + privateKeySerialNumber + "\","
                + "signature=\"" + signature.getSign() + "\"";
        LOG.info("authorization token=[{}]", token);

        return token;
    }
}
