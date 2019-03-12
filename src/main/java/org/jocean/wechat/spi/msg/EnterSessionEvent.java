package org.jocean.wechat.spi.msg;

import com.alibaba.fastjson.annotation.JSONField;

public class EnterSessionEvent extends BaseWXMessage {

    @JSONField(name="Event")
    public String getEvent() {
        return _event;
    }

    @JSONField(name="Event")
    public void setEvent(final String event) {
        this._event = event;
    }

    @JSONField(name="SessionFrom")
    public String getSessionFrom() {
        return _sessionFrom;
    }

    @JSONField(name="SessionFrom")
    public void setSessionFrom(final String sessionFrom) {
        this._sessionFrom = sessionFrom;
    }

    private String  _event;
    private String  _sessionFrom;

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("EnterSessionEvent [event=").append(_event).append(", sessionFrom=").append(_sessionFrom)
                .append(", base=").append(super.toString()).append("]");
        return builder.toString();
    }

}
