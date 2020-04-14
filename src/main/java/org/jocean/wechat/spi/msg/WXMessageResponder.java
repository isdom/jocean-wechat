package org.jocean.wechat.spi.msg;

import rx.Observable;

// 该接口的实现类负责根据输入的微信消息，产生回复消息
public interface WXMessageResponder extends WXMessageConsumer {
    public Observable<Object> responseMessage(final BaseWXMessage msg);
}
