package org.jocean.wechat.spi.msg;

import rx.Observable;

// 该接口的实现类根据输入的微信消息，产生一定动作，但并不回复消息给微信网关
// 回复消息由微信接入服务自行决定，一般为 200 OK
public interface WXMessageNotifier extends WXMessageConsumer {
    public Observable<String> notifyMessage(final BaseWXMessage msg);
}
