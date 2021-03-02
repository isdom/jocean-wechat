package org.jocean.wechat.spi.msg;

import org.jocean.http.ContentDecoder;
import org.jocean.http.ContentUtil;
import org.jocean.http.FullMessage;
import org.jocean.http.MessageUtil;

import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpRequest;
import rx.Observable;
import rx.functions.Actions;

public class DecodeMsgUtil {
    public static Observable<BaseWXMessage> decodeWXMessage(final Observable<FullMessage<HttpRequest>> inbound) {
        final Observable<FullMessage<HttpRequest>> cached = inbound.compose(MessageUtil.cacheFullMessage());
        return decodeBodyAs(cached, ContentUtil.ASJSON, MsgType.class).flatMap(type -> {
            if (type.getType().equals("event")) {
                return decodeBodyAs(cached, ContentUtil.ASJSON, EnterSessionEvent.class);
            } else if (type.getType().equals("text")) {
                return decodeBodyAs(cached, ContentUtil.ASJSON, TextWXMessage.class);
            } else if (type.getType().equals("image")) {
                return decodeBodyAs(cached, ContentUtil.ASJSON, ImageWXMessage.class);
            } else {
                return Observable.error(new RuntimeException("unknown wx message"));
            }
        });
    }

    private static <T, M extends HttpMessage> Observable<T> decodeBodyAs(
            final Observable<FullMessage<M>> fhms,
            final ContentDecoder decoder,
            final Class<T> type) {
        return fhms.flatMap(MessageUtil.fullmsg2body()).compose(MessageUtil.body2bean(decoder, type, Actions.empty()));
    }
}
