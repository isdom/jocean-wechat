package org.jocean.wechat.spi;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@Produces({MediaType.APPLICATION_XML})
@JacksonXmlRootElement(localName="xml")
@Path("https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers")
public class PromotionTransfersRequest {
    @JacksonXmlProperty(localName="mch_appid")
    public String getMchAppid() {
        return mch_appid;
    }
    @JacksonXmlProperty(localName="mch_appid")
    public void setMchAppid(final String mch_appid) {
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
	@JacksonXmlProperty(localName="sign")
	public String getSign() {
		return sign;
	}
	@JacksonXmlProperty(localName="sign")
	public void setSign(final String sign) {
		this.sign = sign;
	}
	@JacksonXmlProperty(localName="partner_trade_no")
	public String getPartnerTradeNo() {
		return partner_trade_no;
	}
	@JacksonXmlProperty(localName="partner_trade_no")
	public void setPartnerTradeNo(final String partner_trade_no) {
		this.partner_trade_no = partner_trade_no;
	}
    @JacksonXmlProperty(localName="openid")
    public String getOpenid() {
        return openid;
    }
    @JacksonXmlProperty(localName="openid")
    public void setOpenid(final String openid) {
        this.openid = openid;
    }
	@JacksonXmlProperty(localName="check_name")
	public String getCheckName() {
		return check_name;
	}
	@JacksonXmlProperty(localName="check_name")
	public void setCheckName(final String check_name) {
		this.check_name = check_name;
	}
	@JacksonXmlProperty(localName="re_user_name")
	public String getReUserName() {
		return re_user_name;
	}
	@JacksonXmlProperty(localName="re_user_name")
	public void setReUserName(final String re_user_name) {
		this.re_user_name = re_user_name;
	}
	@JacksonXmlProperty(localName="amount")
	public int getAmount() {
		return amount;
	}
	@JacksonXmlProperty(localName="amount")
	public void setAmount(final int amount) {
		this.amount = amount;
	}
	@JacksonXmlProperty(localName="desc")
	public String getDesc() {
		return desc;
	}
	@JacksonXmlProperty(localName="desc")
	public void setDesc(final String desc) {
		this.desc = desc;
	}
	@JacksonXmlProperty(localName="spbill_create_ip")
	public String getSpbillCreateIp() {
		return spbill_create_ip;
	}
	@JacksonXmlProperty(localName="spbill_create_ip")
	public void setSpbillCreateIp(final String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}

    // 商户账号appid    mch_appid   是   wx8888888888888888  String  申请商户号的appid或商户号绑定的appid
    private String mch_appid;
    // 商户号  mchid   是   1900000109  String(32)  微信支付分配的商户号
    private String mchid;
    // 备号   device_info 否   013467007045764 String(32)  微信支付分配的终端设备号
    private String device_info;

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("PromotionTransfersRequest [mch_appid=").append(mch_appid).append(", mchid=").append(mchid)
                .append(", device_info=").append(device_info).append(", nonce_str=").append(nonce_str).append(", sign=")
                .append(sign).append(", partner_trade_no=").append(partner_trade_no).append(", check_name=")
                .append(check_name).append(", wxappid=").append(wxappid).append(", re_user_name=").append(re_user_name)
                .append(", openid=").append(openid).append(", amount=").append(amount).append(", desc=").append(desc)
                .append(", spbill_create_ip=").append(spbill_create_ip).append("]");
        return builder.toString();
    }

    // 随机字符串    nonce_str   是   5K8264ILTKCH16CQ2502SI8ZNMTM67VS    String(32)  随机字符串，不长于32位
    private String nonce_str;

    // 签名   sign    是   C380BEC2BFD727A4B6845133519F3AD6    String(32)  签名，详见签名算法
    private String sign;

    private String partner_trade_no;
    // 校验用户姓名选项    check_name  是   FORCE_CHECK String  NO_CHECK：不校验真实姓名  FORCE_CHECK：强校验真实姓名
    private String check_name;
    private String wxappid;
    // 收款用户姓名   re_user_name    可选  王小王 String  收款用户真实姓名。 如果check_name设置为FORCE_CHECK，则必填用户真实姓名
    private String re_user_name;
    // 用户openid openid  是   oxTWIuGaIt6gTKsQRLau2M0yL16E    String  商户appid下，某用户的openid
    private String openid;
    // 金额   amount  是   10099   int 企业付款金额，单位为分
    private int amount;
    // 企业付款描述信息    desc    是   理赔  String  企业付款操作说明信息。必填。
    private String desc;
    // Ip地址 spbill_create_ip    是   192.168.0.1 String(32)  该IP同在商户平台设置的IP白名单中的IP没有关联，该IP可传用户端或者服务端的IP。
    private String spbill_create_ip;
}
