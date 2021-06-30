package org.jocean.wechat;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jocean.rpc.annotation.ConstParams;
import org.jocean.rpc.annotation.OnResponse;
import org.jocean.rpc.annotation.RpcBuilder;
import org.jocean.wechat.WXProtocol.Code2SessionResponse;
import org.jocean.wechat.WXProtocol.OAuthAccessTokenResponse;
import org.jocean.wechat.WXProtocol.WXAPIResponse;
import org.jocean.wechat.spi.FetchComponentTokenResponse;

import com.alibaba.fastjson.annotation.JSONField;

import rx.Observable;

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

    @RpcBuilder
    interface CreatePreAuthCodeBuilder {

        @QueryParam("component_access_token")
        public CreatePreAuthCodeBuilder componentAccessToken(final String accessToken);

        @JSONField(name = "component_appid")
        public CreatePreAuthCodeBuilder componentAppid(final String appid);

        @POST
        @Path("https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode")
        @Consumes(MediaType.APPLICATION_JSON)
        @OnResponse("org.jocean.wechat.WXProtocol.CHECK_WXRESP")
        Observable<PreAuthCodeResponse> call();
    }

    public CreatePreAuthCodeBuilder createPreAuthCode();

    @RpcBuilder
    interface QueryAuthBuilder {

        @QueryParam("component_access_token")
        public QueryAuthBuilder componentAccessToken(final String accessToken);

        @JSONField(name = "component_appid")
        public QueryAuthBuilder componentAppid(final String appid);

        @JSONField(name = "authorization_code")
        public QueryAuthBuilder authorizationCode(final String code);

        @POST
        @Path("https://api.weixin.qq.com/cgi-bin/component/api_query_auth")
        @Consumes(MediaType.APPLICATION_JSON)
        @OnResponse("org.jocean.wechat.WXProtocol.CHECK_WXRESP")
        Observable<QueryAuthResponse> call();
    }

    public QueryAuthBuilder queryAuth();


    @RpcBuilder
    interface AuthorizerTokenBuilder {

        @QueryParam("component_access_token")
        public AuthorizerTokenBuilder componentAccessToken(final String accessToken);

        @JSONField(name = "component_appid")
        public AuthorizerTokenBuilder componentAppid(final String appid);

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

    @RpcBuilder
    interface GetAuthorizerInfoBuilder {

        @QueryParam("component_access_token")
        public GetAuthorizerInfoBuilder componentAccessToken(final String accessToken);

        @JSONField(name = "component_appid")
        public GetAuthorizerInfoBuilder componentAppid(final String appid);

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
    interface GetOAuthAccessTokenBuilder {

        @QueryParam("component_access_token")
        public GetOAuthAccessTokenBuilder componentAccessToken(final String accessToken);

        @QueryParam("component_appid")
        public GetOAuthAccessTokenBuilder componentAppid(final String appid);

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
    interface Code2SessionBuilder {

        @QueryParam("component_access_token")
        public Code2SessionBuilder componentAccessToken(final String accessToken);

        @QueryParam("component_appid")
        public Code2SessionBuilder componentAppid(final String appid);

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
