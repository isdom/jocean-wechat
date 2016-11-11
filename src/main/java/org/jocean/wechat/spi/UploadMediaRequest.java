/**
 * 
 */
package org.jocean.wechat.spi;

import javax.ws.rs.BeanParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.jocean.idiom.AnnotationWrapper;


/**
 * @author isdom
 *
 */
@Path("/media/upload")
@AnnotationWrapper(POST.class)
public class UploadMediaRequest {
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */

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

    public byte[] getBody() {
        return _body;
    }

    public void setBody(final byte[] body) {
        this._body = body;
    }

    public void setContentLength(String contentLength) {
        this._contentLength = contentLength;
    }

    public void setContentType(String contentType) {
        this._contentType = contentType;
    }

    @QueryParam("access_token")
    @AnnotationWrapper(POST.class)
    private String _accessToken;

    @QueryParam("type")
    @AnnotationWrapper(POST.class)
    private String _type;
    
    @HeaderParam("Content-Length")
    private String _contentLength;
    
    @HeaderParam("Content-Type")
    private String _contentType;
    
    @BeanParam
    private byte[] _body;
}
