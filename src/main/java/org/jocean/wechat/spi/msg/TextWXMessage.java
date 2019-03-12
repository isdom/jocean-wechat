package org.jocean.wechat.spi.msg;

import com.alibaba.fastjson.annotation.JSONField;

public class TextWXMessage extends BaseWXMessage {

    @JSONField(name="Content")
    public String getContent() {
        return _content;
    }

    @JSONField(name="Content")
    public void setContent(final String content) {
        this._content = content;
    }

    private String  _content;

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("TextWXMessage [content=").append(_content).append(", base=").append(super.toString())
                .append("]");
        return builder.toString();
    }
}
