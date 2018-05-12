
package org.jocean.wechat;

import org.jocean.http.Interact;
import org.jocean.wechat.WXProtocol.UserInfoResponse;

import rx.Observable;
import rx.functions.Func1;

public interface WXCommonAPI {
    public Func1<Interact, Observable<UserInfoResponse>> getUserInfo(final String accessToken, final String openid);

    public Func1<Interact, Observable<UserInfoResponse>> getSnsUserInfo(final String oauth2Token, final String openid);
}
