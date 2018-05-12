
package org.jocean.wechat;

import org.jocean.http.Interact;
import org.jocean.http.MessageBody;
import org.jocean.wechat.WXProtocol.OAuthAccessTokenResponse;
import org.jocean.wechat.WXProtocol.UserInfoResponse;

import rx.Observable;
import rx.functions.Func1;

public interface WechatAPI {

    public String getName();

    public String getAppid();
    public String getJsapiTicket();

    public String getAccessToken();

    public Func1<Interact, Observable<UserInfoResponse>> getUserInfo(final String openid);

    public Func1<Interact, Observable<UserInfoResponse>> getSnsapiUserInfo(final String snsapiAccessToken, final String openid);

    public Func1<Interact, Observable<OAuthAccessTokenResponse>> getOAuthAccessToken(final String code);

    /**
     * @param interact
     * @param expireSeconds 该二维码有效时间，以秒为单位。 最大不超过2592000（即30天）
     * @param scenestr 场景值ID（字符串形式的ID），字符串类型，长度限制为1到64
     * @return url for download qrcode image
     */
    public Func1<Interact, Observable<String>> createVolatileQrcode(final int expireSeconds, final String scenestr);

    //  input mediaId (see : https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1444738726&token=&lang=zh_CN)
    //  output Blob : media download
    public Func1<Interact, Observable<MessageBody>> downloadMedia(final String mediaId);
}
