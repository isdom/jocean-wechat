package org.jocean.wechat.spi.msg;

import javax.ws.rs.Consumes;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@Consumes({"application/xml","text/xml"})
@JacksonXmlRootElement(localName="xml")
public class WxaNicknameAuditMessage extends BaseWXMessage {

    // 名称审核结果事件推送
    /**
     * https://developers.weixin.qq.com/doc/oplatform/Third-party_Platforms/Mini_Programs/wxa_nickname_audit.html
    名称审核结果事件推送
    小程序改名的审核结果会向授权事件接收 URL 以 POST 的方式推送相关通知。

    接收 POST 请求后，只需直接返回字符串 success。为了加强安全性，postdata 中的 xml 将使用服务申请时的加解密 key 来进行加密，具体请见《加密解密技术方案》, 在收到推送后需进行解密（详细请见《消息加解密接入指引》）,

    字段说明
    参数  类型  字段描述
    nickname    string  需要更改的昵称
    ret number  审核结果 2：失败，3：成功
    reason  string  审核失败的驳回原因
    推送内容解密后的示例：

    <xml>
        <ToUserName><![CDATA[gh_fxxxxxxxa4b2]]></ToUserName>
        <FromUserName><![CDATA[odxxxxM-xxxxxxxx-trm4a7apsU8]]></FromUserName>
        <CreateTime>1488800000</CreateTime>
        <MsgType><![CDATA[event]]></MsgType>
        <Event><![CDATA[wxa_nickname_audit]]></Event>
        <ret>2</ret>
        <nickname>昵称</nickname>
        <reason>驳回原因</reason>
    </xml>
     */
    //  需要更改的昵称
    private String _nickname;
    //  审核结果 2：失败，3：成功
    private long _ret;
    //  审核失败的驳回原因
    private String _reason;

    @JacksonXmlProperty(localName="nickname")
    public String getNickname() {
        return _nickname;
    }

    @JacksonXmlProperty(localName="nickname")
    public void setNickname(final String nickname) {
        this._nickname = nickname;
    }

    @JacksonXmlProperty(localName="ret")
    public long getRet() {
        return _ret;
    }

    @JacksonXmlProperty(localName="ret")
    public void setRet(final long ret) {
        this._ret = ret;
    }

    @JacksonXmlProperty(localName="reason")
    public String getReason() {
        return _reason;
    }

    @JacksonXmlProperty(localName="reason")
    public void setReason(final String reason) {
        this._reason = reason;
    }


}
