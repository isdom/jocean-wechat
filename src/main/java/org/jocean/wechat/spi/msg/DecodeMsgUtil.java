package org.jocean.wechat.spi.msg;

import org.jocean.http.ContentUtil;
import org.jocean.svr.TradeContext;

import rx.Observable;

public class DecodeMsgUtil {
    public static Observable<BaseWXMessage> decodeWXMessage(final TradeContext tctx) {
        tctx.enableRepeatDecode();
        return tctx.decodeBodyAs(ContentUtil.ASJSON, MsgType.class).flatMap(type -> {
            if (type.getType().equals("event")) {
                return tctx.decodeBodyAs(ContentUtil.ASJSON, EnterSessionEvent.class);
            } else if (type.getType().equals("text")) {
                return tctx.decodeBodyAs(ContentUtil.ASJSON, TextWXMessage.class);
            } else if (type.getType().equals("image")) {
                return tctx.decodeBodyAs(ContentUtil.ASJSON, ImageWXMessage.class);
            } else {
                return Observable.error(new RuntimeException("unknown wx message"));
            }
        });
    }
}
