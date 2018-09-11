package org.jocean.wechat.spi;

import javax.ws.rs.Consumes;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@Consumes({"application/xml","text/xml"})
@JacksonXmlRootElement(localName="xml")
public class PromotionTransfersResponse extends PayResponse {
    @JacksonXmlProperty(localName="mch_appid")
    public String getAppid() {
        return mch_appid;
    }

    @JacksonXmlProperty(localName="mch_appid")
    public void setAppid(final String mch_appid) {
        this.mch_appid = mch_appid;
    }

    @JacksonXmlProperty(localName="mchid")
    public String getMchId() {
        return mchid;
    }

    @JacksonXmlProperty(localName="mchid")
    public void setMchId(final String mchid) {
        this.mchid = mchid;
    }

    @JacksonXmlProperty(localName="device_info")
    public String getDeviceInfo() {
        return device_info;
    }

    @JacksonXmlProperty(localName="device_info")
    public void setDeviceInfo(final String device_info) {
        this.device_info = device_info;
    }

    @JacksonXmlProperty(localName="nonce_str")
    public String getNonceStr() {
        return nonce_str;
    }

    @JacksonXmlProperty(localName="nonce_str")
    public void setNonceStr(final String nonce_str) {
        this.nonce_str = nonce_str;
    }

    @JacksonXmlProperty(localName="partner_trade_no")
    public String getPartnerTradeNo() {
        return partner_trade_no;
    }
    @JacksonXmlProperty(localName="partner_trade_no")
    public void setPartnerTradeNo(final String partner_trade_no) {
        this.partner_trade_no = partner_trade_no;
    }

    @JacksonXmlProperty(localName="payment_no")
    public String getPaymentNo() {
        return payment_no;
    }

    @JacksonXmlProperty(localName="payment_no")
    public void setPaymentNo(final String payment_no) {
        this.payment_no = payment_no;
    }

    @JacksonXmlProperty(localName="payment_time")
    public String getPaymentTime() {
        return payment_time;
    }

    @JacksonXmlProperty(localName="payment_time")
    public void setPaymentTime(final String payment_time) {
        this.payment_time = payment_time;
    }

    // 商户appid mch_appid   是   wx8888888888888888  String  申请商户号的appid或商户号绑定的appid（企业号corpid即为此appId）
    protected String mch_appid;

    // 商户号  mchid   是   1900000109  String(32)  微信支付分配的商户号
    protected String mchid;

    // 随机字符串    nonce_str   是   5K8264ILTKCH16CQ2502SI8ZNMTM67VS    String(32)  随机字符串，不长于32位
    protected String nonce_str;

    // 设备号  device_info 否   013467007045764 String(32)  微信支付分配的终端设备号，
    protected String device_info;

    // 商户订单号    partner_trade_no    是   1217752501201407033233368018    String(32)  商户订单号，需保持历史全局唯一性(只能是字母或者数字，不能包含有符号)
    private String partner_trade_no;

    // 微信订单号    payment_no  是   1007752501201407033233368018    String  企业付款成功，返回的微信订单号
    private String payment_no;

    // 微信支付成功时间 payment_time    是   2015-05-19 15：26：59 String  企业付款成功时间
    private String payment_time;

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("PromotionTransfersResponse [mch_appid=").append(mch_appid).append(", mchid=").append(mchid)
                .append(", nonce_str=").append(nonce_str).append(", device_info=").append(device_info)
                .append(", partner_trade_no=").append(partner_trade_no).append(", payment_no=").append(payment_no)
                .append(", payment_time=").append(payment_time).append(", return_code=").append(return_code)
                .append(", return_msg=").append(return_msg).append(", result_code=").append(result_code)
                .append(", err_code=").append(err_code).append(", err_code_des=").append(err_code_des).append("]");
        return builder.toString();
    }
}
