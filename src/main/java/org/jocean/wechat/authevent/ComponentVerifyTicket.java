package org.jocean.wechat.authevent;


import javax.ws.rs.Consumes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * https://developers.weixin.qq.com/doc/oplatform/Third-party_Platforms/api/component_verify_ticket.html
 *
    验证票据
    验证票据（component_verify_ticket），在第三方平台创建审核通过后，微信服务器会向其 ”授权事件接收URL” 每隔 10 分钟以 POST 的方式推送 component_verify_ticket

    接收 POST 请求后，只需直接返回字符串 success。为了加强安全性，postdata 中的 xml 将使用服务申请时的加解密 key 来进行加密，具体请见《加密解密技术方案》, 在收到推送后需进行解密（详细请见《消息加解密接入指引》）,

    参数说明
    参数  类型  字段描述
    AppId   string  第三方平台 appid
    CreateTime  number  时间戳，单位：s
    InfoType    string  固定为："component_verify_ticket"
    ComponentVerifyTicket   string  Ticket 内容
    推送内容解密后的示例：

    <xml>
    <AppId>some_appid</AppId>
    <CreateTime>1413192605</CreateTime>
    <InfoType>component_verify_ticket</InfoType>
    <ComponentVerifyTicket>some_verify_ticket</ComponentVerifyTicket>
    </xml>
    注意：

    component_verify_ticket 的有效时间较 component_access_token 更长，建议保存最近可用的component_verify_ticket，
    在 component_access_token 过期之前可以直接使用该 component_verify_ticket 进行更新，避免出现因为 component_verify_ticket 接收失败而无法更新 component_access_token 的情况。

 *
 */

@Consumes({"application/xml","text/xml"})
@JacksonXmlRootElement(localName="xml")
public class ComponentVerifyTicket extends ComponentAuthBase {

    @JacksonXmlProperty(localName="ComponentVerifyTicket")
    public String getComponentVerifyTicket() {
        return _componentVerifyTicket;
    }

    @JacksonXmlProperty(localName="ComponentVerifyTicket")
    public void setComponentVerifyTicket(final String componentVerifyTicket) {
        _componentVerifyTicket = componentVerifyTicket;
    }

    //  Ticket内容: component_verify_ticket
    private String _componentVerifyTicket;

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ComponentVerifyTicket [componentVerifyTicket=").append(_componentVerifyTicket)
                .append(", appId=").append(_appId).append(", createTime=").append(_createTime).append(", infoType=")
                .append(_infoType).append("]");
        return builder.toString();
    }

    public static void main(final String[] args) throws Exception {
        final String xmlsrc =
                "<xml>"
                + "<AppId>123456789</AppId>"
                + "<CreateTime>1413192605</CreateTime>"
                + "<InfoType>component_verify_ticket</InfoType>"
                + "<ComponentVerifyTicket>ticket_demo</ComponentVerifyTicket>"
                + "</xml>";
        final ObjectMapper mapper = new XmlMapper();

        final ComponentVerifyTicket msg = mapper.readValue(xmlsrc, ComponentVerifyTicket.class);
        System.out.println("msg:" + msg);
        System.out.println("as Xml:\r\n" + mapper.enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(msg) );

    }
}
