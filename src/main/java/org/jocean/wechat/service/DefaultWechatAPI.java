/**
 *
 */
package org.jocean.wechat.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.inject.Inject;

import org.jocean.http.ContentUtil;
import org.jocean.http.Feature;
import org.jocean.http.Interact;
import org.jocean.http.MessageBody;
import org.jocean.http.MessageUtil;
import org.jocean.http.TransportException;
import org.jocean.http.client.HttpClient;
import org.jocean.idiom.BeanFinder;
import org.jocean.idiom.Terminable;
import org.jocean.idiom.jmx.MBeanRegister;
import org.jocean.idiom.jmx.MBeanRegisterAware;
import org.jocean.idiom.rx.RxObservables;
import org.jocean.idiom.rx.RxObservables.RetryPolicy;
import org.jocean.wechat.WechatAPI;
import org.jocean.wechat.spi.CreateQrcodeRequest;
import org.jocean.wechat.spi.CreateQrcodeResponse;
import org.jocean.wechat.spi.DownloadMediaRequest;
import org.jocean.wechat.spi.OAuthAccessTokenRequest;
import org.jocean.wechat.spi.OAuthAccessTokenResponse;
import org.jocean.wechat.spi.UserInfoRequest;
import org.jocean.wechat.spi.UserInfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

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
    
    @Override
    public String getAccessToken() {
        return this._accessToken;
    }
    
    @Override
    public Observable<UserInfoResponse> getUserInfo(final String openid) {
        try {
            final UserInfoRequest reqbean = new UserInfoRequest();
            reqbean.setAccessToken(this._accessToken);
            reqbean.setOpenid(openid);

            return this._finder.find(HttpClient.class)
                    .flatMap(client -> MessageUtil.interaction(client).reqbean(reqbean)
                            .feature(Feature.ENABLE_LOGGING_OVER_SSL).execution())
                    .compose(MessageUtil.responseAs(UserInfoResponse.class, MessageUtil::unserializeAsJson));
        } catch (Exception e) {
            return Observable.error(e);
        }
    }
    
    @Override
    public Observable<UserInfoResponse> getSnsapiUserInfo(final String snsapiToken, final String openid) {
        try {
            return this._finder.find(HttpClient.class)
                    .flatMap(client -> MessageUtil.interaction(client)
                            .feature(Feature.ENABLE_LOGGING_OVER_SSL)
                            .uri("https://api.weixin.qq.com").path("/sns/userinfo")
                            .paramAsQuery("access_token", snsapiToken).paramAsQuery("openid", openid)
                            .paramAsQuery("lang", "zh_CN")
                            .execution())
                    .compose(MessageUtil.responseAs(UserInfoResponse.class, MessageUtil::unserializeAsJson));
        } catch (Exception e) {
            return Observable.error(e);
        }
    }
    
    @Override
    public Observable<OAuthAccessTokenResponse> getOAuthAccessToken(final String code) {
        final OAuthAccessTokenRequest reqbean = new OAuthAccessTokenRequest();
        reqbean.setCode(code);
        reqbean.setAppid(this._appid);
        reqbean.setSecret(this._secret);

        try {
            return this._finder.find(HttpClient.class)
                    .flatMap(client -> MessageUtil.interaction(client).reqbean(reqbean)
                            .feature(Feature.ENABLE_LOGGING_OVER_SSL).execution())
                    .compose(MessageUtil.responseAs(OAuthAccessTokenResponse.class, MessageUtil::unserializeAsJson));
        } catch (Exception e) {
            return Observable.error(e);
        }
    }

    public Observable<UserInfoResponse> getUserInfo(final Interact interact, final String openid) {
        try {
            final UserInfoRequest reqbean = new UserInfoRequest();
            reqbean.setAccessToken(this._accessToken);
            reqbean.setOpenid(openid);

            return interact.reqbean(reqbean).feature(Feature.ENABLE_LOGGING_OVER_SSL).execution()
                .compose(MessageUtil.responseAs(UserInfoResponse.class, MessageUtil::unserializeAsJson));
        } catch (Exception e) {
            return Observable.error(e);
        }
    }

    public Observable<UserInfoResponse> getSnsapiUserInfo(final Interact interact, final String snsapiToken, final String openid) {
        try {
            return interact.feature(Feature.ENABLE_LOGGING_OVER_SSL)
                .uri("https://api.weixin.qq.com").path("/sns/userinfo")
                .paramAsQuery("access_token", snsapiToken).paramAsQuery("openid", openid)
                .paramAsQuery("lang", "zh_CN")
                .execution()
                .compose(MessageUtil.responseAs(UserInfoResponse.class, MessageUtil::unserializeAsJson));
        } catch (Exception e) {
            return Observable.error(e);
        }
    }
    
    public Observable<OAuthAccessTokenResponse> getOAuthAccessToken(final Interact interact, final String code) {
        final OAuthAccessTokenRequest reqbean = new OAuthAccessTokenRequest();
        reqbean.setCode(code);
        reqbean.setAppid(this._appid);
        reqbean.setSecret(this._secret);

        try {
            return interact.reqbean(reqbean)
                .feature(Feature.ENABLE_LOGGING_OVER_SSL).execution()
                .compose(MessageUtil.responseAs(OAuthAccessTokenResponse.class, MessageUtil::unserializeAsJson));
        } catch (Exception e) {
            return Observable.error(e);
        }
    }
    
    @Override
    public Observable<String> createVolatileQrcode(final Interact interact, final int expireSeconds, final String scenestr) {
        final CreateQrcodeRequest reqAndBody = new CreateQrcodeRequest();
        
        reqAndBody.setAccessToken(this._accessToken);
        reqAndBody.setActionName("QR_STR_SCENE");
        reqAndBody.setExpireSeconds(expireSeconds);
        reqAndBody.setScenestr(scenestr);

        try {
            return interact.reqbean(reqAndBody)
                .body(reqAndBody, ContentUtil.TOJSON)
                .feature(Feature.ENABLE_LOGGING_OVER_SSL).execution()
                .compose(MessageUtil.responseAs(CreateQrcodeResponse.class, MessageUtil::unserializeAsJson))
                .doOnNext(resp->{
                    if (null!=resp.getErrcode()) {
                        throw new RuntimeException(resp.toString());
                    }
                })
                .map(resp-> "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + urlencodeAsUtf8(resp.getTicket()));
        } catch (Exception e) {
            return Observable.error(e);
        }
    }

    private static String urlencodeAsUtf8(final String ticket) {
        try {
            return URLEncoder.encode(ticket, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public Observable<MessageBody> downloadMedia(final Terminable terminable, final String mediaId) {
        final DownloadMediaRequest reqbean = new DownloadMediaRequest();
        reqbean.setAccessToken(this._accessToken);
        reqbean.setMediaId(mediaId);

        try {
            return this._finder.find(HttpClient.class)
                    .flatMap(client -> MessageUtil.interaction(client).reqbean(reqbean)
                            .feature(Feature.ENABLE_LOGGING_OVER_SSL).feature(Feature.ENABLE_COMPRESSOR).execution())
                    .flatMap(interaction -> {
                        terminable.doOnTerminate(interaction.initiator().closer());
                        return interaction.execute();
                    }).retryWhen(retryPolicy()).compose(MessageUtil.asBody());
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
            
    @Value("${wechat.retrytimes}")
    private int _maxRetryTimes = 3;
    
    @Value("${wechat.retryinterval}")
    private int _retryIntervalBase = 100; // 100 ms
}
