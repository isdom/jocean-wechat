package org.jocean.wechat;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jocean.rpc.annotation.OnResponse;
import org.jocean.rpc.annotation.RpcBuilder;
import org.jocean.wechat.WXProtocol.WXAPIResponse;

import rx.Observable;

public interface TcbAPI {
    interface NeedAccessToken<BUILDER extends NeedAccessToken<?>> {
        @QueryParam("access_token")
        BUILDER accessToken(final String accessToken);
    }

    // https://developers.weixin.qq.com/miniprogram/dev/wxcloud/reference-http-api/functions/invokeCloudFunction.html
    @RpcBuilder
    interface InvokeCloudFunctionBuilder extends NeedAccessToken<InvokeCloudFunctionBuilder> {

        @QueryParam("env")
        InvokeCloudFunctionBuilder env(final String env);

        @QueryParam("name")
        InvokeCloudFunctionBuilder name(final String name);

        @Produces(MediaType.APPLICATION_JSON)
        InvokeCloudFunctionBuilder postbody(final Object body);

        @POST
        @Path("https://api.weixin.qq.com/tcb/invokecloudfunction")
        @Consumes(MediaType.APPLICATION_JSON)
        @OnResponse("org.jocean.wechat.WXProtocol.CHECK_WXRESP")
        Observable<WXAPIResponse> call();
    }

    public InvokeCloudFunctionBuilder invokeCloudFunction();
}
