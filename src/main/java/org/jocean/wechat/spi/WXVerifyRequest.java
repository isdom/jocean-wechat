package org.jocean.wechat.spi;

import javax.ws.rs.QueryParam;

/**
 * @author isdom
 *
 */
public class WXVerifyRequest {
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("WXVerifyRequest [signature=").append(_signature)
                .append(", timestamp=").append(_timestamp)
                .append(", nonce=").append(_nonce)
                .append(", msg_signature=").append(_msgSignature)
                .append(", echostr=").append(_echostr).append("]");
        return builder.toString();
    }

    public String getSignature() {
        return _signature;
    }

    public void setSignature(final String signature) {
        this._signature = signature;
    }

    public String getTimestamp() {
        return _timestamp;
    }

    public void setTimestamp(final String timestamp) {
        this._timestamp = timestamp;
    }

    public String getNonce() {
        return _nonce;
    }

    public void setNonce(final String nonce) {
        this._nonce = nonce;
    }

    public String getEchostr() {
        return _echostr;
    }

    public void setEchostr(final String echostr) {
        this._echostr = echostr;
    }

    public String getMsgSignature() {
        return _msgSignature;
    }

    public void setMsgSignature(final String msgSignature) {
        this._msgSignature = msgSignature;
    }

    @QueryParam("signature")
    private String  _signature;

    @QueryParam("timestamp")
    private String  _timestamp;

    @QueryParam("nonce")
    private String  _nonce;

    @QueryParam("msg_signature")
    private String  _msgSignature;

    @QueryParam("echostr")
    private String  _echostr;
}
