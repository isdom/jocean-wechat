package org.jocean.wechat.spi.msg;

import org.jocean.http.RpcRunner;

import rx.Observable.Transformer;

public interface WXMessageConsumer {
    public Transformer<RpcRunner, String> consumeMessage(final BaseWXMessage msg);
}
