
package org.jocean.wechat;

import org.jocean.wechat.spi.UserInfoResponse;

import rx.Observable;

public interface WechatAPI {
    
    public String getName();
    
    public String getAppid();
    public String getJsapiTicket();
    
    public Observable<UserInfoResponse> getUserInfo(final String accessToken, final String openid);
    
    public Observable<UserInfoResponse> getUserInfo(final String openid);

    public Observable<UserInfoResponse> getSnsapiUserInfo(final String snsapiAccessToken, final String openid);
}
