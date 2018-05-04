
package org.jocean.wechat;

import org.jocean.http.Interact;

import com.alibaba.fastjson.annotation.JSONField;

import rx.Observable;
import rx.functions.Func1;

public interface WXOpenAPI {

    public String getName();

    public String getAppid();

    public String getComponentToken();

    public interface PreAuthCodeResponse {
        @JSONField(name="errcode")
        public String getErrcode();

        @JSONField(name="errcode")
        public void setErrcode(final String errcode);

        @JSONField(name="errmsg")
        public String getErrmsg();

        @JSONField(name="errmsg")
        public void setErrmsg(final String errmsg);

        @JSONField(name = "pre_auth_code")
        public String getPreAuthCode();

        @JSONField(name = "pre_auth_code")
        public void setPreAuthCode(final String code);

        @JSONField(name = "expires_in")
        public int getExpires();

        @JSONField(name = "expires_in")
        public void setExpires(final int expires);
    }

    public Func1<Interact, Observable<PreAuthCodeResponse>> getPreAuthCode();
}
