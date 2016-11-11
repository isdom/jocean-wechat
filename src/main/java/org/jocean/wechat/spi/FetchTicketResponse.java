/**
 * 
 */
package org.jocean.wechat.spi;

import com.alibaba.fastjson.annotation.JSONField;


/**
 * @author isdom
 *
 */
public class FetchTicketResponse extends WechatResponse {

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("FetchTicketResponse [ticket=").append(_ticket)
                .append(", expiresIn=").append(_expiresIn)
                .append(", ").append(super.toString()).append("]");
        return builder.toString();
    }

    @JSONField(name="ticket")
    public String getTicket() {
        return _ticket;
    }

    @JSONField(name="ticket")
    public void setTicket(String ticket) {
        this._ticket = ticket;
    }

    @JSONField(name="expires_in")
    public String getExpiresIn() {
        return _expiresIn;
    }

    @JSONField(name="expires_in")
    public void setExpiresIn(String _expiresIn) {
        this._expiresIn = _expiresIn;
    }

    private String _ticket;
    
    private String _expiresIn;
    
}
