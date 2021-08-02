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
    /**
     * 触发云函数。注意：HTTP API 途径触发云函数不包含用户信息。
     * POST https://api.weixin.qq.com/tcb/invokecloudfunction?access_token=ACCESS_TOKEN&env=ENV&name=FUNCTION_NAME
     * 请求参数
        属性  类型  默认值 必填  说明
        access_token    string      是   接口调用凭证
        env string      是   云开发环境ID
        name    string      是   云函数名称
        POSTBODY    string      是   云函数的传入参数，具体结构由开发者定义。
        返回值
        Object
        返回的 JSON 数据包

        属性  类型  说明
        errcode number  错误码
        errmsg  string  错误信息
        resp_data   string  云函数返回的buffer
        errcode 的合法值

        值   说明  最低版本
        0   请求成功
        -1  系统错误
        -1000   系统错误
        40014   AccessToken 不合法
        40101   缺少必填参数
        41001   缺少AccessToken
        42001   AccessToken过期
        43002   HTTP METHOD 错误
        44002   POST BODY 为空
        85088   该APP未开通云开发
        其他错误码   云开发错误码
     * @author isdom
     *
     */
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
