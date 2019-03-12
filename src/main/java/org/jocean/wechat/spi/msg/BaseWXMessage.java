package org.jocean.wechat.spi.msg;

import com.alibaba.fastjson.annotation.JSONField;

public class BaseWXMessage {

    @JSONField(name="ToUserName")
    public String getToUserName() {
        return _toUserName;
    }

    @JSONField(name="ToUserName")
    public void setToUserName(final String toUserName) {
        this._toUserName = toUserName;
    }

    @JSONField(name="FromUserName")
    public String getFromUserName() {
        return _fromUserName;
    }

    @JSONField(name="FromUserName")
    public void setFromUserName(final String fromUserName) {
        this._fromUserName = fromUserName;
    }

    @JSONField(name="CreateTime")
    public long getCreateTime() {
        return _createTime;
    }

    @JSONField(name="CreateTime")
    public void setCreateTime(final long createTime) {
        this._createTime = createTime;
    }

    @JSONField(name="MsgType")
    public String getMsgType() {
        return _msgType;
    }

    @JSONField(name="MsgType")
    public void setMsgType(final String msgType) {
        this._msgType = msgType;
    }

    @JSONField(name="MsgId")
    public String getMsgId() {
        return _msgId;
    }

    @JSONField(name="MsgId")
    public void setMsgId(final String msgId) {
        this._msgId = msgId;
    }

    private String  _toUserName;
    private String  _fromUserName;
    private long    _createTime;
    private String  _msgType;
    private String  _msgId;

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("[toUserName=").append(_toUserName).append(", fromUserName=")
                .append(_fromUserName).append(", createTime=").append(_createTime).append(", msgType=")
                .append(_msgType).append(", msgId=").append(_msgId).append("]");
        return builder.toString();
    }
}
