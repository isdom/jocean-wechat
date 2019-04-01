package org.jocean.wechat.spi;

import javax.ws.rs.Consumes;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@Consumes({"application/xml", "text/xml"})
@JacksonXmlRootElement(localName = "xml")
public class RefundResponse extends OrderResponse {
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

    @JacksonXmlProperty(localName = "out_refund_no")
    public String getOutRefundNo() {
        return outRefundNo;
    }

    @JacksonXmlProperty(localName = "out_refund_no")
    public void setOutRefundNo(final String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    @JacksonXmlProperty(localName = "refund_id")
    public String getRefundId() {
        return refundId;
    }

    @JacksonXmlProperty(localName = "refund_id")
    public void setRefundId(final String refundId) {
        this.refundId = refundId;
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

    private String transactionId; //微信订单号

    private String outTradeNo;// 商户订单号

    private String outRefundNo; //商户退款单号

    private String refundId;//微信退款单号

    private Integer refundFee;//退款金额

    private Integer totalFee; //订单总金额


}
