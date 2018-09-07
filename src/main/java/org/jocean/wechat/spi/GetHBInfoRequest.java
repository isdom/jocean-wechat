package org.jocean.wechat.spi;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * 查询红包记录
 * // https://pay.weixin.qq.com/wiki/doc/api/tools/cash_coupon.php?chapter=13_6&index=5
 */
@Consumes({"application/xml","text/xml"})
@JacksonXmlRootElement(localName="xml")
@Path("https://api.mch.weixin.qq.com/mmpaymkttransfers/gethbinfo")
public class GetHBInfoRequest {

    //  随机字符串   nonce_str   是   5K8264ILTKCH16CQ2502SI8ZNMTM67VS    String(32)  随机字符串，不长于32位
    @JacksonXmlProperty(localName="nonce_str")
    public String getNonceStr() {
        return nonce_str;
    }

    //  随机字符串   nonce_str   是   5K8264ILTKCH16CQ2502SI8ZNMTM67VS    String(32)  随机字符串，不长于32位
    @JacksonXmlProperty(localName="nonce_str")
    public void setNonceStr(final String nonce_str) {
        this.nonce_str = nonce_str;
    }

    //  签名  sign    是   C380BEC2BFD727A4B6845133519F3AD6    String(32)  详见签名生成算法
    @JacksonXmlProperty(localName="sign")
    public String getSign() {
        return sign;
    }

    //  签名  sign    是   C380BEC2BFD727A4B6845133519F3AD6    String(32)  详见签名生成算法
    @JacksonXmlProperty(localName="sign")
    public void setSign(final String sign) {
        this.sign = sign;
    }

    // Appid    appid   是   wxe062425f740d30d8  String(32)  微信分配的公众账号ID（企业号corpid即为此appId），接口传入的所有appid应该为公众号的appid（在mp.weixin.qq.com申请的），不能为APP的appid（在open.weixin.qq.com申请的）。
    @JacksonXmlProperty(localName="appid")
    public String getAppid() {
        return appid;
    }

    // Appid    appid   是   wxe062425f740d30d8  String(32)  微信分配的公众账号ID（企业号corpid即为此appId），接口传入的所有appid应该为公众号的appid（在mp.weixin.qq.com申请的），不能为APP的appid（在open.weixin.qq.com申请的）。
    @JacksonXmlProperty(localName="appid")
    public void setAppid(final String appid) {
        this.appid = appid;
    }

    // 商户订单号    mch_billno  是   10000098201411111234567890  String(28)  商户发放红包的商户订单号
    @JacksonXmlProperty(localName="mch_billno")
    public String getMchBillno() {
        return this.mch_billno;
    }

    // 商户订单号    mch_billno  是   10000098201411111234567890  String(28)  商户发放红包的商户订单号
    @JacksonXmlProperty(localName="mch_billno")
    public void setMchBillno(final String mch_billno) {
        this.mch_billno = mch_billno;
    }

    // 商户号  mch_id  是   10000098    String(32)  微信支付分配的商户号
    @JacksonXmlProperty(localName="mch_id")
    public String getMchId() {
        return mch_id;
    }

    // 商户号  mch_id  是   10000098    String(32)  微信支付分配的商户号
    @JacksonXmlProperty(localName="mch_id")
    public void setMchId(final String mch_id) {
        this.mch_id = mch_id;
    }

    // 订单类型 bill_type   是   MCHT    String(32)  MCHT:通过商户订单号获取红包信息。
    @JacksonXmlProperty(localName="bill_type")
    public String getBillType() {
        return this.bill_type;
    }

    // 订单类型 bill_type   是   MCHT    String(32)  MCHT:通过商户订单号获取红包信息。
    @JacksonXmlProperty(localName="bill_type")
    public void setBillType(final String bill_type) {
        this.bill_type = bill_type;
    }

    private String appid;
    private String mch_id ;
    private String mch_billno;
    private String bill_type;

    private String sign;//签名，详见签名生成算法

    private String nonce_str ;

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("GetHBInfoRequest [appid=").append(appid).append(", mch_id=").append(mch_id)
                .append(", mch_billno=").append(mch_billno).append(", bill_type=").append(bill_type).append(", sign=")
                .append(sign).append(", nonce_str=").append(nonce_str).append("]");
        return builder.toString();
    }
}
