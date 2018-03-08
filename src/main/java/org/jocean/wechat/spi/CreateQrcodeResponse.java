package org.jocean.wechat.spi;

import com.alibaba.fastjson.annotation.JSONField;

public class CreateQrcodeResponse {

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("CreateQrcodeResponse [errcode=").append(errcode).append(", errmsg=").append(errmsg)
                .append(", ticket=").append(ticket).append(", expire_seconds=").append(expire_seconds).append(", url=")
                .append(url).append("]");
        return builder.toString();
    }

    @JSONField(name="errcode")
    public String getErrcode() {
        return errcode;
    }

    @JSONField(name="errcode")
    public void setErrcode(final String errcode) {
        this.errcode = errcode;
    }

    @JSONField(name="errmsg")
    public String getErrmsg() {
        return errmsg;
    }

    @JSONField(name="errmsg")
    public void setErrmsg(final String errmsg) {
        this.errmsg = errmsg;
    }

    @JSONField(name="ticket")
    public String getTicket() {
        return ticket;
    }

    @JSONField(name="ticket")
    public void setTicket(final String ticket) {
        this.ticket = ticket;
    }

    @JSONField(name="expire_seconds")
    public int getExpireSeconds() {
        return expire_seconds;
    }

    @JSONField(name="expire_seconds")
    public void setExpireSeconds(final int expire_seconds) {
        this.expire_seconds = expire_seconds;
    }


    @JSONField(name="url")
    public String getUrl() {
        return url;
    }

    @JSONField(name="url")
    public void setUrl(final String url) {
        this.url = url;
    }

    private String errcode;

    private String errmsg;

    private String ticket;

    private int expire_seconds;

    private String url;
}
