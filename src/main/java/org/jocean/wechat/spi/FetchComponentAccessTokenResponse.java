/**
 *
 */
package org.jocean.wechat.spi;

import com.alibaba.fastjson.annotation.JSONField;


/**
 * @author isdom
 *
 */
public class FetchComponentAccessTokenResponse extends WechatResponse {

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("FetchAccessTokenResponse [accessToken=")
                .append(_accessToken).append(", expiresIn=").append(_expiresIn)
                .append(", ").append(super.toString()).append("]");
        return builder.toString();
    }

    @JSONField(name="component_access_token")
    public String getComponentAccessToken() {
        return _accessToken;
    }

    @JSONField(name="component_access_token")
    public void setComponentAccessToken(final String accessToken) {
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
