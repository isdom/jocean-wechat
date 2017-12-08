
package org.jocean.wechat;

import org.jocean.http.MessageUnit;
import org.jocean.idiom.TerminateAware;
import org.jocean.wechat.spi.OAuthAccessTokenResponse;
import org.jocean.wechat.spi.UserInfoResponse;

import rx.Observable;

public interface WechatAPI {
    
    public String getName();
    
    public String getAppid();
    public String getJsapiTicket();
    
    public Observable<UserInfoResponse> getUserInfo(final String accessToken, final String openid);
    
    public Observable<UserInfoResponse> getUserInfo(final String openid);

    public Observable<UserInfoResponse> getSnsapiUserInfo(final String snsapiAccessToken, final String openid);
    
    public Observable<OAuthAccessTokenResponse> getOAuthAccessToken(final String code);
    
    //  input mediaId (see : https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1444738726&token=&lang=zh_CN)
    //  output Blob : media download
    public Observable<MessageUnit> downloadMedia(final TerminateAware<?> terminateAware, final String mediaId);
}
