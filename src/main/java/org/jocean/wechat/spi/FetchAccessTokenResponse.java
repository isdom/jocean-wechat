/**
 * 
 */
package org.jocean.wechat.spi;

import com.alibaba.fastjson.annotation.JSONField;


/**
 * @author isdom
 *
 */
public class FetchAccessTokenResponse extends WechatResponse {



    @JSONField(name="access_token")
    public String getAccessToken() {
        return _accessToken;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("FetchAccessTokenResponse [accessToken=")
                .append(_accessToken).append(", expiresIn=").append(_expiresIn)
                .append(", ").append(super.toString()).append("]");
        return builder.toString();
    }

    @JSONField(name="access_token")
    public void setAccessToken(final String accessToken) {
        this._accessToken = accessToken;
    }

    @JSONField(name="expires_in")
    public String getExpiresIn() {
        return _expiresIn;
    }

    @JSONField(name="expires_in")
    public void setExpiresIn(final String expiresIn) {
        this._expiresIn = expiresIn;
    }

    private String _accessToken;
    
    private String _expiresIn;
}
