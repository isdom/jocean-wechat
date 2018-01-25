package org.jocean.wechat.spi;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 */
@JacksonXmlRootElement(localName="xml")
public class TextMessage extends ToWXMessage {

    // 回复的消息内容（换行：在content中能够换行，微信客户端就支持换行显示）
    private String Content;

    @JacksonXmlProperty(localName="Content")
    public String getContent() {
        return Content;
    }

    @JacksonXmlProperty(localName="Content")
    public void setContent(final String content) {
        Content = content;
    }

    public TextMessage() {
        this.setMsgType("text");
    }
}
