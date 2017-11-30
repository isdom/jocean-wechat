package org.jocean.wechat.spi;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@Consumes({MediaType.APPLICATION_XML})
@JacksonXmlRootElement(localName="xml")
public class SendRedpackResponse {
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SendRedpackResponse [return_code=").append(return_code).append(", return_msg=")
                .append(return_msg).append(", sign=").append(sign).append(", result_code=").append(result_code)
                .append(", err_code=").append(err_code).append(", err_code_des=").append(err_code_des)
                .append(", mch_billno=").append(mch_billno).append(", mch_id=").append(mch_id).append(", wxappid=")
                .append(wxappid).append(", re_openid=").append(re_openid).append(", total_amount=").append(total_amount)
                .append(", send_listid=").append(send_listid).append("]");
        return builder.toString();
    }
    
    private String return_code;
	private String return_msg;
	private String sign;
	private String result_code;
	private String err_code;
	private String err_code_des;
	private String mch_billno;
	private String mch_id;
	private String wxappid;
	private String re_openid;
	private String total_amount;
	private String send_listid;
	
	@JacksonXmlProperty(localName="return_code")
	public String getReturnCode() {
		return return_code;
	}
	@JacksonXmlProperty(localName="return_code")
	public void setReturnCode(final String return_code) {
		this.return_code = return_code;
	}
	@JacksonXmlProperty(localName="return_msg")
	public String getReturnMsg() {
		return return_msg;
	}
	@JacksonXmlProperty(localName="return_msg")
	public void setReturnMsg(final String return_msg) {
		this.return_msg = return_msg;
	}
	@JacksonXmlProperty(localName="result_code")
	public String getResultCode() {
		return result_code;
	}
	@JacksonXmlProperty(localName="result_code")
	public void setResultCode(final String result_code) {
		this.result_code = result_code;
	}
	@JacksonXmlProperty(localName="err_code")
	public String getErrCode() {
		return err_code;
	}
	@JacksonXmlProperty(localName="err_code")
	public void setErrCode(final String err_code) {
		this.err_code = err_code;
	}
	@JacksonXmlProperty(localName="err_code_des")
	public String getErrCodeDes() {
		return err_code_des;
	}
	@JacksonXmlProperty(localName="err_code_des")
	public void setErrCodeDes(final String err_code_des) {
		this.err_code_des = err_code_des;
	}
	@JacksonXmlProperty(localName="send_listid")
	public String getSendListid() {
		return send_listid;
	}
	@JacksonXmlProperty(localName="send_listid")
	public void setSendListid(final String send_listid) {
		this.send_listid = send_listid;
	}
    @JacksonXmlProperty(localName="total_amount")
    public String getTotalAmount() {
        return total_amount;
    }
	@JacksonXmlProperty(localName="total_amount")
	public void setTotalAmount(final String total_amount) {
		this.total_amount = total_amount;
	}
	
	@JacksonXmlProperty(localName="sign")
	public String getSign() {
		return sign;
	}
	
	@JacksonXmlProperty(localName="sign")
	public void setSign(final String sign) {
		this.sign = sign;
	}
	@JacksonXmlProperty(localName="mch_billno")
	public String getMchBillno() {
		return mch_billno;
		
	}
	@JacksonXmlProperty(localName="mch_billno")
	public void setMchBillno(String mch_billno) {
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
	@JacksonXmlProperty(localName="wxappid")
	public String getWxappid() {
		return wxappid;
	}
	@JacksonXmlProperty(localName="wxappid")
	public void setWxappid(final String wxappid) {
		this.wxappid = wxappid;
	}
	@JacksonXmlProperty(localName="re_openid")
	public String getReOpenid() {
		return re_openid;
	}
	@JacksonXmlProperty(localName="re_openid")
	public void setReOpenid(final String re_openid) {
		this.re_openid = re_openid;
	}
}
