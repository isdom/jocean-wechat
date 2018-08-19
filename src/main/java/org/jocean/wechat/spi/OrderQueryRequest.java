package org.jocean.wechat.spi;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * 查询订单
 * https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_sl_api.php?chapter=9_2
 */
@Consumes({"application/xml","text/xml"})
@JacksonXmlRootElement(localName="xml")
@Path("https://api.mch.weixin.qq.com/pay/orderquery")
public class OrderQueryRequest {

    // 服务商ID    appid   是   String(32)  wxd678efh567hg6787  微信分配的公众账号ID
    @JacksonXmlProperty(localName="appid")
    public String getAppid() {
        return appid;
    }

    // 服务商ID    appid   是   String(32)  wxd678efh567hg6787  微信分配的公众账号ID
    @JacksonXmlProperty(localName="appid")
    public void setAppid(final String appid) {
        this.appid = appid;
    }

    // 商户号  mch_id  是   String(32)  1230000109  微信支付分配的商户号
    @JacksonXmlProperty(localName="mch_id")
    public String getMchId() {
        return mch_id;
    }

    // 商户号  mch_id  是   String(32)  1230000109  微信支付分配的商户号
    @JacksonXmlProperty(localName="mch_id")
    public void setMchId(final String mch_id) {
        this.mch_id = mch_id;
    }

    // 小程序的APPID    sub_appid   是   String(32)  wx8888888888888888  当前调起支付的小程序APPID
    @JacksonXmlProperty(localName="sub_appid")
    public String getSubAppid() {
        return this.sub_appid;
    }

    // 小程序的APPID    sub_appid   是   String(32)  wx8888888888888888  当前调起支付的小程序APPID
    @JacksonXmlProperty(localName="sub_appid")
    public void setSubAppid(final String sub_appid) {
        this.sub_appid = sub_appid;
    }

    // 子商户号    sub_mch_id  是   String(32)  1900000109  微信支付分配的子商户号
    @JacksonXmlProperty(localName="sub_mch_id")
    public String getSubMchId() {
        return this.sub_mch_id;
    }

    // 子商户号    sub_mch_id  是   String(32)  1900000109  微信支付分配的子商户号
    @JacksonXmlProperty(localName="sub_mch_id")
    public void setSubMchId(final String sub_mch_id) {
        this.sub_mch_id = sub_mch_id;
    }

    //  微信订单号   transaction_id  二选一 String(32)  1009660380201506130728806387    微信的订单号，优先使用
    @JacksonXmlProperty(localName="transaction_id")
    public String getTransactionId() {
        return transaction_id;
    }

    //  微信订单号   transaction_id  二选一 String(32)  1009660380201506130728806387    微信的订单号，优先使用
    @JacksonXmlProperty(localName="transaction_id")
    public void setTransactionId(final String transaction_id) {
        this.transaction_id = transaction_id;
    }

    // 商户订单号    out_trade_no    是   String(32)  1217752501201407033233368018    商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*且在同一个商户号下唯一。详见商户订单号
    @JacksonXmlProperty(localName="out_trade_no")
    public String getOutTradeNo() {
        return out_trade_no;
    }

    // 商户订单号    out_trade_no    是   String(32)  1217752501201407033233368018    商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*且在同一个商户号下唯一。详见商户订单号
    @JacksonXmlProperty(localName="out_trade_no")
    public void setOutTradeNo(final String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    //  随机字符串   nonce_str   是   String(32)  5K8264ILTKCH16CQ2502SI8ZNMTM67VS    随机字符串，不长于32位。推荐随机数生成算法
    @JacksonXmlProperty(localName="nonce_str")
    public String getNonceStr() {
        return nonce_str;
    }

    //  随机字符串   nonce_str   是   String(32)  5K8264ILTKCH16CQ2502SI8ZNMTM67VS    随机字符串，不长于32位。推荐随机数生成算法
    @JacksonXmlProperty(localName="nonce_str")
    public void setNonceStr(final String nonce_str) {
        this.nonce_str = nonce_str;
    }

    //  签名  sign    是   String(32)  C380BEC2BFD727A4B6845133519F3AD6    签名，详见签名生成算法
    @JacksonXmlProperty(localName="sign")
    public String getSign() {
        return sign;
    }

    //  签名  sign    是   String(32)  C380BEC2BFD727A4B6845133519F3AD6    签名，详见签名生成算法
    @JacksonXmlProperty(localName="sign")
    public void setSign(final String sign) {
        this.sign = sign;
    }

    //  签名类型    sign_type   否   String(32)  HMAC-SHA256 签名类型，目前支持HMAC-SHA256和MD5，默认为MD5
    @JacksonXmlProperty(localName="sign_type")
    public String getSignType() {
        return this.sign_type;
    }

    //  签名类型    sign_type   否   String(32)  HMAC-SHA256 签名类型，目前支持HMAC-SHA256和MD5，默认为MD5
    @JacksonXmlProperty(localName="sign_type")
    public void setSignType(final String sign_type) {
        this.sign_type = sign_type;
    }

    private String appid;
    private String mch_id ;
    private String sub_appid;
    private String sub_mch_id;
    private String transaction_id;

    private String sign;//签名，详见签名生成算法
    private String sign_type = "MD5";

    private String nonce_str ;
    private String out_trade_no;//商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("OrderQueryRequest [appid=").append(appid).append(", mch_id=").append(mch_id)
                .append(", sub_appid=").append(sub_appid).append(", sub_mch_id=").append(sub_mch_id)
                .append(", transaction_id=").append(transaction_id).append(", sign=").append(sign)
                .append(", sign_type=").append(sign_type).append(", nonce_str=").append(nonce_str)
                .append(", out_trade_no=").append(out_trade_no).append("]");
        return builder.toString();
    }
}
