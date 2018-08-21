package org.jocean.wechat.spi;

import javax.ws.rs.Consumes;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@Consumes({"application/xml","text/xml"})
@JacksonXmlRootElement(localName="xml")
public class UnifiedOrderResponse extends PayBaseResponse {

    //  预支付交易会话标识   prepay_id   是   String(64)  wx201410272009395522657a690389285100    微信生成的预支付回话标识，用于后续接口调用中使用，该值有效期为2小时
    @JacksonXmlProperty(localName="prepay_id")
    public String getPrepayId() {
        return prepay_id;
    }

    //  预支付交易会话标识   prepay_id   是   String(64)  wx201410272009395522657a690389285100    微信生成的预支付回话标识，用于后续接口调用中使用，该值有效期为2小时
    @JacksonXmlProperty(localName="prepay_id")
    public void setPrepayId(final String prepay_id) {
        this.prepay_id = prepay_id;
    }

    private String prepay_id;

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("UnifiedOrderResponse [prepay_id=").append(prepay_id).append(", return_code=")
                .append(return_code).append(", return_msg=").append(return_msg).append(", appid=").append(appid)
                .append(", mch_id=").append(mch_id).append(", nonce_str=").append(nonce_str).append(", sign=")
                .append(sign).append(", result_code=").append(result_code).append(", err_code=").append(err_code)
                .append(", err_code_des=").append(err_code_des).append(", device_info=").append(device_info)
                .append("]");
        return builder.toString();
    }
}
