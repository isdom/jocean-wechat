package org.jocean.wechat.spi;

import com.alibaba.fastjson.annotation.JSONField;

public class Code2SessionResponse {

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Code2SessionResponse [errcode=").append(errcode)
                .append(", errmsg=").append(errmsg)
                .append(", openid=").append(openid)
                .append(", session_key=").append(session_key)
                .append(", unionid=").append(unionid)
                .append("]");
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

    @JSONField(name="openid")
    public String getOpenid() {
        return openid;
    }

    @JSONField(name="openid")
    public void setOpenid(final String openid) {
        this.openid = openid;
    }

    @JSONField(name="session_key")
    public String getSessionkey() {
        return session_key;
    }

    @JSONField(name="session_key")
    public void setSessionkey(String session_key) {
        this.session_key = session_key;
    }


    @JSONField(name="unionid")
    public String getUnionid() {
        return unionid;
    }

    @JSONField(name="unionid")
    public void setUnionid(final String unionid) {
        this.unionid = unionid;
    }

    private String errcode;

    private String errmsg;

    private String openid;

    private String session_key;

    private String unionid;
}
