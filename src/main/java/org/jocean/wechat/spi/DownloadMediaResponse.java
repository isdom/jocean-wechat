/**
 * 
 */
package org.jocean.wechat.spi;

import java.util.Arrays;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;

import com.alibaba.fastjson.annotation.JSONField;


/**
 * @author isdom
 *
 */
public class DownloadMediaResponse {
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DownloadMediaResponse [contentType=")
                .append(_contentType)
                .append(", contentLength=")
                .append(_contentLength)
                .append(", contentDisposition=")
                .append(_contentDisposition)
                .append(", errormsg=")
                .append(_errormsg)
                .append(", msgbody=")
                .append(Arrays.toString(_msgbody))
                .append("]");
        return builder.toString();
    }

    public String getContentType() {
        return _contentType;
    }

    public byte[] getMsgbody() {
        return _msgbody;
    }

    public Integer getContentLength() {
        return _contentLength;
    }

    public ErrorMsg getErrormsg() {
        return _errormsg;
    }

    public String getContentDisposition() {
        return _contentDisposition;
    }
    
    @HeaderParam("Content-Type")
    private String _contentType;

    @HeaderParam("Content-Length")
    private Integer _contentLength;
    
    @HeaderParam("Content-disposition")
    private String _contentDisposition;


    @BeanParam
    private byte[] _msgbody;
    
    @BeanParam
    private ErrorMsg _errormsg;
    
    @Consumes({"application/json", "text/plain"})
    public static class ErrorMsg {
        
        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append("ErrorMsg [errcode=").append(_errcode)
                    .append(", errmsg=").append(_errmsg).append("]");
            return builder.toString();
        }

        @JSONField(name="errcode")
        public int getErrcode() {
            return _errcode;
        }
        
        @JSONField(name="errcode")
        public void setErrcode(int errcode) {
            this._errcode = errcode;
        }
        
        @JSONField(name="errmsg")
        public String getErrmsg() {
            return _errmsg;
        }
        
        @JSONField(name="errmsg")
        public void setErrmsg(String errmsg) {
            this._errmsg = errmsg;
        }
        
        private int _errcode;
        private String _errmsg;
    }
}
