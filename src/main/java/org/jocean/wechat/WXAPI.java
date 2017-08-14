
package org.jocean.wechat;

import org.jocean.wechat.spi.UserInfoResponse;

import rx.Observable;

public interface WXAPI {
    public Observable<UserInfoResponse> getUserInfo(final String accessToken, final String openid);
    
    public Observable<UserInfoResponse> getUserInfo(final String openid);
}
