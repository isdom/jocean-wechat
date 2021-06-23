package org.jocean.wechat;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.jocean.rpc.annotation.OnResponse;
import org.jocean.rpc.annotation.RpcBuilder;
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

}
