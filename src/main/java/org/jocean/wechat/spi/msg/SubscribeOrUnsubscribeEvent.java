package org.jocean.wechat.spi.msg;

import com.alibaba.fastjson.annotation.JSONField;

// https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_event_pushes.html
public class SubscribeOrUnsubscribeEvent extends BaseWXMessage {

    @JSONField(name="Event")
    public String getEvent() {
        return _event;
    }

    @JSONField(name="Event")
    public void setEvent(final String event) {
        this._event = event;
    }

    private String  _event;

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SubscribeOrUnsubscribeEvent [event=").append(_event)
                .append(", base=").append(super.toString()).append("]");
        return builder.toString();
    }
}
