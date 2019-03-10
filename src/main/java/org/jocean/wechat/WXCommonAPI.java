
package org.jocean.wechat;

import org.jocean.http.MessageBody;
import org.jocean.http.RpcRunner;
import org.jocean.wechat.WXProtocol.UserInfoResponse;
import org.jocean.wechat.WXProtocol.WXAPIResponse;

import rx.Observable.Transformer;

public interface WXCommonAPI {
    public Transformer<RpcRunner, UserInfoResponse> getUserInfo(final String accessToken, final String openid);

    public Transformer<RpcRunner, UserInfoResponse> getSnsUserInfo(final String oauth2Token, final String openid);

    public Transformer<RpcRunner, WXAPIResponse> sendCustomMessageInText(final String accessToken,
            final String openid, final String content);

    public Transformer<RpcRunner, String> getShorturl(final String accessToken, final String url);

    public Transformer<RpcRunner, MessageBody> getTempMedia(final String accessToken, final String mediaId);
}
