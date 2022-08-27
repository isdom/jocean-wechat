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

// ref: https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay-1.shtml
//      https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay2_0.shtml

public interface WechatPayAPIV3 {
	
	// https://pay.weixin.qq.com/wiki/doc/apiv3/open/pay/chapter4_3_3.shtml
	// 商家转账到零钱开发指引
	// https://api.mch.weixin.qq.com/v3/transfer/batches
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


}
