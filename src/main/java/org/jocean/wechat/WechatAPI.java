
package org.jocean.wechat;

import org.jocean.http.Interact;
import org.jocean.http.MessageBody;
import org.jocean.idiom.Terminable;
import org.jocean.wechat.spi.OAuthAccessTokenResponse;
import org.jocean.wechat.spi.UserInfoResponse;

import rx.Observable;

public interface WechatAPI {
    
    public String getName();
    
    public String getAppid();
    public String getJsapiTicket();
    
    public String getAccessToken();
    
    public Observable<UserInfoResponse> getUserInfo(final String openid);

    public Observable<UserInfoResponse> getSnsapiUserInfo(final String snsapiAccessToken, final String openid);
    
    public Observable<OAuthAccessTokenResponse> getOAuthAccessToken(final String code);
    
    public Observable<UserInfoResponse> getUserInfo(final Interact interact, final String openid);

    public Observable<UserInfoResponse> getSnsapiUserInfo(final Interact interact, final String snsapiAccessToken, final String openid);
    
    public Observable<OAuthAccessTokenResponse> getOAuthAccessToken(final Interact interact, final String code);
    
    //  input mediaId (see : https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1444738726&token=&lang=zh_CN)
    //  output Blob : media download
    public Observable<MessageBody> downloadMedia(final Terminable terminable, final String mediaId);
}
