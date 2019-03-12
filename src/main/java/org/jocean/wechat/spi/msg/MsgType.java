package org.jocean.wechat.spi.msg;

import com.alibaba.fastjson.annotation.JSONField;

public interface MsgType {

    @JSONField(name="MsgType")
    public String getType();

    @JSONField(name="MsgType")
    public void setType(final String type);
}
