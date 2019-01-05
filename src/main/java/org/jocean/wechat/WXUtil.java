package org.jocean.wechat;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.jocean.http.RpcRunner;
import org.jocean.idiom.BeanFinder;
import org.jocean.svr.ResponseBean;
import org.jocean.svr.ResponseUtil;
import org.jocean.wechat.WXOpenAPI.AuthorizerTokenResponse;
import org.jocean.wechat.WXOpenAPI.QueryAuthResponse;
import org.jocean.wechat.WXProtocol.Code2SessionResponse;
import org.jocean.wechat.WXProtocol.OAuthAccessTokenResponse;
import org.jocean.wechat.WXProtocol.UserInfoResponse;
import org.jocean.wechat.WXProtocol.WXAPIResponse;

import rx.Observable;
import rx.Observable.Transformer;

public class WXUtil {
    public static Observable<ResponseBean> redirect4componentoauth(final BeanFinder finder, final String mpappid,
            final String redirect_uri, final String scope, final Observable<String> getstate) {
        // 代公众号发起网页授权
        // https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419318590&token=&lang=zh_CN
        // redirect to wechat auth2.0
        // https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI
        //      &response_type=code&scope=SCOPE&state=STATE&component_appid=component_appid#wechat_redirect
        return finder.find(mpappid, AuthorizedMP.class).flatMap(mp -> getstate.map(state -> {
                try {
                    return ResponseUtil.redirectOnly("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + mp.getAppid()
                            + "&redirect_uri=" + URLEncoder.encode(redirect_uri, "UTF-8")
                            + "&response_type=code"
                            + "&scope=" + scope
                            + "&state=" + URLEncoder.encode(state, "UTF-8")
                            + "&component_appid=" + mp.getComponentAppid()
                            + "#wechat_redirect");
                } catch (final UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }));
    }

    public static Observable<Transformer<RpcRunner, OAuthAccessTokenResponse>> code2tos(
            final BeanFinder finder,
            final String mpappid,
            final String code) {
        return finder.find(mpappid, AuthorizedMP.class).flatMap(mp -> finder.find(mp.getComponentAppid(), WXOpenAPI.class)
                .map(api -> api.getOAuthAccessToken(mp.getAppid(), code)));
    }

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

    private static Observable<Transformer<RpcRunner, UserInfoResponse>> mpuserinfo(final BeanFinder finder,
            final String mpappid,
            final String openid) {
        return Observable.zip(finder.find(mpappid, AuthorizedMP.class), finder.find(WXCommonAPI.class),
                (mp, wcapi) -> wcapi.getUserInfo(mp.getAccessToken(), openid));
    }

    private static Observable<Transformer<RpcRunner, UserInfoResponse>> snsuserinfo(
            final BeanFinder finder,
            final String oauth2Token,
            final String openid) {
        return finder.find(WXCommonAPI.class).map(wcapi -> wcapi.getSnsUserInfo(oauth2Token, openid));
    }

    public static Observable<Transformer<RpcRunner, Code2SessionResponse>> component_code2session(
            final BeanFinder finder,
            final String minaAppid,
            final String code) {
        return finder.find(minaAppid, AuthorizedMP.class).flatMap(mp -> finder.find(mp.getComponentAppid(), WXOpenAPI.class))
                .map(wxopen -> wxopen.code2session(minaAppid, code));
    }

    public static Observable<Transformer<RpcRunner, QueryAuthResponse>> component_queryAuth(
            final BeanFinder finder,
            final String componentAppid,
            final String authorizationCode) {
        return finder.find(componentAppid, WXOpenAPI.class).map(woapi-> woapi.queryAuth(authorizationCode));
    }

    public static Observable<Transformer<RpcRunner, AuthorizerTokenResponse>> component_authorizerToken(
            final BeanFinder finder,
            final String componentAppid,
            final String authorizerAppid,
            final String refreshToken) {
        return finder.find(componentAppid, WXOpenAPI.class).map(woapi-> woapi.authorizerToken(authorizerAppid, refreshToken));
    }


    public static Observable<Transformer<RpcRunner, WXAPIResponse>> common_sendCustomMessageInText(
            final BeanFinder finder,
            final String accessToken,
            final String openid,
            final String content) {
        return finder.find(WXCommonAPI.class).map(wcapi->wcapi.sendCustomMessageInText(accessToken, openid, content));
    }
}
