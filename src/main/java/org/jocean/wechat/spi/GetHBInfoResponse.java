package org.jocean.wechat.spi;

import java.util.List;

import javax.ws.rs.Consumes;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@Consumes({"application/xml","text/xml"})
@JacksonXmlRootElement(localName="xml")
public class GetHBInfoResponse extends PayResponse {
    // 商户订单号    mch_billno  是   10000098201411111234567890  String(28)  商户使用查询API填写的商户单号的原路返回
    private String mch_billno;

    // 商户号  mch_id  是   10000098    String(32)  微信支付分配的商户号
    private String mch_id;

    // 红包单号 detail_id   是   1000000000201503283103439304    String(32)  使用API发放现金红包时返回的红包单号
    private String detail_id;

    // 包状态  status  是   RECEIVED    string(16)
    /*
    SENDING:发放中
    SENT:已发放待领取
    FAILED：发放失败
    RECEIVED:已领取
    RFUND_ING:退款中
    REFUND:已退款
    */
    private String status;

    // 发放类型 send_type   是   API String(32)  API:通过API接口发放  UPLOAD:通过上传文件方式发放  ACTIVITY:通过活动方式发放
    private String send_type;

    // 红包类型 hb_type 是   GROUP   String(32)  GROUP:裂变红包  NORMAL:普通红包
    private String hb_type;

    // 红包个数 total_num   是   1   int 红包个数
    private Integer total_num;

    // 红包金额 total_amount    是   5000    int 红包总金额（单位分）
    private Integer total_amount;

    // 失败原因 reason  否   余额不足    String(32)  发送失败原因
    private String reason;

    // 红包发送时间   send_time   是   2015-04-21 20:00:00 String(32)
    private String  send_time;

    // 红包退款时间   refund_time 否   2015-04-21 23:03:00 String(32)  红包的退款时间（如果其未领取的退款
    private String refund_time;

    // 红包退款金额   refund_amount   否   8000    Int 红包退款金额
    private Integer refund_amount;

    // 祝福语  wishing 否   新年快乐    String(128) 祝福语
    private String wishing;

    // 活动描述 remark  否   新年红包    String(256) 活动描述，低版本微信可见
    private String remark;

    // 动名称  act_name    否   新年红包    String(32)  发红包的活动名称
    private String act_name;

    // 裂变红包领取列表 hblist  否   内容如下表       裂变红包的领取列表
    private List<HbInfo>  hblist;

    @JacksonXmlRootElement(localName="hbinfo")
    public static class HbInfo {
        // 领取红包的Openid  openid  是   ohO4GtzOAAYMp2yapORH3dQB3W18    String(32)  领取红包的openid
        private String  openid;

        // 金额   amount  是   100 int 领取金额
        private int  amount;

        // 接收时间 rcv_time    是   2015-04-21 20:00:00 String(32)  领取红包的时间
        private String  rcv_time;

        @JacksonXmlProperty(localName="openid")
        public String getOpenid() {
            return openid;
        }

        @JacksonXmlProperty(localName="openid")
        public void setOpenid(final String openid) {
            this.openid = openid;
        }
        @JacksonXmlProperty(localName="amount")
        public int getAmount() {
            return amount;
        }

        @JacksonXmlProperty(localName="amount")
        public void setAmount(final int amount) {
            this.amount = amount;
        }
        @JacksonXmlProperty(localName="rcv_time")
        public String getRcv_time() {
            return rcv_time;
        }

        @JacksonXmlProperty(localName="rcv_time")
        public void setRcv_time(final String rcv_time) {
            this.rcv_time = rcv_time;
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append("HbInfo [openid=").append(openid).append(", amount=").append(amount).append(", rcv_time=")
                    .append(rcv_time).append("]");
            return builder.toString();
        }
    }

    @JacksonXmlProperty(localName="mch_billno")
    public String getMchBillno() {
        return mch_billno;
    }

    @JacksonXmlProperty(localName="mch_billno")
    public void setMchBillno(final String mch_billno) {
        this.mch_billno = mch_billno;
    }

    @JacksonXmlProperty(localName="mch_id")
    public String getMchId() {
        return mch_id;
    }

    @JacksonXmlProperty(localName="mch_id")
    public void setMchId(final String mch_id) {
        this.mch_id = mch_id;
    }

    @JacksonXmlProperty(localName="detail_id")
    public String getDetailId() {
        return detail_id;
    }

    @JacksonXmlProperty(localName="detail_id")
    public void setDetailId(final String detail_id) {
        this.detail_id = detail_id;
    }

    @JacksonXmlProperty(localName="status")
    public String getStatus() {
        return status;
    }

    @JacksonXmlProperty(localName="status")
    public void setStatus(final String status) {
        this.status = status;
    }

    @JacksonXmlProperty(localName="send_type")
    public String getSendType() {
        return send_type;
    }

    @JacksonXmlProperty(localName="send_type")
    public void setSendType(final String send_type) {
        this.send_type = send_type;
    }

    @JacksonXmlProperty(localName="hb_type")
    public String getHbType() {
        return hb_type;
    }

    @JacksonXmlProperty(localName="hb_type")
    public void setHbType(final String hb_type) {
        this.hb_type = hb_type;
    }

    @JacksonXmlProperty(localName="total_num")
    public Integer getTotalNum() {
        return total_num;
    }

    @JacksonXmlProperty(localName="total_num")
    public void setTotalNum(final Integer total_num) {
        this.total_num = total_num;
    }

    @JacksonXmlProperty(localName="total_amount")
    public Integer getTotalAmount() {
        return total_amount;
    }

    @JacksonXmlProperty(localName="total_amount")
    public void setTotalAmount(final Integer total_amount) {
        this.total_amount = total_amount;
    }

    @JacksonXmlProperty(localName="reason")
    public String getReason() {
        return reason;
    }

    @JacksonXmlProperty(localName="reason")
    public void setReason(final String reason) {
        this.reason = reason;
    }

    @JacksonXmlProperty(localName="send_time")
    public String getSendTime() {
        return send_time;
    }

    @JacksonXmlProperty(localName="send_time")
    public void setSendTime(final String send_time) {
        this.send_time = send_time;
    }

    @JacksonXmlProperty(localName="refund_time")
    public String getRefundTime() {
        return refund_time;
    }

    @JacksonXmlProperty(localName="refund_time")
    public void setRefundTime(final String refund_time) {
        this.refund_time = refund_time;
    }

    @JacksonXmlProperty(localName="refund_amount")
    public Integer getRefundAmount() {
        return refund_amount;
    }

    @JacksonXmlProperty(localName="refund_amount")
    public void setRefundAmount(final Integer refund_amount) {
        this.refund_amount = refund_amount;
    }

    @JacksonXmlProperty(localName="wishing")
    public String getWishing() {
        return wishing;
    }

    @JacksonXmlProperty(localName="wishing")
    public void setWishing(final String wishing) {
        this.wishing = wishing;
    }

    @JacksonXmlProperty(localName="remark")
    public String getRemark() {
        return remark;
    }

    @JacksonXmlProperty(localName="remark")
    public void setRemark(final String remark) {
        this.remark = remark;
    }

    @JacksonXmlProperty(localName="act_name")
    public String getActName() {
        return act_name;
    }

    @JacksonXmlProperty(localName="act_name")
    public void setActName(final String act_name) {
        this.act_name = act_name;
    }

    @JacksonXmlProperty(localName="hblist")
    public List<HbInfo> getHblist() {
        return hblist;
    }

    @JacksonXmlProperty(localName="hblist")
    public void setHblist(final List<HbInfo> hblist) {
        this.hblist = hblist;
    }

    @Override
    public String toString() {
        final int maxLen = 10;
        final StringBuilder builder = new StringBuilder();
        builder.append("GetHBInfoResponse [mch_billno=").append(mch_billno).append(", mch_id=").append(mch_id)
                .append(", detail_id=").append(detail_id).append(", status=").append(status).append(", send_type=")
                .append(send_type).append(", hb_type=").append(hb_type).append(", total_num=").append(total_num)
                .append(", total_amount=").append(total_amount).append(", reason=").append(reason)
                .append(", send_time=").append(send_time).append(", refund_time=").append(refund_time)
                .append(", refund_amount=").append(refund_amount).append(", wishing=").append(wishing)
                .append(", remark=").append(remark).append(", act_name=").append(act_name).append(", hblist=")
                .append(hblist != null ? hblist.subList(0, Math.min(hblist.size(), maxLen)) : null)
                .append(", super=").append(super.toString()).append("]");
        return builder.toString();
    }

}
