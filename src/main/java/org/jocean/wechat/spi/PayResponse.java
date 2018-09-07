package org.jocean.wechat.spi;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class PayResponse {

    @JacksonXmlProperty(localName = "return_code")
    public String getReturnCode() {
        return return_code;
    }

    @JacksonXmlProperty(localName = "return_code")
    public void setReturnCode(final String return_code) {
        this.return_code = return_code;
    }

    @JacksonXmlProperty(localName = "return_msg")
    public String getReturnMsg() {
        return return_msg;
    }

    @JacksonXmlProperty(localName = "return_msg")
    public void setReturnMsg(final String return_msg) {
        this.return_msg = return_msg;
    }

    @JacksonXmlProperty(localName = "result_code")
    public String getResultCode() {
        return result_code;
    }

    @JacksonXmlProperty(localName = "result_code")
    public void setResultCode(final String result_code) {
        this.result_code = result_code;
    }

    @JacksonXmlProperty(localName = "err_code")
    public String getErrCode() {
        return err_code;
    }

    @JacksonXmlProperty(localName = "err_code")
    public void setErrCode(final String err_code) {
        this.err_code = err_code;
    }

    @JacksonXmlProperty(localName = "err_code_des")
    public String getErrCodeDes() {
        return err_code_des;
    }

    @JacksonXmlProperty(localName = "err_code_des")
    public void setErrCodeDes(final String err_code_des) {
        this.err_code_des = err_code_des;
    }

    protected String return_code;
    protected String return_msg;
    protected String result_code;
    protected String err_code;
    protected String err_code_des;

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("PayResponse [return_code=").append(return_code).append(", return_msg=").append(return_msg)
                .append(", result_code=").append(result_code).append(", err_code=").append(err_code)
                .append(", err_code_des=").append(err_code_des).append("]");
        return builder.toString();
    }
}