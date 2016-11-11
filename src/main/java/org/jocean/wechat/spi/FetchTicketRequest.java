/**
 * 
 */
package org.jocean.wechat.spi;

import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;


/**
 * @author isdom
 *
 */
@Path("/ticket/getticket")
public class FetchTicketRequest {

    @Override
    public String toString() {
        return "FetchTicketRequest [_accessToken=" + _accessToken + ", _type="
                + _type + "]";
    }

    public String getAccessToken() {
        return _accessToken;
    }

    public void setAccessToken(final String accessToken) {
        this._accessToken = accessToken;
    }

    public String getType() {
        return _type;
    }

    public void setType(final String type) {
        this._type = type;
    }

    @QueryParam("access_token")
    private String _accessToken;

    @QueryParam("type")
    private String _type;
    
}
