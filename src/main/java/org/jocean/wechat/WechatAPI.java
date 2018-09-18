
package org.jocean.wechat;

import org.jocean.http.MessageBody;
import org.jocean.http.RpcRunner;
import org.jocean.wechat.WXProtocol.OAuthAccessTokenResponse;

import rx.Observable.Transformer;

public interface WechatAPI {

    public String getName();

    public String getAppid();
    public String getJsapiTicket();

    public String getAccessToken();

    public Transformer<RpcRunner, OAuthAccessTokenResponse> getOAuthAccessToken(final String code);

    /**
     * @param interact
     * @param expireSeconds 该二维码有效时间，以秒为单位。 最大不超过2592000（即30天）
     * @param scenestr 场景值ID（字符串形式的ID），字符串类型，长度限制为1到64
     * @return url for download qrcode image
     */
    public Transformer<RpcRunner, String> createVolatileQrcode(final int expireSeconds, final String scenestr);

    //  input mediaId (see : https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1444738726&token=&lang=zh_CN)
    //  output MessageBody : media download
    public Transformer<RpcRunner, MessageBody> downloadMedia(final String mediaId);
}
