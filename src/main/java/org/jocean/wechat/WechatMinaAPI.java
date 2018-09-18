
package org.jocean.wechat;

import org.jocean.http.RpcRunner;
import org.jocean.wechat.WXProtocol.WXAPIResponse;

import com.alibaba.fastjson.annotation.JSONField;

import rx.Observable.Transformer;

public interface WechatMinaAPI {

    public String getName();

    public String getAppid();

    public interface Code2SessionResponse extends WXAPIResponse {
        @JSONField(name="openid")
        public String getOpenid();

        @JSONField(name="openid")
        public void setOpenid(final String openid);

        @JSONField(name="session_key")
        public String getSessionkey();

        @JSONField(name="session_key")
        public void setSessionkey(String session_key);


        @JSONField(name="unionid")
        public String getUnionid();

        @JSONField(name="unionid")
        public void setUnionid(final String unionid);
    }

    public Transformer<RpcRunner, Code2SessionResponse> code2session(final String code);
}
