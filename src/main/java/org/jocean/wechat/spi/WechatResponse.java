package org.jocean.wechat.spi;

import com.alibaba.fastjson.annotation.JSONField;

public class WechatResponse {

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[errmsg=").append(_errmsg)
                .append(", errcode=").append(_errcode).append("]");
        return builder.toString();
    }

    @JSONField(name = "errcode")
    public String getErrcode() {
        return _errcode;
    }

    @JSONField(name = "errcode")
    public void setErrcode(final String errcode) {
        this._errcode = errcode;
    }

    @JSONField(name = "errmsg")
    public String getErrmsg() {
        return _errmsg;
    }

    @JSONField(name = "errmsg")
    public void setErrmsg(final String errmsg) {
        this._errmsg = errmsg;
    }

    protected String _errmsg;
    
    protected String _errcode;
}