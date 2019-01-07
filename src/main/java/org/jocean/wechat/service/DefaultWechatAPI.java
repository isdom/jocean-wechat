/**
 *
 */
package org.jocean.wechat.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.jocean.http.ContentUtil;
import org.jocean.http.MessageBody;
import org.jocean.http.MessageUtil;
import org.jocean.http.RpcRunner;
import org.jocean.idiom.jmx.MBeanRegister;
import org.jocean.idiom.jmx.MBeanRegisterAware;
import org.jocean.wechat.WXProtocol;
import org.jocean.wechat.WXProtocol.CreateQrcodeResponse;
import org.jocean.wechat.WXProtocol.OAuthAccessTokenResponse;
import org.jocean.wechat.WechatAPI;
import org.jocean.wechat.spi.CreateQrcodeRequest;
import org.jocean.wechat.spi.OAuthAccessTokenRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import rx.Observable;
import rx.Observable.Transformer;


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

    @Override
    public Transformer<RpcRunner, OAuthAccessTokenResponse> getOAuthAccessToken(final String code) {
        return runners -> runners.flatMap(runner -> runner.name("wxmp.getOAuthAccessToken").execute(interact -> {
            final OAuthAccessTokenRequest reqbean = new OAuthAccessTokenRequest();
            reqbean.setCode(code);
            reqbean.setAppid(this._appid);
            reqbean.setSecret(this._secret);

            try {
                return interact.reqbean(reqbean).responseAs(ContentUtil.ASJSON, OAuthAccessTokenResponse.class)
                    .doOnNext(WXProtocol.CHECK_WXRESP);
            } catch (final Exception e) {
                return Observable.error(e);
            }
        }));
    }

    @Override
    public Transformer<RpcRunner, String> createVolatileQrcode(final int expireSeconds, final String scenestr) {
        return runners -> runners.flatMap(runner -> runner.name("wxmp.createVolatileQrcode").execute(interact -> {
            final CreateQrcodeRequest reqAndBody = new CreateQrcodeRequest();

            reqAndBody.setAccessToken(this._accessToken);
            reqAndBody.setActionName("QR_STR_SCENE");
            reqAndBody.setExpireSeconds(expireSeconds);
            reqAndBody.setScenestr(scenestr);

            try {
                return interact.method(HttpMethod.POST).reqbean(reqAndBody).body(reqAndBody, ContentUtil.TOJSON)
                    .responseAs(ContentUtil.ASJSON, CreateQrcodeResponse.class).doOnNext(WXProtocol.CHECK_WXRESP)
                    .map(resp-> "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + urlencodeAsUtf8(resp.getTicket()));
            } catch (final Exception e) {
                return Observable.error(e);
            }
        }));
    }

    private static String urlencodeAsUtf8(final String ticket) {
        try {
            return URLEncoder.encode(ticket, "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Transformer<RpcRunner, MessageBody> downloadMedia(final String mediaId) {
        return runners -> runners.flatMap(runner -> runner.name("wxmp.downloadMedia").execute(interact -> {
            try {
                return interact.method(HttpMethod.GET)
                    .uri("https://api.weixin.qq.com")
                    .path("/cgi-bin/media/get")
                    .paramAsQuery("access_token", this._accessToken)
                    .paramAsQuery("media_id", mediaId)
                    .response()
                    .flatMap(fullmsg -> {
                        final String contentType = fullmsg.message().headers().get(HttpHeaderNames.CONTENT_TYPE);
                        if (contentType.startsWith("application/json")) {
                            // error return as json
                            return fullmsg.body().compose(MessageUtil.body2bean(ContentUtil.ASJSON, WXProtocol.WXAPIResponse.class))
                                .flatMap(resp -> Observable.error(
                                        new RuntimeException(resp.getErrcode() + "/" + resp.getErrmsg())));
                        } else {
                            return fullmsg.body();
                        }
                    });
            } catch (final Exception e) {
                return Observable.error(e);
            }
        }));
    }

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
}
