package org.jocean.wechat;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.jocean.http.RpcRunner;
import org.jocean.idiom.BeanFinder;
import org.jocean.svr.ResponseBean;
import org.jocean.svr.ResponseUtil;
import org.jocean.wechat.WXProtocol.UserInfoResponse;
import org.jocean.wechat.WXProtocol.WXAPIResponse;

import rx.Observable;
import rx.Observable.Transformer;

public class WXUtil {
    private WXUtil() {
    }

    public static Observable<ResponseBean> toMPAuthorize(
            final AuthorizedMP mp,
            final String redirect_uri,
            final String scope,
            final Observable<String> getstate) {
        // 代公众号发起网页授权
        // https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419318590&token=&lang=zh_CN
        // redirect to wechat auth2.0
        // https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI
        //      &response_type=code&scope=SCOPE&state=STATE&component_appid=component_appid#wechat_redirect
        return getstate.map(state -> {
                try {
                    String location = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + mp.getAppid()
                            + "&redirect_uri=" + URLEncoder.encode(redirect_uri, "UTF-8")
                            + "&response_type=code"
                            + "&scope=" + scope
                            + "&state=" + URLEncoder.encode(state, "UTF-8");
                    if (null != mp.getComponentAppid()) {
                        location += "&component_appid=" + mp.getComponentAppid();
                    }
                    location += "#wechat_redirect";
                    return ResponseUtil.redirectOnly(location);
                } catch (final UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            });
    }

//    public static Observable<Transformer<RpcRunner, OAuthAccessTokenResponse>> code2tos(
//            final BeanFinder finder,
//            final String mpappid,
//            final String code) {
//        return finder.find(mpappid, AuthorizedMP.class).flatMap(mp -> finder.find(mp.getComponentAppid(), OldWXOpenAPI.class)
//                .map(api -> api.getOAuthAccessToken(mp.getAppid(), code)));
//    }

    public static Observable<Transformer<RpcRunner, UserInfoResponse>> tos2userinfo(
            final BeanFinder finder,
            final String scope,
            final String openid,
            final String mpappid,
            final String oauth_token
            ) {
        if (scope.equals("snsapi_userinfo")) {
            return Observable.zip(mpuserinfo(finder, mpappid, openid), snsuserinfo(finder, oauth_token, openid),
                    (get_mpuserinfo, get_snsuserinfo) -> {
                        return runners -> Observable.zip(runners.compose(get_mpuserinfo),
                                runners.compose(get_snsuserinfo), (mpuserinfo, snsuserinfo) -> {
                                    snsuserinfo.setSubscribe(mpuserinfo.getSubscribe());
                                    return snsuserinfo;
                                });
                    });
        } else {
            // scope is snsapi_base
            return mpuserinfo(finder, mpappid, openid);
        }
    }

    public static Observable<Transformer<RpcRunner, UserInfoResponse>> mpuserinfo(final BeanFinder finder,
            final String mpappid,
            final String openid) {
        return Observable.zip(finder.find(mpappid, AuthorizedMP.class), finder.find(WXCommonAPI.class),
                (mp, wcapi) -> wcapi.getUserInfo(mp.getAccessToken(), openid));
    }

    public static Observable<Transformer<RpcRunner, UserInfoResponse>> snsuserinfo(
            final BeanFinder finder,
            final String oauth2Token,
            final String openid) {
        return finder.find(WXCommonAPI.class).map(wcapi -> wcapi.getSnsUserInfo(oauth2Token, openid));
    }

    public static Observable<Transformer<RpcRunner, WXAPIResponse>> common_sendCustomMessageInText(
            final BeanFinder finder,
            final String accessToken,
            final String openid,
            final String content) {
        return finder.find(WXCommonAPI.class).map(wcapi->wcapi.sendCustomMessageInText(accessToken, openid, content));
    }
}
