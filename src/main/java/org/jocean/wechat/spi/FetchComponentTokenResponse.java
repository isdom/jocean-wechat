/**
 *
 */
package org.jocean.wechat.spi;

import org.jocean.wechat.WXProtocol.WXAPIResponse;

import com.alibaba.fastjson.annotation.JSONField;


/**
 * @author isdom
 *
 */
public interface FetchComponentTokenResponse extends WXAPIResponse {

    @JSONField(name="component_access_token")
    public String getComponentToken();

    @JSONField(name="component_access_token")
    public void setComponentToken(final String accessToken);

    @JSONField(name="expires_in")
    public String getExpiresIn();

    @JSONField(name="expires_in")
    public void setExpiresIn(final String expiresIn);
}
