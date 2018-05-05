
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

    public Func1<Interact, Observable<PreAuthCodeResponse>> createPreAuthCode();

    interface FuncscopeCategory {
        @JSONField(name = "id")
        public int getId();
        @JSONField(name = "id")
        public void setId(final int id);
    }

    interface FuncInfo {
        @JSONField(name = "funcscope_category")
        public FuncscopeCategory getCategory();

        @JSONField(name = "funcscope_category")
        public void setCategory(final FuncscopeCategory category);
    }

    interface AuthorizationInfo {
        @JSONField(name = "authorizer_appid")
        public String getAuthorizerAppid();

        @JSONField(name = "authorizer_appid")
        public String setAuthorizerAppid(final String appid);

        @JSONField(name = "authorizer_access_token")
        public String getAuthorizerAccessToken();

        @JSONField(name = "authorizer_access_token")
        public String setAuthorizerAccessToken(final String token);

        @JSONField(name = "authorizer_refresh_token")
        public String getAuthorizerRefreshToken();

        @JSONField(name = "authorizer_refresh_token")
        public String setAuthorizerRefreshToken(final String refreshToken);

        @JSONField(name = "expires_in")
        public int getExpires();

        @JSONField(name = "expires_in")
        public void setExpires(final int expires);

        @JSONField(name = "func_info")
        public FuncInfo[] getFuncInfos();

        @JSONField(name = "func_info")
        public void setFuncInfos(final FuncInfo[] infos);
    }

    public interface QueryAuthResponse {

        @JSONField(name = "authorization_info")
        public AuthorizationInfo getAuthorizationInfo();

        @JSONField(name = "authorization_info")
        public void setAuthorizationInfo(final AuthorizationInfo info);
    }

    public Func1<Interact, Observable<QueryAuthResponse>> queryAuth(final String authorizationCode);
}
