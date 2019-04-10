package org.jocean.wechat.spi;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_4
 */
@Consumes({"application/xml", "text/xml"})
@JacksonXmlRootElement(localName = "xml")
@Path("https://api.mch.weixin.qq.com/secapi/pay/refund")
public class RefundRequest{

    // 服务商ID    appid   是   String(32)  wxd678efh567hg6787  微信分配的公众账号ID
    @JacksonXmlProperty(localName = "appid")
    public String getAppid() {
        return appid;
    }

    // 服务商ID    appid   是   String(32)  wxd678efh567hg6787  微信分配的公众账号ID
    @JacksonXmlProperty(localName = "appid")
    public void setAppid(final String appid) {
        this.appid = appid;
    }

    // 商户号  mch_id  是   String(32)  1230000109  微信支付分配的商户号
    @JacksonXmlProperty(localName = "mch_id")
    public String getMchId() {
        return mch_id;
    }

    // 商户号  mch_id  是   String(32)  1230000109  微信支付分配的商户号
    @JacksonXmlProperty(localName = "mch_id")
    public void setMchId(final String mch_id) {
        this.mch_id = mch_id;
    }


    //  随机字符串   nonce_str   是   String(32)  5K8264ILTKCH16CQ2502SI8ZNMTM67VS    随机字符串，不长于32位。推荐随机数生成算法
    @JacksonXmlProperty(localName = "nonce_str")
    public String getNonceStr() {
        return nonce_str;
    }

    //  随机字符串   nonce_str   是   String(32)  5K8264ILTKCH16CQ2502SI8ZNMTM67VS    随机字符串，不长于32位。推荐随机数生成算法
    @JacksonXmlProperty(localName = "nonce_str")
    public void setNonceStr(final String nonce_str) {
        this.nonce_str = nonce_str;
    }

    //  签名  sign    是   String(32)  C380BEC2BFD727A4B6845133519F3AD6    签名，详见签名生成算法
    @JacksonXmlProperty(localName = "sign")
    public String getSign() {
        return sign;
    }

    //  签名  sign    是   String(32)  C380BEC2BFD727A4B6845133519F3AD6    签名，详见签名生成算法
    @JacksonXmlProperty(localName = "sign")
    public void setSign(final String sign) {
        this.sign = sign;
    }

    //  签名类型    sign_type   否   String(32)  HMAC-SHA256 签名类型，目前支持HMAC-SHA256和MD5，默认为MD5
    @JacksonXmlProperty(localName = "sign_type")
    public String getSignType() {
        return this.sign_type;
    }

    //  签名类型    sign_type   否   String(32)  HMAC-SHA256 签名类型，目前支持HMAC-SHA256和MD5，默认为MD5
    @JacksonXmlProperty(localName = "sign_type")
    public void setSignType(final String sign_type) {
        this.sign_type = sign_type;
    }


    // 商户订单号    out_trade_no    是   String(32)  1217752501201407033233368018    商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*且在同一个商户号下唯一。详见商户订单号
    @JacksonXmlProperty(localName = "out_trade_no")
    public String getOutTradeNo() {
        return out_trade_no;
    }

    // 商户订单号    out_trade_no    是   String(32)  1217752501201407033233368018    商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*且在同一个商户号下唯一。详见商户订单号
    @JacksonXmlProperty(localName = "out_trade_no")
    public void setOutTradeNo(final String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }


    @JacksonXmlProperty(localName = "transaction_id")
    public String getTransactionId() {
        return transaction_id;
    }

    @JacksonXmlProperty(localName = "transaction_id")
    public void setTransactionId(final String transaction_id) {
        this.transaction_id = transaction_id;
    }

    @JacksonXmlProperty(localName = "total_fee")
    public int getTotalFee() {
        return total_fee;
    }

    @JacksonXmlProperty(localName = "total_fee")
    public void setTotalFee(final int total_fee) {
        this.total_fee = total_fee;
    }

    @JacksonXmlProperty(localName = "refund_fee")
    public int getRefundFee() {
        return refund_fee;
    }

    @JacksonXmlProperty(localName = "refund_fee")
    public void setRefundFee(final int refund_fee) {
        this.refund_fee = refund_fee;
    }

    @JacksonXmlProperty(localName = "refund_fee_type")
    public String getRefundFeeType() {
        return refund_fee_type;
    }

    @JacksonXmlProperty(localName = "refund_fee_type")
    public void setRefundFeeType(final String refund_fee_type) {
        this.refund_fee_type = refund_fee_type;
    }

    @JacksonXmlProperty(localName = "notify_url")
    public String getNotifyUrl() {
        return notify_url;
    }

    @JacksonXmlProperty(localName = "notify_url")
    public void setNotifyUrl(final String notify_url) {
        this.notify_url = notify_url;
    }

    @JacksonXmlProperty(localName = "refund_desc")
    public String getRefundDesc() {
        return refund_desc;
    }

    @JacksonXmlProperty(localName = "refund_desc")
    public void setRefundDesc(final String refund_desc) {
        this.refund_desc = refund_desc;
    }

    @JacksonXmlProperty(localName = "refund_account")
    public String getRefundAccount() {
        return refund_account;
    }

    @JacksonXmlProperty(localName = "refund_account")
    public void setRefundAccount(final String refund_account) {
        this.refund_account = refund_account;
    }
    @JacksonXmlProperty(localName = "out_refund_no")
    public String getOutRefundNo() {
        return out_refund_no;
    }
    @JacksonXmlProperty(localName = "out_refund_no")
    public void setOutRefundNo(final String out_refund_no) {
        this.out_refund_no = out_refund_no;
    }

    private String appid;
    private String mch_id;
    private String nonce_str;

    private String sign;//签名，详见签名生成算法
    private String sign_type = "MD5";


    private String transaction_id;//微信订单号
    private String out_trade_no;//商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
    private int total_fee;
    private int refund_fee;

    private String out_refund_no; //商户退款单号

    private String refund_fee_type = "CNY";


    private String notify_url;
    private String refund_desc;//退款原因

    private String refund_account;//退款资金来源


}
