
package org.jocean.wechat;

import org.jocean.http.MessageBody;
import org.jocean.http.RpcRunner;
import org.jocean.wechat.WXProtocol.UserInfoResponse;
import org.jocean.wechat.WXProtocol.WXAPIResponse;

import com.alibaba.fastjson.annotation.JSONField;

import rx.Observable;
import rx.Observable.Transformer;

public interface WXCommonAPI {
    public Transformer<RpcRunner, UserInfoResponse> getUserInfo(final String accessToken, final String openid);

    public Transformer<RpcRunner, UserInfoResponse> getSnsUserInfo(final String oauth2Token, final String openid);

    public Transformer<RpcRunner, WXAPIResponse> sendCustomMessageInText(final String accessToken,
            final String openid, final String content);

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

    public Transformer<RpcRunner, UploadTempMediaResponse> uploadTempMedia(final String accessToken, final String filename, final Observable<? extends MessageBody> media);
}
