package org.jocean.wechat.spi;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class OrderResponse extends PayResponse {
    @JacksonXmlProperty(localName="appid")
    public String getAppid() {
        return appid;
    }

    @JacksonXmlProperty(localName="appid")
    public void setAppid(final String appid) {
        this.appid = appid;
    }

    @JacksonXmlProperty(localName="mch_id")
    public String getMchId() {
        return mch_id;
    }

    @JacksonXmlProperty(localName="mch_id")
    public void setMchId(final String mch_id) {
        this.mch_id = mch_id;
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

    @JacksonXmlProperty(localName="sign")
    public String getSign() {
        return sign;
    }

    @JacksonXmlProperty(localName="sign")
    public void setSign(final String sign) {
        this.sign = sign;
    }

    // 协议返回的具体数据（以下字段在return_code 为SUCCESS 的时候有返回）
    //  小程序ID    appid   是   String(32)  wxd678efh567hg6787  微信分配的小程序ID
    protected String appid;
    // 商户号  mch_id  是   String(32)  1230000109  微信支付分配的商户号
    protected String mch_id;//  String(32)  1900000109  微信支付分配的商户号

    // 随机字符串    nonce_str   是   String(32)  5K8264ILTKCH16CQ2502SI8ZNMTM67VS    随机字符串，不长于32位。推荐随机数生成算法
    protected String nonce_str;

    // 签名   sign    是   String(32)  C380BEC2BFD727A4B6845133519F3AD6    签名，详见签名生成算法
    protected String sign;

    // 以下字段在return_code 、result_code、trade_state都为SUCCESS时有返回 ，
    // 如trade_state不为 SUCCESS，则只返回out_trade_no（必传）和attach（选传）。
    // 设备号  device_info 否   String(32)  013467007045764 微信支付分配的终端设备号，
    protected String device_info;
}
