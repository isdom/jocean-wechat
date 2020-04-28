package org.jocean.wechat;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jocean.rpc.annotation.ConstParams;
import org.jocean.wechat.WXProtocol.UserInfoResponse;

import rx.Observable;

public interface WechatMPAPI {
    interface GetUserInfoBuilder {
        @QueryParam("access_token")
        GetUserInfoBuilder accessToken(final String accessToken);

        @QueryParam("openid")
        GetUserInfoBuilder openid(final String openid);

        @GET
        @Path("https://api.weixin.qq.com/cgi-bin/user/info")
        @ConstParams({"lang", "zh_CN"})
        @Consumes(MediaType.APPLICATION_JSON)
        Observable<UserInfoResponse> call();
    }

    public GetUserInfoBuilder getUserInfo();

    interface GetSnsUserInfoBuilder {
        @QueryParam("access_token")
        GetSnsUserInfoBuilder accessToken(final String accessToken);

        @QueryParam("openid")
        GetSnsUserInfoBuilder openid(final String openid);

        @GET
        @Path("https://api.weixin.qq.com/sns/userinfo")
        @ConstParams({"lang", "zh_CN"})
        @Consumes(MediaType.APPLICATION_JSON)
        Observable<UserInfoResponse> call();
    }

    public GetSnsUserInfoBuilder getSnsUserInfo();
}
