package org.jocean.wechat.spi;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@Produces({MediaType.APPLICATION_XML})
@JacksonXmlRootElement(localName = "xml")
@Path("https://api.mch.weixin.qq.com/mmpaymkttransfers/gettransferinfo")
public class TransfersQueryRequest {
    @JacksonXmlProperty(localName = "appid")
    public String getMchAppid() {
        return mch_appid;
    }

    @JacksonXmlProperty(localName = "appid")
    public void setMchAppid(final String mch_appid) {
        this.mch_appid = mch_appid;
    }

    @JacksonXmlProperty(localName = "mch_id")
    public String getMchId() {
        return mchid;
    }

    @JacksonXmlProperty(localName = "mch_id")
    public void setMchId(final String mchid) {
        this.mchid = mchid;
    }

    @JacksonXmlProperty(localName = "nonce_str")
    public String getNonceStr() {
        return nonce_str;
    }

    @JacksonXmlProperty(localName = "nonce_str")
    public void setNonceStr(final String nonce_str) {
        this.nonce_str = nonce_str;
    }

    @JacksonXmlProperty(localName = "sign")
    public String getSign() {
        return sign;
    }

    @JacksonXmlProperty(localName = "sign")
    public void setSign(final String sign) {
        this.sign = sign;
    }

    @JacksonXmlProperty(localName = "partner_trade_no")
    public String getPartnerTradeNo() {
        return partner_trade_no;
    }

    @JacksonXmlProperty(localName = "partner_trade_no")
    public void setPartnerTradeNo(final String partner_trade_no) {
        this.partner_trade_no = partner_trade_no;
    }


    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("PromotionTransfersRequest [appid=").append(mch_appid).append(", mch_id=").append(mchid)
                .append(", nonce_str=").append(nonce_str).append(", sign=")
                .append(sign).append(", partner_trade_no=").append(partner_trade_no).append("]");
        return builder.toString();
    }

    // 随机字符串    nonce_str   是   5K8264ILTKCH16CQ2502SI8ZNMTM67VS    String(32)  随机字符串，不长于32位
    private String nonce_str;

    // 签名   sign    是   C380BEC2BFD727A4B6845133519F3AD6    String(32)  签名，详见签名算法
    private String sign;

    private String partner_trade_no;
    private String mch_appid;
    private String mchid;
}

