
package org.jocean.wechat;

import org.jocean.http.Interact;

import com.alibaba.fastjson.annotation.JSONField;

import rx.Observable;
import rx.functions.Func1;

public interface WXOpenAPI {

    public String getName();

    public String getAppid();

    public String getComponentToken();

    public interface WXOpenResponse {
        @JSONField(name="errcode")
        public String getErrcode();

        @JSONField(name="errcode")
        public void setErrcode(final String errcode);

        @JSONField(name="errmsg")
        public String getErrmsg();

        @JSONField(name="errmsg")
        public void setErrmsg(final String errmsg);
    }

    public interface PreAuthCodeResponse extends WXOpenResponse {
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
        public void setAuthorizerAppid(final String appid);

        @JSONField(name = "authorizer_access_token")
        public String getAuthorizerAccessToken();

        @JSONField(name = "authorizer_access_token")
        public void setAuthorizerAccessToken(final String token);

        @JSONField(name = "authorizer_refresh_token")
        public String getAuthorizerRefreshToken();

        @JSONField(name = "authorizer_refresh_token")
        public void setAuthorizerRefreshToken(final String refreshToken);

        @JSONField(name = "expires_in")
        public int getExpires();

        @JSONField(name = "expires_in")
        public void setExpires(final int expires);

        @JSONField(name = "func_info")
        public FuncInfo[] getFuncInfos();

        @JSONField(name = "func_info")
        public void setFuncInfos(final FuncInfo[] infos);
    }

    public interface QueryAuthResponse extends WXOpenResponse {

        @JSONField(name = "authorization_info")
        public AuthorizationInfo getAuthorizationInfo();

        @JSONField(name = "authorization_info")
        public void setAuthorizationInfo(final AuthorizationInfo info);
    }

    public Func1<Interact, Observable<QueryAuthResponse>> queryAuth(final String authorizationCode);

    public interface AuthorizerTokenResponse extends WXOpenResponse {
        @JSONField(name = "authorizer_access_token")
        public String getAuthorizerAccessToken();

        @JSONField(name = "authorizer_access_token")
        public void setAuthorizerAccessToken(final String token);

        @JSONField(name = "authorizer_refresh_token")
        public String getAuthorizerRefreshToken();

        @JSONField(name = "authorizer_refresh_token")
        public void setAuthorizerRefreshToken(final String refreshToken);

        @JSONField(name = "expires_in")
        public int getExpires();

        @JSONField(name = "expires_in")
        public void setExpires(final int expires);
    }

    public Func1<Interact, Observable<AuthorizerTokenResponse>> authorizerToken(final String authorizerAppid, final String refreshToken);

    interface HasId {
        @JSONField(name = "id")
        public int getId();

        @JSONField(name = "id")
        public void setId(final int id);
    }

    interface BusinessInfo {
        @JSONField(name = "open_store")
        public int getOpenStore();

        @JSONField(name = "open_store")
        public void setOpenStore(final int valid);

        @JSONField(name = "open_scan")
        public int getOpenScan();

        @JSONField(name = "open_scan")
        public void setOpenScan(final int valid);

        @JSONField(name = "open_pay")
        public int getOpenPay();

        @JSONField(name = "open_pay")
        public void setOpenPay(final int valid);

        @JSONField(name = "open_card")
        public int getOpenCard();

        @JSONField(name = "open_card")
        public void setOpenCard(final int valid);

        @JSONField(name = "open_shake")
        public int getOpenShake();

        @JSONField(name = "open_shake")
        public void setOpenShake(final int valid);
    }

    interface AuthorizerInfo {
        @JSONField(name = "nick_name")
        public String getNickName();

        @JSONField(name = "nick_name")
        public void setNickName(final String nickName);

        @JSONField(name = "head_img")
        public String getHeadImg();

        @JSONField(name = "head_img")
        public void setHeadImg(final String img);

        @JSONField(name = "service_type_info")
        public HasId getServiceTypeInfo();

        @JSONField(name = "service_type_info")
        public void setServiceTypeInfo(final HasId info);

        @JSONField(name = "verify_type_info")
        public HasId getVerifyTypeInfo();

        @JSONField(name = "verify_type_info")
        public void setVerifyTypeInfo(final HasId info);

        @JSONField(name = "user_name")
        public String getUserName();

        @JSONField(name = "user_name")
        public void setUserName(final String name);

        @JSONField(name = "principal_name")
        public String getPrincipalName();

        @JSONField(name = "principal_name")
        public void setPrincipalName(final String name);

        @JSONField(name = "business_info")
        public BusinessInfo getBusinessInfo();

        @JSONField(name = "business_info")
        public void setBusinessInfo(final BusinessInfo info);

        @JSONField(name = "alias")
        public String getAlias();

        @JSONField(name = "alias")
        public void setAlias(final String alias);

        @JSONField(name = "qrcode_url")
        public String getQrcodeUrl();

        @JSONField(name = "qrcode_url")
        public void setQrcodeUrl(final String url);
    }

    interface SimpleAuthorizationInfo {
        @JSONField(name = "authorization_appid")
        public String getAuthorizationAppid();

        @JSONField(name = "authorization_appid")
        public void setAuthorizationAppid(final String appid);

        @JSONField(name = "func_info")
        public FuncInfo[] getFuncInfos();

        @JSONField(name = "func_info")
        public void setFuncInfos(final FuncInfo[] infos);
    }

    public interface AuthorizerInfoResponse extends WXOpenResponse {
        @JSONField(name = "authorizer_info")
        public AuthorizerInfo getAuthorizerInfo();

        @JSONField(name = "authorizer_info")
        public void setAuthorizerInfo(final AuthorizerInfo info);

        @JSONField(name = "authorization_info")
        public SimpleAuthorizationInfo getAuthorizationInfo();

        @JSONField(name = "authorization_info")
        public void setAuthorizationInfo(final SimpleAuthorizationInfo info);
    }

    public Func1<Interact, Observable<AuthorizerInfoResponse>> getAuthorizerInfo(final String authorizerAppid);

}
