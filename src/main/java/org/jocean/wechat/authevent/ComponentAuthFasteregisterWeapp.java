package org.jocean.wechat.authevent;


import javax.ws.rs.Consumes;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * https://developers.weixin.qq.com/doc/oplatform/Third-party_Platforms/Mini_Programs/Fast_Registration_Interface_document.html
 *
 * 三、注册审核事件推送
 *
 数据示例

    <xml>
        <AppId><![CDATA[第三方平台appid]]></AppId>
        <CreateTime>1535442403</CreateTime>
        <InfoType><![CDATA[notify_third_fasteregister]]></InfoType>
        <appid>创建小程序appid<appid>
        <status>0</status>
        <auth_code>xxxxx第三方授权码</auth_code>
        <msg>OK</msg>
        <info>
        <name><![CDATA[企业名称]]></name>
        <code><![CDATA[企业代码]]></code>
        <code_type>1</code_type>
        <legal_persona_wechat><![CDATA[法人微信号]]></legal_persona_wechat>
        <legal_persona_name><![CDATA[法人姓名]]></legal_persona_name>
        <component_phone><![CDATA[第三方联系电话]]></component_phone>
        </info>
    </xml>

 *
 */

@Consumes({"application/xml","text/xml"})
@JacksonXmlRootElement(localName="xml")
public class ComponentAuthFasteregisterWeapp extends ComponentAuthBase {

    @JacksonXmlProperty(localName="appid")
    public String getAuthorizerAppid() {
        return _authorizerAppid;
    }

    @JacksonXmlProperty(localName="appid")
    public void setAuthorizerAppid(final String appid) {
        _authorizerAppid = appid;
    }

    @JacksonXmlProperty(localName="auth_code")
    public String getAuthCode() {
        return _authCode;
    }

    @JacksonXmlProperty(localName="auth_code")
    public void setAuthCode(final String authcode) {
        _authCode = authcode;
    }

    @JacksonXmlProperty(localName="status")
    public String getStatus() {
        return _status;
    }

    @JacksonXmlProperty(localName="status")
    public void setStatus(final String status) {
        _status = status;
    }

    //  第三方授权码
    private String _authCode;

    //  创建小程序 appid
    private String _authorizerAppid;

    private String _status;

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ComponentAuthFasteregisterWeapp [authCode=").append(_authCode).append(", authorizerAppid=")
                .append(_authorizerAppid).append(", status=").append(_status).append(", appId=").append(_appId)
                .append(", createTime=").append(_createTime).append(", infoType=").append(_infoType).append("]");
        return builder.toString();
    }
}
