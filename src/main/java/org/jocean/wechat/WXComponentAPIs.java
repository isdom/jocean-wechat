
package org.jocean.wechat;

import org.jocean.http.Feature;
import org.jocean.http.MessageUtil;
import org.jocean.http.RpcRunner;
import org.jocean.wechat.WXProtocol.OAuthAccessTokenResponse;

import rx.Observable.Transformer;

public class WXComponentAPIs {
    private WXComponentAPIs() {
        throw new IllegalStateException("No instances!");
    }


    public static Transformer<RpcRunner, OAuthAccessTokenResponse> getOAuthAccessToken(
            final String appid,
            final String componentAppid,
            final String componentAccessToken,
            final String code) {
        return rpcs -> rpcs.flatMap( rpc -> rpc.execute(
        interact-> interact
                .uri("https://api.weixin.qq.com")
                .path("/sns/oauth2/component/access_token")
                .paramAsQuery("appid", appid)
                .paramAsQuery("code", code)
                .paramAsQuery("grant_type", "authorization_code")
                .paramAsQuery("component_appid", componentAppid)
                .paramAsQuery("component_access_token", componentAccessToken)
                .feature(Feature.ENABLE_LOGGING_OVER_SSL).execution()
                .compose(MessageUtil.responseAs(OAuthAccessTokenResponse.class, MessageUtil::unserializeAsJson))
            ));
    }
}
