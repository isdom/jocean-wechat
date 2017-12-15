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
@Path("https://api.weixin.qq.com/cgi-bin/media/get")
public class DownloadMediaRequest {
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DownloadMediaRequest [access_token=")
                .append(_accessToken).append(", media_id=").append(_mediaId)
                .append("]");
        return builder.toString();
    }

    public String getAccessToken() {
        return _accessToken;
    }

    public void setAccessToken(final String accessToken) {
        this._accessToken = accessToken;
    }

    public String getMediaId() {
        return _mediaId;
    }

    public void setMediaId(final String mediaId) {
        this._mediaId = mediaId;
    }

    @QueryParam("access_token")
    private String _accessToken;

    @QueryParam("media_id")
    private String _mediaId;
    
}
