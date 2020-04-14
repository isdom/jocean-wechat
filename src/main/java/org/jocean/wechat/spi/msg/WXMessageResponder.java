package org.jocean.wechat.spi.msg;

import org.jocean.wechat.spi.ToWXMessage;

import rx.Observable;

// 该接口的实现类负责根据输入的微信消息，产生回复消息
public interface WXMessageResponder extends WXMessageConsumer {
    public Observable<ToWXMessage> responseMessage(final BaseWXMessage msg);
}
