package org.jocean.wechat.spi.msg;

import javax.ws.rs.Consumes;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@Consumes({"application/xml","text/xml"})
@JacksonXmlRootElement(localName="xml")
public class WeappAuditMessage extends BaseWXMessage {

    // 代码审核结果推送
    /**
     * https://developers.weixin.qq.com/doc/oplatform/Third-party_Platforms/Mini_Programs/code/audit_event.html
    当小程序有审核结果后，微信服务器会向第三方平台方的消息与事件接收 URL（创建时由第三方平台时填写）以 POST 的方式推送相关通知
    接收 POST 请求后，只需直接返回字符串 success。为了加强安全性，postdata 中的 xml 将使用服务申请时的加解密 key 来进行加密，具体请见《加密解密技术方案》, 在收到推送后需进行解密（详细请见《消息加解密接入指引》）,
    除了消息通知之外，第三方平台也可通过接口查询指定版本的审核状态、查询最新一次提交的审核状态

    字段说明
    SuccTime    Number  审核成功时的时间戳
    FailTime    Number  审核不通过的时间戳
    DelayTime   Number  审核延后时的时间戳
    Reason  String  审核不通过的原因
    ScreenShot  String  审核不通过的截图示例。用 | 分隔的 media_id 的列表，可通过获取永久素材接口拉取截图内容

    事件类型
    事件类型    说明
    weapp_audit_success 审核通过
    weapp_audit_fail    审核不通过
    weapp_audit_delay   审核延后

    推送内容解密后的示例：
    审核通过
    <xml>
      <ToUserName><![CDATA[gh_fb9688c2a4b2]]></ToUserName>
      <FromUserName><![CDATA[od1P50M-fNQI5Gcq-trm4a7apsU8]]></FromUserName>
      <CreateTime>1488856741</CreateTime>
      <MsgType><![CDATA[event]]></MsgType>
      <Event><![CDATA[weapp_audit_success]]></Event>
      <SuccTime>1488856741</SuccTime>
    </xml>
    审核不通过
    <xml>
      <ToUserName><![CDATA[gh_fb9688c2a4b2]]></ToUserName>
      <FromUserName><![CDATA[od1P50M-fNQI5Gcq-trm4a7apsU8]]></FromUserName>
      <CreateTime>1488856591</CreateTime>
      <MsgType><![CDATA[event]]></MsgType>
      <Event><![CDATA[weapp_audit_fail]]></Event>
      <Reason><![CDATA[1:账号信息不符合规范:<br>(1):包含色情因素<br>2:服务类目"金融业-保险_"与你提交代码审核时设置的功能页面内容不一致:<br>(1):功能页面设置的部分标签不属于所选的服务类目范围。<br>(2):功能页面设置的部分标签与该页面内容不相关。<br>]]></Reason>
      <FailTime>1488856591</FailTime>
      <ScreenShot>xxx|yyy|zzz</ScreeenShot>
    </xml>
    审核延后
    <xml>
      <ToUserName><![CDATA[gh_fb9688c2a4b2]]></ToUserName>
      <FromUserName><![CDATA[od1P50M-fNQI5Gcq-trm4a7apsU8]]></FromUserName>
      <CreateTime>1488856591</CreateTime>
      <MsgType><![CDATA[event]]></MsgType>
      <Event><![CDATA[weapp_audit_delay]]></Event>
      <Reason><![CDATA[为了更好的服务小程序，您的服务商正在进行提审系统的优化，可能会导致审核时效的增长，请耐心等待]]></Reason>
      <DelayTime>1488856591</DelayTime>
    </xml>
     */

    //  审核成功时的时间戳
    private long _succTime;
    //  审核不通过的时间戳
    private long _failTime;
    // 审核延后时的时间戳
    private long _delayTime;
    //  审核不通过的原因
    private String _reason;
    //  审核不通过的截图示例。用 | 分隔的 media_id 的列表，可通过获取永久素材接口拉取截图内容
    private String _screenShot;

    @JacksonXmlProperty(localName="SuccTime")
    public long getSuccTime() {
        return _succTime;
    }

    @JacksonXmlProperty(localName="SuccTime")
    public void setSuccTime(final long succTime) {
        this._succTime = succTime;
    }

    @JacksonXmlProperty(localName="FailTime")
    public long getFailTime() {
        return _failTime;
    }

    @JacksonXmlProperty(localName="FailTime")
    public void setFailTime(final long failTime) {
        this._failTime = failTime;
    }

    @JacksonXmlProperty(localName="DelayTime")
    public long getDelayTime() {
        return _delayTime;
    }

    @JacksonXmlProperty(localName="DelayTime")
    public void setDelayTime(final long delayTime) {
        this._delayTime = delayTime;
    }

    @JacksonXmlProperty(localName="Reason")
    public String getReason() {
        return _reason;
    }

    @JacksonXmlProperty(localName="Reason")
    public void setReason(final String reason) {
        this._reason = reason;
    }

    @JacksonXmlProperty(localName="ScreenShot")
    public String getScreenShot() {
        return _screenShot;
    }

    @JacksonXmlProperty(localName="ScreenShot")
    public void setScreenShot(final String screenShot) {
        this._screenShot = screenShot;
    }
}
