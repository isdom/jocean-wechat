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

    static class PreAuthCodeReq {
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
    public Func1<Interact, Observable<PreAuthCodeResponse>> createPreAuthCode() {
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

    static class QueryAuthReq {
        @JSONField(name = "component_appid")
        public String getComponentAppid() {
            return this._componentAppid;
        }

        @JSONField(name = "component_appid")
        public void setComponentAppid(final String appid) {
            this._componentAppid = appid;
        }

        @JSONField(name = "authorization_code")
        public String getAuthorizationCode() {
            return this._authorizationCode;
        }

        @JSONField(name = "authorization_code")
        public void setAuthorizationCode(final String code) {
            this._authorizationCode = code;
        }

        private String _componentAppid;
        private String _authorizationCode;
    }

    /* (non-Javadoc)
     * https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1453779503&token=&lang=zh_CN
     * 4、使用授权码换取公众号或小程序的接口调用凭据和授权信息
     * 该API用于使用授权码换取授权公众号或小程序的授权信息，并换取authorizer_access_token和authorizer_refresh_token。
     * 授权码的获取，需要在用户在第三方平台授权页中完成授权流程后，在回调URI中通过URL参数提供给第三方平台方。
     * 请注意，由于现在公众号或小程序可以自定义选择部分权限授权给第三方平台，因此第三方平台开发者需要通过该接口来获取公众号或小程序具体授权了哪些权限，
     * 而不是简单地认为自己声明的权限就是公众号或小程序授权的权限。

     * 接口调用请求说明
     * http请求方式: POST（请使用https协议）
     * https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token=xxxx
     * POST数据示例:
     * {
     * "component_appid":"appid_value" ,
     * "authorization_code": "auth_code_value"
     * }
     * 请求参数说明
     * 参数  说明
     * component_appid 第三方平台appid
     * authorization_code  授权code,会在授权成功时返回给第三方平台，详见第三方平台授权流程说明
     * 返回结果示例
     * {
     * "authorization_info": {
     * "authorizer_appid": "wxf8b4f85f3a794e77",
     * "authorizer_access_token": "QXjUqNqfYVH0yBE1iI_7vuN_9gQbpjfK7hYwJ3P7xOa88a89-Aga5x1NMYJyB8G2yKt1KCl0nPC3W9GJzw0Zzq_dBxc8pxIGUNi_bFes0qM",
     * "expires_in": 7200,
     * "authorizer_refresh_token": "dTo-YCXPL4llX-u1W1pPpnp8Hgm4wpJtlR6iV0doKdY",
     * "func_info": [
     * {
     * "funcscope_category": {
     * "id": 1
     * }
     * },
     * {
     * "funcscope_category": {
     * "id": 2
     * }
     * },
     * {
     * "funcscope_category": {
     * "id": 3
     * }
     * }
     * ]
     * }}
     * 结果参数说明
     * 参数  说明
     * authorization_info  授权信息
     * authorizer_appid    授权方appid
     * authorizer_access_token 授权方接口调用凭据（在授权的公众号或小程序具备API权限时，才有此返回值），也简称为令牌
     * expires_in  有效期（在授权的公众号或小程序具备API权限时，才有此返回值）
     * authorizer_refresh_token    接口调用凭据刷新令牌（在授权的公众号具备API权限时，才有此返回值），刷新令牌主要用于第三方平台获取和刷新已授权用户的access_token，只会在授权时刻提供，请妥善保存。 一旦丢失，只能让用户重新授权，才能再次拿到新的刷新令牌
     * func_info   授权给开发者的权限集列表，ID为1到26分别代表：
     *                  1、消息管理权限
     *                  2、用户管理权限
     *                  3、帐号服务权限
     *                  4、网页服务权限
     *                  5、微信小店权限
     *                  6、微信多客服权限
     *                  7、群发与通知权限
     *                  8、微信卡券权限
     *                  9、微信扫一扫权限
     *                  10、微信连WIFI权限
     *                  11、素材管理权限
     *                  12、微信摇周边权限
     *                  13、微信门店权限
     *                  14、微信支付权限
     *                  15、自定义菜单权限
     *                  16、获取认证状态及信息
     *                  17、帐号管理权限（小程序）
     *                  18、开发管理与数据分析权限（小程序）
     *                  19、客服消息管理权限（小程序）
     *                  20、微信登录权限（小程序）
     *                  21、数据分析权限（小程序）
     *                  22、城市服务接口权限
     *                  23、广告管理权限
     *                  24、开放平台帐号管理权限
     *                  25、 开放平台帐号管理权限（小程序）
     *                  26、微信电子发票权限
     *    请注意： 1）该字段的返回不会考虑公众号是否具备该权限集的权限（因为可能部分具备），请根据公众号的帐号类型和认证情况，来判断公众号的接口权限。
     *
     *
     * @see org.jocean.wechat.WXOpenAPI#queryAuth(java.lang.String)
     */
    @Override
    public Func1<Interact, Observable<QueryAuthResponse>> queryAuth(final String authorizationCode) {
        return interact-> {
            try {
                final QueryAuthReq req = new QueryAuthReq();
                req.setComponentAppid(this._appid);
                req.setAuthorizationCode(authorizationCode);

                return interact.method(HttpMethod.POST)
                    .feature(Feature.ENABLE_LOGGING_OVER_SSL)
                    .uri("https://api.weixin.qq.com")
                    .path("/cgi-bin/component/api_query_auth")
                    .paramAsQuery("component_access_token", this._componentToken)
                    .body(req, ContentUtil.TOJSON)
                    .execution()
                    .compose(MessageUtil.responseAs(QueryAuthResponse.class, MessageUtil::unserializeAsJson))
                    .compose(timeoutAndRetry());
            } catch (final Exception e) {
                return Observable.error(e);
            }
        };
    }

    static class AuthorizerTokenReq {
        @JSONField(name = "component_appid")
        public String getComponentAppid() {
            return this._componentAppid;
        }

        @JSONField(name = "component_appid")
        public void setComponentAppid(final String appid) {
            this._componentAppid = appid;
        }

        @JSONField(name = "authorizer_appid")
        public String getAuthorizerAppid() {
            return this._authorizerAppid;
        }

        @JSONField(name = "authorizer_appid")
        public void setAuthorizerAppid(final String appid) {
            this._authorizerAppid = appid;
        }

        @JSONField(name = "authorizer_refresh_token")
        public String getAuthorizerRefreshToken() {
            return this._authorizerRefreshToken;
        }

        @JSONField(name = "authorizer_refresh_token")
        public void setAuthorizerRefreshToken(final String refreshToken) {
            this._authorizerRefreshToken = refreshToken;
        }

        private String _componentAppid;
        private String _authorizerAppid;
        private String _authorizerRefreshToken;
    }

    /* (non-Javadoc)
     * @see org.jocean.wechat.WXOpenAPI#authorizerToken(java.lang.String, java.lang.String)
     * https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1453779503&token=&lang=zh_CN
     * 5、获取（刷新）授权公众号或小程序的接口调用凭据（令牌）
     * 该API用于在授权方令牌（authorizer_access_token）失效时，可用刷新令牌（authorizer_refresh_token）获取新的令牌。
     * 请注意，此处token是2小时刷新一次，开发者需要自行进行token的缓存，避免token的获取次数达到每日的限定额度。
     * 缓存方法可以参考：http://mp.weixin.qq.com/wiki/2/88b2bf1265a707c031e51f26ca5e6512.html
     * 当换取 authorizer_refresh_token后建议保存。
     * 接口调用请求说明
     * http请求方式: POST（请使用https协议）
     * https:// api.weixin.qq.com /cgi-bin/component/api_authorizer_token?component_access_token=xxxxx
     * POST数据示例:

     * {
     * "component_appid":"appid_value",
     * "authorizer_appid":"auth_appid_value",
     * "authorizer_refresh_token":"refresh_token_value",
     * }
     * 请求参数说明
     * 参数  说明
     * component_appid             第三方平台appid
     * authorizer_appid            授权方appid
     * authorizer_refresh_token    授权方的刷新令牌，刷新令牌主要用于第三方平台获取和刷新已授权用户的access_token，
     *                             只会在授权时刻提供，请妥善保存。一旦丢失，只能让用户重新授权，才能再次拿到新的刷新令牌
     * 返回结果示例
     * {
     * "authorizer_access_token":
     *    "aaUl5s6kAByLwgV0BhXNuIFFUqfrR8vTATsoSHukcIGqJgrc4KmMJ-JlKoC_-NKCLBvuU1cWPv4vDcLN8Z0pn5I45mpATruU0b51hzeT1f8",
     * "expires_in": 7200,
     * "authorizer_refresh_token":
     * "BstnRqgTJBXb9N2aJq6L5hzfJwP406tpfahQeLNxX0w"
     * }
     * 结果参数说明
     * 参数  说明
     * authorizer_access_token     授权方令牌
     * expires_in                  有效期，为2小时
     * authorizer_refresh_token    刷新令牌
     *
     */
    @Override
    public Func1<Interact, Observable<AuthorizerTokenResponse>> authorizerToken(final String authorizerAppid, final String refreshToken) {
        return interact-> {
            try {
                final AuthorizerTokenReq req = new AuthorizerTokenReq();
                req.setComponentAppid(this._appid);
                req.setAuthorizerAppid(authorizerAppid);
                req.setAuthorizerRefreshToken(refreshToken);

                return interact.method(HttpMethod.POST)
                    .feature(Feature.ENABLE_LOGGING_OVER_SSL)
                    .uri("https://api.weixin.qq.com")
                    .path("/cgi-bin/component/api_authorizer_token")
                    .paramAsQuery("component_access_token", this._componentToken)
                    .body(req, ContentUtil.TOJSON)
                    .execution()
                    .compose(MessageUtil.responseAs(AuthorizerTokenResponse.class, MessageUtil::unserializeAsJson))
                    .compose(timeoutAndRetry());
            } catch (final Exception e) {
                return Observable.error(e);
            }
        };
    }

    static class AuthorizerInfoReq {
        @JSONField(name = "component_appid")
        public String getComponentAppid() {
            return this._componentAppid;
        }

        @JSONField(name = "component_appid")
        public void setComponentAppid(final String appid) {
            this._componentAppid = appid;
        }

        @JSONField(name = "authorizer_appid")
        public String getAuthorizerAppid() {
            return this._authorizerAppid;
        }

        @JSONField(name = "authorizer_appid")
        public void setAuthorizerAppid(final String appid) {
            this._authorizerAppid = appid;
        }

        private String _componentAppid;
        private String _authorizerAppid;
    }

    /* (non-Javadoc)
     * @see org.jocean.wechat.WXOpenAPI#getAuthorizerInfo(java.lang.String)
     * https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1453779503&token=&lang=zh_CN
     * 获取授权方的帐号基本信息
     * 该API用于获取授权方的基本信息，包括头像、昵称、帐号类型、认证类型、微信号、原始ID和二维码图片URL。

     * 需要特别记录授权方的帐号类型，在消息及事件推送时，对于不具备客服接口的公众号，需要在5秒内立即响应；
     *                而若有客服接口，则可以选择暂时不响应，而选择后续通过客服接口来发送消息触达粉丝。
     * 1）公众号获取方法如下：

     * 接口调用请求说明

     * http请求方式: POST（请使用https协议）
     * https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_info?component_access_token=xxxx
     * POST数据示例:
     * {
     * "component_appid":"appid_value" ,
     * "authorizer_appid": "auth_appid_value"
     * }
     * 请求参数说明
     * 参数  说明
     * component_appid     第三方平台appid
     * authorizer_appid    授权方appid
     * 返回结果示例
     * {
     * "authorizer_info": {
     * "nick_name": "微信SDK Demo Special",
     * "head_img": "http://wx.qlogo.cn/mmopen/GPy",
     * "service_type_info": { "id": 2 },
     * "verify_type_info": { "id": 0 },
     * "user_name":"gh_eb5e3a772040",
     * "principal_name":"腾讯计算机系统有限公司",
     * "business_info": {"open_store": 0, "open_scan": 0, "open_pay": 0, "open_card": 0,
     * "open_shake": 0},
     * "alias":"paytest01"
     * "qrcode_url":"URL",
     * },
     * "authorization_info": {
     * "authorization_appid": "wxf8b4f85f3a794e77",
     * "func_info": [
     * { "funcscope_category": { "id": 1 } },
     * { "funcscope_category": { "id": 2 } },
     * { "funcscope_category": { "id": 3 } }
     * ]
     * }
     * }
     * 结果参数说明
     * 参数  说明
     * nick_name   授权方昵称
     * head_img    授权方头像
     * service_type_info   授权方公众号类型，0代表订阅号，1代表由历史老帐号升级后的订阅号，2代表服务号
     * verify_type_info    授权方认证类型，-1代表未认证，
     *                                 0代表微信认证，
     *                                 1代表新浪微博认证，
     *                                 2代表腾讯微博认证，
     *                                 3代表已资质认证通过但还未通过名称认证，
     *                                 4代表已资质认证通过、还未通过名称认证，但通过了新浪微博认证，
     *                                 5代表已资质认证通过、还未通过名称认证，但通过了腾讯微博认证
     * user_name       授权方公众号的原始ID
     * principal_name  公众号的主体名称
     * alias           授权方公众号所设置的微信号，可能为空
     * business_info   用以了解以下功能的开通状况（0代表未开通，1代表已开通）：
     *                         open_store:是否开通微信门店功能
     *                         open_scan:是否开通微信扫商品功能
     *                         open_pay:是否开通微信支付功能
     *                         open_card:是否开通微信卡券功能
     *                         open_shake:是否开通微信摇一摇功能
     * qrcode_url      二维码图片的URL，开发者最好自行也进行保存
     * authorization_info  授权信息
     * authorization_appid 授权方appid
     * func_info   公众号授权给开发者的权限集列表，ID为1到15时分别代表：
     *                         1.消息管理权限
     *                         2.用户管理权限
     *                         3.帐号服务权限
     *                         4.网页服务权限
     *                         5.微信小店权限
     *                         6.微信多客服权限
     *                         7.群发与通知权限
     *                         8.微信卡券权限
     *                         9.微信扫一扫权限
     *                         10.微信连WIFI权限
     *                         11.素材管理权限
     *                         12.微信摇周边权限
     *                         13.微信门店权限
     *                         14.微信支付权限
     *                         15.自定义菜单权限
     *                         请注意： 1）该字段的返回不会考虑公众号是否具备该权限集的权限（因为可能部分具备），
     *                                 请根据公众号的帐号类型和认证情况，来判断公众号的接口权限。
     */
    @Override
    public Func1<Interact, Observable<AuthorizerInfoResponse>> getAuthorizerInfo(final String authorizerAppid) {
        return interact-> {
            try {
                final AuthorizerInfoReq req = new AuthorizerInfoReq();
                req.setComponentAppid(this._appid);
                req.setAuthorizerAppid(authorizerAppid);

                return interact.method(HttpMethod.POST)
                    .feature(Feature.ENABLE_LOGGING_OVER_SSL)
                    .uri("https://api.weixin.qq.com")
                    .path("/cgi-bin/component/api_get_authorizer_info")
                    .paramAsQuery("component_access_token", this._componentToken)
                    .body(req, ContentUtil.TOJSON)
                    .execution()
                    .compose(MessageUtil.responseAs(AuthorizerInfoResponse.class, MessageUtil::unserializeAsJson))
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
