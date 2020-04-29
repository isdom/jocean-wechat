package org.jocean.wechat;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jocean.rpc.annotation.ConstParams;
import org.jocean.wechat.WXProtocol.UserInfoResponse;
import org.jocean.wechat.spi.FetchTicketResponse;

import rx.Observable;

public interface WechatMPAPI {
    interface NeedAccessToken<BUILDER> {
        @QueryParam("access_token")
        BUILDER accessToken(final String accessToken);
    }

    interface GetUserInfoBuilder extends NeedAccessToken<GetUserInfoBuilder> {

        @QueryParam("openid")
        GetUserInfoBuilder openid(final String openid);

        @GET
        @Path("https://api.weixin.qq.com/cgi-bin/user/info")
        @ConstParams({"lang", "zh_CN"})
        @Consumes(MediaType.APPLICATION_JSON)
        Observable<UserInfoResponse> call();
    }

    public GetUserInfoBuilder getUserInfo();

    interface GetSnsUserInfoBuilder extends NeedAccessToken<GetSnsUserInfoBuilder> {

        @QueryParam("openid")
        GetSnsUserInfoBuilder openid(final String openid);

        @GET
        @Path("https://api.weixin.qq.com/sns/userinfo")
        @ConstParams({"lang", "zh_CN"})
        @Consumes(MediaType.APPLICATION_JSON)
        Observable<UserInfoResponse> call();
    }

    public GetSnsUserInfoBuilder getSnsUserInfo();

    interface GetJsapiTicketBuilder extends NeedAccessToken<GetJsapiTicketBuilder> {

        @GET
        @Path("https://api.weixin.qq.com/cgi-bin/ticket/getticket")
        @ConstParams({"type", "jsapi"})
        @Consumes(MediaType.APPLICATION_JSON)
        Observable<FetchTicketResponse> call();
    }

    public GetJsapiTicketBuilder getJsapiTicket();
}
