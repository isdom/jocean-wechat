package org.jocean.wechat.spi;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 */
public abstract class ToWXMessage {
    // 开发人员微信号
    private String ToUserName;

    // 发送方帐号（一个OpenID）
    private String FromUserName;

    // 消息创建时间 （整型）
    private long CreateTime;

    // 消息类型（text/image/location/link）
    private String MsgType;

    @JacksonXmlProperty(localName="ToUserName")
    public String getToUserName() {
        return ToUserName;
    }

    @JacksonXmlProperty(localName="ToUserName")
    public void setToUserName(final String toUserName) {
        ToUserName = toUserName;
    }

    @JacksonXmlProperty(localName="FromUserName")
    public String getFromUserName() {
        return FromUserName;
    }

    @JacksonXmlProperty(localName="FromUserName")
    public void setFromUserName(final String fromUserName) {
        FromUserName = fromUserName;
    }

    @JacksonXmlProperty(localName="CreateTime")
    public long getCreateTime() {
        return CreateTime;
    }

    @JacksonXmlProperty(localName="CreateTime")
    public void setCreateTime(final long createTime) {
        CreateTime = createTime;
    }

    @JacksonXmlProperty(localName="MsgType")
    public String getMsgType() {
        return MsgType;
    }

    @JacksonXmlProperty(localName="MsgType")
    public void setMsgType(final String msgType) {
        MsgType = msgType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}