package org.jocean.wechat;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jocean.rpc.annotation.ConstParams;
import org.jocean.rpc.annotation.OnBuild;
import org.jocean.rpc.annotation.OnResponse;
import org.jocean.rpc.annotation.RpcBuilder;
import org.jocean.wechat.WXProtocol.Code2SessionResponse;
import org.jocean.wechat.WXProtocol.OAuthAccessTokenResponse;
import org.jocean.wechat.WXProtocol.WXAPIResponse;
import org.jocean.wechat.spi.FetchComponentTokenResponse;

import com.alibaba.fastjson.annotation.JSONField;

import rx.Observable;
import rx.functions.Action2;

public interface WechatOpenAPI {

    public interface PreAuthCodeResponse extends WXAPIResponse {
        @JSONField(name = "pre_auth_code")
        public String getPreAuthCode();

        @JSONField(name = "pre_auth_code")
        public void setPreAuthCode(final String code);

        @JSONField(name = "expires_in")
        public int getExpires();

        @JSONField(name = "expires_in")
        public void setExpires(final int expires);
    }

    interface FuncscopeCategory {
        @JSONField(name = "id")
        public int getId();
        @JSONField(name = "id")
        public void setId(final int id);
    }

    interface FuncInfo {
        @JSONField(name = "funcscope_category")
        public FuncscopeCategory getCategory();

        @JSONField(name = "funcscope_category")
        public void setCategory(final FuncscopeCategory category);
    }

    interface AuthorizerTokenInfo {
        @JSONField(name = "authorizer_access_token")
        public String getAuthorizerAccessToken();

        @JSONField(name = "authorizer_access_token")
        public void setAuthorizerAccessToken(final String token);

        @JSONField(name = "authorizer_refresh_token")
        public String getAuthorizerRefreshToken();

        @JSONField(name = "authorizer_refresh_token")
        public void setAuthorizerRefreshToken(final String refreshToken);

        @JSONField(name = "expires_in")
        public int getExpires();

        @JSONField(name = "expires_in")
        public void setExpires(final int expires);
    }

    interface AuthorizationInfo extends AuthorizerTokenInfo {
        @JSONField(name = "authorizer_appid")
        public String getAuthorizerAppid();

        @JSONField(name = "authorizer_appid")
        public void setAuthorizerAppid(final String appid);

        @JSONField(name = "func_info")
        public FuncInfo[] getFuncInfos();

        @JSONField(name = "func_info")
        public void setFuncInfos(final FuncInfo[] infos);
    }

    public interface QueryAuthResponse extends WXAPIResponse {

        @JSONField(name = "authorization_info")
        public AuthorizationInfo getAuthorizationInfo();

        @JSONField(name = "authorization_info")
        public void setAuthorizationInfo(final AuthorizationInfo info);
    }

    public interface AuthorizerTokenResponse extends AuthorizerTokenInfo, WXAPIResponse {
    }

    interface HasId {
        @JSONField(name = "id")
        public int getId();

        @JSONField(name = "id")
        public void setId(final int id);
    }

    interface BusinessInfo {
        @JSONField(name = "open_store")
        public int getOpenStore();

        @JSONField(name = "open_store")
        public void setOpenStore(final int valid);

        @JSONField(name = "open_scan")
        public int getOpenScan();

        @JSONField(name = "open_scan")
        public void setOpenScan(final int valid);

        @JSONField(name = "open_pay")
        public int getOpenPay();

        @JSONField(name = "open_pay")
        public void setOpenPay(final int valid);

        @JSONField(name = "open_card")
        public int getOpenCard();

        @JSONField(name = "open_card")
        public void setOpenCard(final int valid);

        @JSONField(name = "open_shake")
        public int getOpenShake();

        @JSONField(name = "open_shake")
        public void setOpenShake(final int valid);
    }

    interface MiniProgramInfo {
        // "network":{"RequestDomain":["https:xxx","https:xxxx","https:xxx"],"WsRequestDomain":[],
        //              "UploadDomain":[],"DownloadDomain":[],"BizDomain":[],"UDPDomain":[]},

        interface Network {
            @JSONField(name = "RequestDomain")
            public String[] getRequestDomain();

            @JSONField(name = "RequestDomain")
            public void setRequestDomain(final String[] domains);

            @JSONField(name = "WsRequestDomain")
            public String[] getWsRequestDomain();

            @JSONField(name = "WsRequestDomain")
            public void setWsRequestDomain(final String[] domains);

            @JSONField(name = "UploadDomain")
            public String[] getUploadDomain();

            @JSONField(name = "UploadDomain")
            public void setUploadDomain(final String[] domains);

            @JSONField(name = "DownloadDomain")
            public String[] getDownloadDomain();

            @JSONField(name = "DownloadDomain")
            public void setDownloadDomain(final String[] domains);

            @JSONField(name = "BizDomain")
            public String[] getBizDomain();

            @JSONField(name = "BizDomain")
            public void setBizDomain(final String[] domains);

            @JSONField(name = "UDPDomain")
            public String[] getUDPDomain();

            @JSONField(name = "UDPDomain")
            public void setUDPDomain(final String[] domains);
        }

        //  "categories":[{"first":"工具","second":"效率"}]
        interface Categories {
            @JSONField(name = "first")
            public String getFirst();

            @JSONField(name = "first")
            public void setFirst(final String first);

            @JSONField(name = "second")
            public String getSecond();

            @JSONField(name = "second")
            public void setSecond(final String second);
        }

        // {"network":{"RequestDomain":["https:xxx","https:xxxx","https:xxx"],"WsRequestDomain":[],"UploadDomain":[],"DownloadDomain":[],"BizDomain":[],"UDPDomain":[]},
        // "categories":[{"first":"工具","second":"效率"}],"visit_status":0}}
        @JSONField(name = "network")
        public Network getNetwork();

        @JSONField(name = "network")
        public void setNetwork(final Network network);

        @JSONField(name = "categories")
        public Categories getCategories();

        @JSONField(name = "categories")
        public void setCategories(final Categories categories);

        @JSONField(name = "visit_status")
        public int getVisitStatus();

        @JSONField(name = "visit_status")
        public void setVisitStatus(final int status);
    }

    interface AuthorizerInfo {
        @JSONField(name = "nick_name")
        public String getNickName();

        @JSONField(name = "nick_name")
        public void setNickName(final String nickName);

        @JSONField(name = "head_img")
        public String getHeadImg();

        @JSONField(name = "head_img")
        public void setHeadImg(final String img);

        @JSONField(name = "service_type_info")
        public HasId getServiceTypeInfo();

        @JSONField(name = "service_type_info")
        public void setServiceTypeInfo(final HasId info);

        @JSONField(name = "verify_type_info")
        public HasId getVerifyTypeInfo();

        @JSONField(name = "verify_type_info")
        public void setVerifyTypeInfo(final HasId info);

        @JSONField(name = "user_name")
        public String getUserName();

        @JSONField(name = "user_name")
        public void setUserName(final String name);

        @JSONField(name = "principal_name")
        public String getPrincipalName();

        @JSONField(name = "principal_name")
        public void setPrincipalName(final String name);

        @JSONField(name = "business_info")
        public BusinessInfo getBusinessInfo();

        @JSONField(name = "business_info")
        public void setBusinessInfo(final BusinessInfo info);

        @JSONField(name = "alias")
        public String getAlias();

        @JSONField(name = "alias")
        public void setAlias(final String alias);

        @JSONField(name = "qrcode_url")
        public String getQrcodeUrl();

        @JSONField(name = "qrcode_url")
        public void setQrcodeUrl(final String url);

        @JSONField(name = "MiniProgramInfo")
        public String getMiniProgramInfo();

        @JSONField(name = "MiniProgramInfo")
        public void setMiniProgramInfo(final MiniProgramInfo miniProgramInfo);

    }

    public interface AuthorizerInfoResponse extends WXAPIResponse {
        @JSONField(name = "authorizer_info")
        public AuthorizerInfo getAuthorizerInfo();

        @JSONField(name = "authorizer_info")
        public void setAuthorizerInfo(final AuthorizerInfo info);

        @JSONField(name = "authorization_info")
        public AuthorizationInfo getAuthorizationInfo();

        @JSONField(name = "authorization_info")
        public void setAuthorizationInfo(final AuthorizationInfo info);
    }

    @RpcBuilder
    interface FetchComponentTokenBuilder {
        @JSONField(name="component_appid")
        public FetchComponentTokenBuilder componentAppid(final String appid);

        @JSONField(name="component_appsecret")
        public FetchComponentTokenBuilder componentSecret(final String secret);

        @JSONField(name="component_verify_ticket")
        public FetchComponentTokenBuilder componentVerifyTicket(final String ticket);

        @POST
        @Path("https://api.weixin.qq.com/cgi-bin/component/api_component_token")
        @Consumes(MediaType.APPLICATION_JSON)
        @OnResponse("org.jocean.wechat.WXProtocol.CHECK_WXRESP")
        Observable<FetchComponentTokenResponse> call();
    }

    public FetchComponentTokenBuilder fetchComponentToken();

    public static Action2<OpenComponentableQJ<?>, WXOpenComponent> SET_WXC_QJ = (openComponentable, wxc) ->
        openComponentable.componentAccessToken(wxc.getComponentToken()).componentAppid(wxc.getComponentAppid());

    interface OpenComponentableQJ<BUILDER extends OpenComponentableQJ<?>> {
        @QueryParam("component_access_token")
        BUILDER componentAccessToken(final String accessToken);

        @JSONField(name = "component_appid")
        BUILDER componentAppid(final String appid);

        @OnBuild("org.jocean.wechat.WechatOpenAPI.SET_WXC_QJ")
        BUILDER wxOpenComponent(final WXOpenComponent wxc);
    }

    public static Action2<OpenComponentableQQ<?>, WXOpenComponent> SET_WXC_QQ = (openComponentable, wxc) ->
        openComponentable.componentAccessToken(wxc.getComponentToken()).componentAppid(wxc.getComponentAppid());

    interface OpenComponentableQQ<BUILDER extends OpenComponentableQQ<?>> {
        @QueryParam("component_access_token")
        BUILDER componentAccessToken(final String accessToken);

        @QueryParam("component_appid")
        BUILDER componentAppid(final String appid);

        @OnBuild("org.jocean.wechat.WechatOpenAPI.SET_WXC_QQ")
        BUILDER wxOpenComponent(final WXOpenComponent wxc);
    }

    @RpcBuilder
    interface CreatePreAuthCodeBuilder extends OpenComponentableQJ<CreatePreAuthCodeBuilder> {
//        @QueryParam("component_access_token")
//        public CreatePreAuthCodeBuilder componentAccessToken(final String accessToken);

//        @JSONField(name = "component_appid")
//        public CreatePreAuthCodeBuilder componentAppid(final String appid);

        @POST
        @Path("https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode")
        @Consumes(MediaType.APPLICATION_JSON)
        @OnResponse("org.jocean.wechat.WXProtocol.CHECK_WXRESP")
        Observable<PreAuthCodeResponse> call();
    }

    public CreatePreAuthCodeBuilder createPreAuthCode();

    /*
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
    */
    @RpcBuilder
    interface QueryAuthBuilder extends OpenComponentableQJ<QueryAuthBuilder> {

//        @QueryParam("component_access_token")
//        public QueryAuthBuilder componentAccessToken(final String accessToken);
//
//        @JSONField(name = "component_appid")
//        public QueryAuthBuilder componentAppid(final String appid);

        @JSONField(name = "authorization_code")
        public QueryAuthBuilder authorizationCode(final String code);

        @POST
        @Path("https://api.weixin.qq.com/cgi-bin/component/api_query_auth")
        @Consumes(MediaType.APPLICATION_JSON)
        @OnResponse("org.jocean.wechat.WXProtocol.CHECK_WXRESP")
        Observable<QueryAuthResponse> call();
    }

    public QueryAuthBuilder queryAuth();

    /*
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
    @RpcBuilder
    interface AuthorizerTokenBuilder extends OpenComponentableQJ<AuthorizerTokenBuilder> {

//        @QueryParam("component_access_token")
//        public AuthorizerTokenBuilder componentAccessToken(final String accessToken);
//
//        @JSONField(name = "component_appid")
//        public AuthorizerTokenBuilder componentAppid(final String appid);

        @JSONField(name = "authorizer_appid")
        public AuthorizerTokenBuilder authorizerAppid(final String authorizerAppid);

        @JSONField(name = "authorizer_refresh_token")
        public AuthorizerTokenBuilder authorizerRefreshToken(final String refreshToken);

        @POST
        @Path("https://api.weixin.qq.com/cgi-bin/component/api_authorizer_token")
        @Consumes(MediaType.APPLICATION_JSON)
        @OnResponse("org.jocean.wechat.WXProtocol.CHECK_WXRESP")
        Observable<AuthorizerTokenResponse> call();
    }

    public AuthorizerTokenBuilder authorizerToken();

    /*
     * https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1453779503&token=&lang=zh_CN
     *
     * Update 2021-07-23
     * https://developers.weixin.qq.com/doc/oplatform/Third-party_Platforms/2.0/api/ThirdParty/token/api_get_authorizer_info.html
     *
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
    @RpcBuilder
    interface GetAuthorizerInfoBuilder extends OpenComponentableQJ<GetAuthorizerInfoBuilder> {

        @JSONField(name = "authorizer_appid")
        public GetAuthorizerInfoBuilder authorizerAppid(final String authorizerAppid);

        @POST
        @Path("https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_info")
        @Consumes(MediaType.APPLICATION_JSON)
        @OnResponse("org.jocean.wechat.WXProtocol.CHECK_WXRESP")
        Observable<AuthorizerInfoResponse> call();
    }

    public GetAuthorizerInfoBuilder getAuthorizerInfo();

    @RpcBuilder
    interface GetOAuthAccessTokenBuilder extends OpenComponentableQQ<GetOAuthAccessTokenBuilder> {

        @QueryParam("appid")
        public GetOAuthAccessTokenBuilder authorizerAppid(final String authorizerAppid);

        @QueryParam("code")
        public GetOAuthAccessTokenBuilder code(final String code);

        @GET
        @Path("https://api.weixin.qq.com/sns/oauth2/component/access_token")
        @ConstParams({"grant_type", "authorization_code"})
        @Consumes(MediaType.APPLICATION_JSON)
        @OnResponse("org.jocean.wechat.WXProtocol.CHECK_WXRESP")
        Observable<OAuthAccessTokenResponse> call();
    }

    public GetOAuthAccessTokenBuilder getOAuthAccessToken();

    @RpcBuilder
    interface Code2SessionBuilder extends OpenComponentableQQ<Code2SessionBuilder> {

//        @QueryParam("component_access_token")
//        public Code2SessionBuilder componentAccessToken(final String accessToken);
//
//        @QueryParam("component_appid")
//        public Code2SessionBuilder componentAppid(final String appid);

        @QueryParam("appid")
        public GetOAuthAccessTokenBuilder minaAppid(final String minaAppid);

        @QueryParam("js_code")
        public GetOAuthAccessTokenBuilder code(final String code);

        @GET
        @Path("https://api.weixin.qq.com/sns/component/jscode2session")
        @ConstParams({"grant_type", "authorization_code"})
        @Consumes(MediaType.APPLICATION_JSON)
        @OnResponse("org.jocean.wechat.WXProtocol.CHECK_WXRESP")
        Observable<Code2SessionResponse> call();
    }
    // 小程序: Mina
    // https://developers.weixin.qq.com/doc/oplatform/Third-party_Platforms/Mini_Programs/WeChat_login.html
    // refer: https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1492585163_FtTNA&token=&lang=zh_CN
    public Code2SessionBuilder code2session();
}
