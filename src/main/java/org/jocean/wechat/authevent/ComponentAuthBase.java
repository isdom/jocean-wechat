package org.jocean.wechat.authevent;


import javax.ws.rs.Consumes;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1453779503&token=&lang=zh_CN
 *
 * 1、推送component_verify_ticket协议
 *
 * 在第三方平台创建审核通过后，微信服务器会向其“授权事件接收URL”每隔10分钟定时推送component_verify_ticket。
 * 第三方平台方在收到ticket推送后也需进行解密（详细请见【消息加解密接入指引】），接收到后必须直接返回字符串success。
 *
 * POST数据示例
 * <xml>
 * <AppId> </AppId>
 * <CreateTime>1413192605 </CreateTime>
 * <InfoType> </InfoType>
 * <ComponentVerifyTicket> </ComponentVerifyTicket>
 * </xml>
 *
 * 字段说明
 * 字段名称    字段描述
 * AppId   第三方平台appid
 * CreateTime  时间戳
 * InfoType    component_verify_ticket
 * ComponentVerifyTicket   Ticket内容
 *
 * 9、推送授权相关通知
 *
 * 当公众号对第三方平台进行授权、取消授权、更新授权后，微信服务器会向第三方平台方的授权事件接收URL（创建第三方平台时填写）推送相关通知。
 * POST数据示例（授权成功通知）
 * <xml>
 * <AppId>第三方平台appid</AppId>
 * <CreateTime>1413192760</CreateTime>
 * <InfoType>authorized</InfoType>
 * <AuthorizerAppid>公众号appid</AuthorizerAppid>
 * <AuthorizationCode>授权码（code）</AuthorizationCode>
 * <AuthorizationCodeExpiredTime>过期时间</AuthorizationCodeExpiredTime>
 * <PreAuthCode>预授权码</PreAuthCode>
 * <xml>
 *
 * POST数据示例（取消授权通知）
 * <xml>
 * <AppId>第三方平台appid</AppId>
 * <CreateTime>1413192760</CreateTime>
 * <InfoType>unauthorized</InfoType>
 * <AuthorizerAppid>公众号appid</AuthorizerAppid>
 *
 * </xml>
 * POST数据示例（授权更新通知）
 * <xml>
 * <AppId>第三方平台appid</AppId>
 * <CreateTime>1413192760</CreateTime>
 * <InfoType>updateauthorized</InfoType>
 * <AuthorizerAppid>公众号appid</AuthorizerAppid>
 * <AuthorizationCode>授权码（code）</AuthorizationCode>
 * <AuthorizationCodeExpiredTime>过期时间</AuthorizationCodeExpiredTime>
 * <PreAuthCode>预授权码</PreAuthCode>
 * <xml>
 *
 * 第三方平台方在收到授权相关通知后也需进行解密（详细请见【消息加解密接入指引】），接收到后之后只需直接返回字符串success。
 * 为了加强安全性，postdata中的xml将使用服务申请时的加解密key来进行加密，具体请见【公众号第三方平台的加密解密技术方案】
 *
 * 字段说明：
 * 字段名称    字段描述
 * AppId   第三方平台appid
 * CreateTime  时间戳
 * InfoType    unauthorized是取消授权，updateauthorized是更新授权，authorized是授权成功通知
 * AuthorizerAppid 公众号或小程序
 * AuthorizationCode   授权码，可用于换取公众号的接口调用凭据，详细见上面的说明
 * AuthorizationCodeExpiredTime    授权码过期时间
 * PreAuthCode 预授权码
 *
 */

@Consumes({"application/xml","text/xml"})
@JacksonXmlRootElement(localName="xml")
public class ComponentAuthBase {

    @JacksonXmlProperty(localName="AppId")
    public String getAppId() {
        return _appId;
    }

    @JacksonXmlProperty(localName="AppId")
    public void setAppId(final String appId) {
        _appId = appId;
    }

    @JacksonXmlProperty(localName="CreateTime")
    public Integer getCreateTime() {
        return _createTime;
    }

    @JacksonXmlProperty(localName="CreateTime")
    public void setCreateTime(final Integer createTime) {
        _createTime = createTime;
    }

    @JacksonXmlProperty(localName="InfoType")
    public String getInfoType() {
        return _infoType;
    }

    @JacksonXmlProperty(localName="InfoType")
    public void setInfoType(final String infoType) {
        _infoType = infoType;
    }

    //  第三方平台appid
    protected String _appId;

    //  时间戳
    protected Integer _createTime;

    //  component_verify_ticket / authorized / unauthorized / updateauthorized
    protected String _infoType;
}
