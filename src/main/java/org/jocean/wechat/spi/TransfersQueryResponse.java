package org.jocean.wechat.spi;

import javax.ws.rs.Consumes;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@Consumes({"application/xml", "text/xml"})
@JacksonXmlRootElement(localName = "xml")
public class TransfersQueryResponse extends PayResponse {
    @JacksonXmlProperty(localName = "appid")
    public String getAppid() {
        return mch_appid;
    }

    @JacksonXmlProperty(localName = "appid")
    public void setAppid(final String mch_appid) {
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


    @JacksonXmlProperty(localName = "detail_id")
    public String getDetailId() {
        return detail_id;
    }

    @JacksonXmlProperty(localName = "detail_id")
    public void setDetailId(final String detail_id) {
        this.detail_id = detail_id;
    }

    @JacksonXmlProperty(localName = "status")
    public String getStatus() {
        return status;
    }

    @JacksonXmlProperty(localName = "status")
    public void setStatus(final String status) {
        this.status = status;
    }

    @JacksonXmlProperty(localName = "reason")
    public String getReason() {
        return reason;
    }

    @JacksonXmlProperty(localName = "reason")
    public void setReason(final String reason) {
        this.reason = reason;
    }

    @JacksonXmlProperty(localName = "openid")
    public String getOpenid() {
        return openid;
    }

    @JacksonXmlProperty(localName = "openid")
    public void setOpenid(final String openid) {
        this.openid = openid;
    }

    @JacksonXmlProperty(localName = "transfer_name")
    public String getTransferName() {
        return transfer_name;
    }

    @JacksonXmlProperty(localName = "transfer_name")
    public void setTransferName(final String transfer_name) {
        this.transfer_name = transfer_name;
    }

    @JacksonXmlProperty(localName = "payment_amount")
    public Integer getPaymentAmount() {
        return payment_amount;
    }

    @JacksonXmlProperty(localName = "payment_amount")
    public void setPaymentAmount(final Integer payment_amount) {
        this.payment_amount = payment_amount;
    }

    @JacksonXmlProperty(localName = "transfer_time")
    public String getTransferTime() {
        return transfer_time;
    }

    @JacksonXmlProperty(localName = "transfer_time")
    public void setTransferTime(final String transfer_time) {
        this.transfer_time = transfer_time;
    }

    @JacksonXmlProperty(localName = "payment_time")
    public String getPaymentTime() {
        return payment_time;
    }

    @JacksonXmlProperty(localName = "payment_time")
    public void setPaymentTime(final String payment_time) {
        this.payment_time = payment_time;
    }

    @JacksonXmlProperty(localName = "desc")
    public String getDesc() {
        return desc;
    }

    @JacksonXmlProperty(localName = "desc")
    public void setDesc(final String desc) {
        this.desc = desc;
    }

    // 商户appid mch_appid   是   wx8888888888888888  String  申请商户号的appid或商户号绑定的appid（企业号corpid即为此appId）
    protected String mch_appid;

    // 商户号  mchid   是   1900000109  String(32)  微信支付分配的商户号
    protected String mchid;

    //SUCCESS:转账成功
    //
    //FAILED:转账失败
    //
    //PROCESSING:处理中
    private String status;

    //调用企业付款API时，微信系统内部产生的单号
    private String detail_id;

    //如果失败则有失败原因
    private String reason;

    //
    private String openid;

    //收款用户姓名
    private String transfer_name;

    //付款金额单位为“分”
    private Integer payment_amount;

    //发起转账的时间
    private String transfer_time;
    //企业付款成功时间
    private String payment_time;
    //企业付款备注
    private String desc;

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("PromotionTransfersResponse [appid=").append(mch_appid).append(", mchid=").append(mchid)
                .append(", detail_id=").append(detail_id).append(", reason=").append(reason)
                .append(", openid=").append(openid).append(", transfer_name=").append(transfer_name)
                .append(", payment_amount=").append(payment_amount).append(", transfer_time=").append(transfer_time)
                .append(", desc=").append(desc)
                .append(", payment_time=").append(payment_time).append(", return_code=").append(return_code)
                .append(", return_msg=").append(return_msg).append(", result_code=").append(result_code)
                .append(", err_code=").append(err_code).append(", err_code_des=").append(err_code_des).append("]");
        return builder.toString();
    }
}

