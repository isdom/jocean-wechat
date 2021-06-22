package org.jocean.wechat;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jocean.rpc.annotation.OnResponse;
import org.jocean.rpc.annotation.RpcBuilder;
import org.jocean.wechat.spi.FetchComponentTokenRequest;
import org.jocean.wechat.spi.FetchComponentTokenResponse;

import rx.Observable;

public interface WechatOpenAPI {
    @RpcBuilder
    interface FetchComponentTokenBuilder {
        @Produces(MediaType.APPLICATION_JSON)
        FetchComponentTokenBuilder body(final FetchComponentTokenRequest req);

        @POST
        @Path("https://api.weixin.qq.com/cgi-bin/component/api_component_token")
        @Consumes(MediaType.APPLICATION_JSON)
        @OnResponse("org.jocean.wechat.WXProtocol.CHECK_WXRESP")
        Observable<FetchComponentTokenResponse> call();
    }

    public FetchComponentTokenBuilder fetchComponentToken();

}
