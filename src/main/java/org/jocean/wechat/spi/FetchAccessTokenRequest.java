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
@Path("https://api.weixin.qq.com/cgi-bin/token")
public class FetchAccessTokenRequest {

    @Override
    public String toString() {
        return "FetchAccessTokenRequest [_grantType=" + _grantType
                + ", _appid=" + _appid + ", _secret=" + _secret + "]";
    }

    public String getAppid() {
        return _appid;
    }

    public void setAppid(final String appid) {
        this._appid = appid;
    }

    public String getSecret() {
        return _secret;
    }

    public void setSecret(final String secret) {
        this._secret = secret;
    }

    public String getGrantType() {
        return _grantType;
    }

    @QueryParam("grant_type")
    private final String _grantType="client_credential";

    @QueryParam("appid")
    private String _appid;
    
    @QueryParam("secret")
    private String _secret;
}
