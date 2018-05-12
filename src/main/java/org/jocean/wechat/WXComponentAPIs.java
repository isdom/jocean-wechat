
package org.jocean.wechat;

import org.jocean.http.Feature;
import org.jocean.http.Interact;
import org.jocean.http.MessageUtil;
import org.jocean.wechat.WXProtocol.OAuthAccessTokenResponse;
import org.jocean.wechat.WXProtocol.UserInfoResponse;

import rx.Observable;
import rx.functions.Func1;

public class WXComponentAPIs {
    private WXComponentAPIs() {
        throw new IllegalStateException("No instances!");
    }


    public static Func1<Interact, Observable<OAuthAccessTokenResponse>> getOAuthAccessToken(
            final String appid,
            final String componentAppid,
            final String componentAccessToken,
            final String code) {
        return interact->interact
                .uri("https://api.weixin.qq.com")
                .path("/sns/oauth2/component/access_token")
                .paramAsQuery("appid", appid)
                .paramAsQuery("code", code)
                .paramAsQuery("grant_type", "authorization_code")
                .paramAsQuery("component_appid", componentAppid)
                .paramAsQuery("component_access_token", componentAccessToken)
                .feature(Feature.ENABLE_LOGGING_OVER_SSL).execution()
                .compose(MessageUtil.responseAs(OAuthAccessTokenResponse.class, MessageUtil::unserializeAsJson));
    }

    public static Func1<Interact, Observable<UserInfoResponse>> getSnsapiUserInfo(
            final String snsapiToken,
            final String openid) {
        return interact->interact
                .feature(Feature.ENABLE_LOGGING_OVER_SSL)
                .uri("https://api.weixin.qq.com").path("/sns/userinfo")
                .paramAsQuery("access_token", snsapiToken)
                .paramAsQuery("openid", openid)
                .paramAsQuery("lang", "zh_CN")
                .execution()
                .compose(MessageUtil.responseAs(UserInfoResponse.class, MessageUtil::unserializeAsJson));
    }
}
