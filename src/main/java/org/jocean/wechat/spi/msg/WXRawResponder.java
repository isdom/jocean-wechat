package org.jocean.wechat.spi.msg;

import rx.Observable;

// 该接口的实现类负责根据输入的微信消息，产生原始回复消息 (String 形式)
public interface WXRawResponder extends WXMessageConsumer {
    public Observable<String> responseMessage(final BaseWXMessage msg);
}
