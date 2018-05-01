
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

        @JSONField(name="errmsg")
        public String getErrmsg();

        @JSONField(name = "pre_auth_code")
        public String getPreAuthCode();

        @JSONField(name = "expires_in")
        public int getExpires();
    }

    public Func1<Interact, Observable<PreAuthCodeResponse>> getPreAuthCode();
}
