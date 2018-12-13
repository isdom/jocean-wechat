
package org.jocean.wechat;

import org.jocean.http.RpcRunner;
import org.jocean.wechat.WXProtocol.Code2SessionResponse;

import rx.Observable.Transformer;

public interface WechatMinaAPI {

    public String getName();

    public String getAppid();

    // https://developers.weixin.qq.com/miniprogram/dev/api/code2Session.html
    public Transformer<RpcRunner, Code2SessionResponse> code2session(final String code);
}
