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
import org.jocean.wechat.WXOpenAPI.AuthorizerInfoResponse;
import org.jocean.wechat.WXOpenAPI.AuthorizerTokenResponse;
import org.jocean.wechat.WXOpenAPI.PreAuthCodeResponse;
import org.jocean.wechat.WXOpenAPI.QueryAuthResponse;
import org.jocean.wechat.WXProtocol.OAuthAccessTokenResponse;
import org.jocean.wechat.spi.FetchComponentTokenResponse;

import com.alibaba.fastjson.annotation.JSONField;

import rx.Observable;

public interface WechatOpenAPI {

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

    public GetOAuthAccessTokenBuilder getOAuthAccessToken(final String authorizerAppid, final String code);
}
