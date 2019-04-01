package org.jocean.wechat.spi;

import javax.ws.rs.Consumes;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@Consumes({"application/xml", "text/xml"})
@JacksonXmlRootElement(localName = "xml")
public class RefundQueryResponse extends OrderResponse {
    @JacksonXmlProperty(localName = "transaction_id")
    public String getTransactionId() {
        return transactionId;
    }

    @JacksonXmlProperty(localName = "transaction_id")
    public void setTransactionId(final String transactionId) {
        this.transactionId = transactionId;
    }

    @JacksonXmlProperty(localName = "out_trade_no")
    public String getOutTradeNo() {
        return outTradeNo;
    }

    @JacksonXmlProperty(localName = "out_trade_no")
    public void setOutTradeNo(final String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    @JacksonXmlProperty(localName = "refund_fee")
    public Integer getRefundFee() {
        return refundFee;
    }

    @JacksonXmlProperty(localName = "refund_fee")
    public void setRefundFee(final Integer refundFee) {
        this.refundFee = refundFee;
    }

    @JacksonXmlProperty(localName = "total_fee")
    public Integer getTotalFee() {
        return totalFee;
    }

    @JacksonXmlProperty(localName = "total_fee")
    public void setTotalFee(final Integer totalFee) {
        this.totalFee = totalFee;
    }

    @JacksonXmlProperty(localName = "refund_status_0")
    public String getRefund_status_0() {
        return refund_status_0;
    }

    @JacksonXmlProperty(localName = "refund_status_0")
    public void setRefund_status_0(final String refund_status_0) {
        this.refund_status_0 = refund_status_0;
    }

    @JacksonXmlProperty(localName = "refund_fee_0")
    public Integer getRefund_fee_0() {
        return refund_fee_0;
    }

    @JacksonXmlProperty(localName = "refund_fee_0")
    public void setRefund_fee_0(final Integer refund_fee_0) {
        this.refund_fee_0 = refund_fee_0;
    }

    @JacksonXmlProperty(localName = "settlement_refund_fee_0")
    public Integer getSettlement_refund_fee_0() {
        return settlement_refund_fee_0;
    }

    @JacksonXmlProperty(localName = "settlement_refund_fee_0")
    public void setSettlement_refund_fee_0(final Integer settlement_refund_fee_0) {
        this.settlement_refund_fee_0 = settlement_refund_fee_0;
    }

    private String transactionId; //微信订单号

    private String outTradeNo;// 商户订单号

    private Integer refundFee;//退款金额

    private Integer totalFee; //订单总金额

    private String refund_status_0; //退款状态

    private Integer refund_fee_0;

    private Integer settlement_refund_fee_0;


}
