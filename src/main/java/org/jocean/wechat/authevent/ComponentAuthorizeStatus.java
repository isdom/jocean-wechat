package org.jocean.wechat.authevent;


import javax.ws.rs.Consumes;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * https://developers.weixin.qq.com/doc/oplatform/Third-party_Platforms/api/authorize_event.html
 *
    授权变更通知推送
    当公众号/小程序对第三方平台进行授权、取消授权、更新授权后，微信服务器会向第三方平台方的授权事件接收 URL（创建时由第三方平台时填写）以 POST 的方式推送相关通知。

    接收 POST 请求后，只需直接返回字符串 success。为了加强安全性，postdata 中的 xml 将使用服务申请时的加解密 key 来进行加密，具体请见《加密解密技术方案》, 在收到推送后需进行解密（详细请见《消息加解密接入指引》）,

    字段说明
    参数  类型  字段描述
    AppId   string  第三方平台 appid
    CreateTime  number  时间戳
    InfoType    string  通知类型，详见InfoType 说明
    AuthorizerAppid string  公众号或小程序的 appid
    AuthorizationCode   string  授权码，可用于获取授权信息
    AuthorizationCodeExpiredTime    nubmer  授权码过期时间 单位秒
    PreAuthCode string  预授权码
    InfoType 说明
    type    说明
    unauthorized    取消授权
    updateauthorized    更新授权
    authorized  授权成功
    推送内容解密后的示例：

    授权成功通知
    <xml>
      <AppId>第三方平台appid</AppId>
      <CreateTime>1413192760</CreateTime>
      <InfoType>authorized</InfoType>
      <AuthorizerAppid>公众号appid</AuthorizerAppid>
      <AuthorizationCode>授权码</AuthorizationCode>
      <AuthorizationCodeExpiredTime>过期时间</AuthorizationCodeExpiredTime>
      <PreAuthCode>预授权码</PreAuthCode>
    <xml>
    取消授权通知
    <xml>
      <AppId>第三方平台appid</AppId>
      <CreateTime>1413192760</CreateTime>
      <InfoType>unauthorized</InfoType>
      <AuthorizerAppid>公众号appid</AuthorizerAppid>
    </xml>
    授权更新通知
    <xml>
      <AppId>第三方平台appid</AppId>
      <CreateTime>1413192760</CreateTime>
      <InfoType>updateauthorized</InfoType>
      <AuthorizerAppid>公众号appid</AuthorizerAppid>
      <AuthorizationCode>授权码</AuthorizationCode>
      <AuthorizationCodeExpiredTime>过期时间</AuthorizationCodeExpiredTime>
      <PreAuthCode>预授权码</PreAuthCode>
    <xml>
 *
 */

@Consumes({"application/xml","text/xml"})
@JacksonXmlRootElement(localName="xml")
public class ComponentAuthorizeStatus extends ComponentAuthBase {

    @JacksonXmlProperty(localName="AuthorizerAppid")
    public String getAuthorizerAppid() {
        return _authorizerAppid;
    }

    @JacksonXmlProperty(localName="AuthorizerAppid")
    public void setAuthorizerAppid(final String authorizerAppid) {
        _authorizerAppid = authorizerAppid;
    }

    @JacksonXmlProperty(localName="AuthorizationCode")
    public String getAuthorizationCode() {
        return _authorizationCode;
    }

    @JacksonXmlProperty(localName="AuthorizationCode")
    public void setAuthorizationCode(final String authorizationCode) {
        _authorizationCode = authorizationCode;
    }

    @JacksonXmlProperty(localName="AuthorizationCodeExpiredTime")
    public Integer getAuthorizationCodeExpiredTime() {
        return _authorizationCodeExpiredTime;
    }

    @JacksonXmlProperty(localName="AuthorizationCodeExpiredTime")
    public void setAuthorizationCodeExpiredTime(final Integer authorizationCodeExpiredTime) {
        _authorizationCodeExpiredTime = authorizationCodeExpiredTime;
    }

    @JacksonXmlProperty(localName="PreAuthCode")
    public String getPreAuthCode() {
        return _preAuthCode;
    }

    @JacksonXmlProperty(localName="PreAuthCode")
    public void setPreAuthCode(final String preAuthCode) {
        _preAuthCode = preAuthCode;
    }

    //  公众号appid: authorized/unauthorized/updateauthorized
    private String _authorizerAppid;

    //  授权码（code）: authorized/updateauthorized
    private String _authorizationCode;

    //  过期时间: authorized/updateauthorized
    private Integer _authorizationCodeExpiredTime;

    //  预授权码: authorized/updateauthorized
    private String _preAuthCode;

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ComponentAuthorizeStatus [authorizerAppid=").append(_authorizerAppid)
                .append(", authorizationCode=").append(_authorizationCode).append(", authorizationCodeExpiredTime=")
                .append(_authorizationCodeExpiredTime).append(", preAuthCode=").append(_preAuthCode)
                .append(", appId=").append(_appId).append(", createTime=").append(_createTime).append(", infoType=")
                .append(_infoType).append("]");
        return builder.toString();
    }

}
