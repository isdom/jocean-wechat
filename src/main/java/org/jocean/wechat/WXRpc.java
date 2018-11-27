package org.jocean.wechat;

import org.jocean.http.RpcRunner;
import org.jocean.idiom.BeanFinder;
import org.jocean.wechat.WXProtocol.UserInfoResponse;

import rx.Observable;
import rx.Observable.Transformer;

public class WXRpc {
    public static Observable<Transformer<RpcRunner, UserInfoResponse>> tos2userinfo(
            final BeanFinder finder,
            final String scope,
            final String openid,
            final String appid,
            final String oauth_token
            ) {
        if (scope.equals("snsapi_userinfo")) {
            return Observable.zip(mpuserinfo(finder, appid, openid), snsuserinfo(finder, oauth_token, openid),
                    (get_mpuserinfo, get_snsuserinfo) -> {
                        return runners -> Observable.zip(runners.compose(get_mpuserinfo),
                                runners.compose(get_snsuserinfo), (mpuserinfo, snsuserinfo) -> {
                                    snsuserinfo.setSubscribe(mpuserinfo.getSubscribe());
                                    return snsuserinfo;
                                });
                    });
        } else {
            // scope is snsapi_base
            return mpuserinfo(finder, appid, openid);
        }
    }

    private static Observable<Transformer<RpcRunner, UserInfoResponse>> mpuserinfo(final BeanFinder finder,
            final String appid,
            final String openid) {
        return Observable.zip(finder.find(appid, AuthorizedMP.class), finder.find(WXCommonAPI.class),
                (mp, wcapi) -> wcapi.getUserInfo(mp.getAccessToken(), openid));
    }

    private static Observable<Transformer<RpcRunner, UserInfoResponse>> snsuserinfo(
            final BeanFinder finder,
            final String oauth2Token,
            final String openid) {
        return finder.find(WXCommonAPI.class).map(wcapi -> wcapi.getSnsUserInfo(oauth2Token, openid));
    }

}
