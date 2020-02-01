
package org.jocean.wechat;

import org.jocean.http.MessageBody;
import org.jocean.http.RpcRunner;
import org.jocean.wechat.WXProtocol.UserInfoResponse;
import org.jocean.wechat.WXProtocol.WXAPIResponse;
import org.jocean.wechat.spi.FetchTicketResponse;

import com.alibaba.fastjson.annotation.JSONField;

import rx.Observable;
import rx.Observable.Transformer;

public interface WXCommonAPI {
    public Transformer<RpcRunner, FetchTicketResponse> getJsapiTicket(final String accessToken);

    public Transformer<RpcRunner, UserInfoResponse> getUserInfo(final String accessToken, final String openid);

    public Transformer<RpcRunner, UserInfoResponse> getSnsUserInfo(final String oauth2Token, final String openid);

    public Transformer<RpcRunner, WXAPIResponse> sendCustomMessageInText(final String accessToken,
            final String openid, final String content);

    public Transformer<RpcRunner, WXAPIResponse> sendCustomMessageInImage(final String accessToken,
            final String openid, final String mediaId);

    public Transformer<RpcRunner, String> getShorturl(final String accessToken, final String url);

    public Transformer<RpcRunner, MessageBody> getTempMedia(final String accessToken, final String mediaId);

    public interface UploadTempMediaResponse extends WXAPIResponse {
        @JSONField(name="type")
        public String getType();

        @JSONField(name="type")
        public void setType(final String type);

        @JSONField(name="media_id")
        public String getMediaId();

        @JSONField(name="media_id")
        public void setMediaId(final String mediaId);

        @JSONField(name="created_at")
        public int getCreated();

        @JSONField(name="created_at")
        public void setCreated(final int time);
    }

    public Transformer<RpcRunner, UploadTempMediaResponse> uploadTempMedia(final String accessToken,
            final String name,
            final String filename,
            final Observable<? extends MessageBody> media);

    //  input mediaId (see : https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1444738726&token=&lang=zh_CN)
    //  output MessageBody : media download
    public Transformer<RpcRunner, MessageBody> downloadMedia(final String accessToken, final String mediaId);

    // REF: https://developers.weixin.qq.com/doc/offiaccount/Account_Management/Generating_a_Parametric_QR_Code.html
    /**
     * @param expireSeconds 该二维码有效时间，以秒为单位。 最大不超过2592000（即30天）
     * @param scenestr 场景值ID（字符串形式的ID），字符串类型，长度限制为1到64
     * @return url for download qrcode image
     */
    public Transformer<RpcRunner, String> createVolatileQrcode(final String accessToken, final int expireSeconds, final String scenestr);
}
