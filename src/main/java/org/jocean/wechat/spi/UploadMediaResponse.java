/**
 * 
 */
package org.jocean.wechat.spi;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;

import com.alibaba.fastjson.annotation.JSONField;


/**
 * @author isdom
 *
 */
public class UploadMediaResponse {
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UploadMediaResponse [errormsg=").append(_errormsg)
                .append("]");
        return builder.toString();
    }

    public ErrorMsg getErrormsg() {
        return _errormsg;
    }

    @BeanParam
    private ErrorMsg _errormsg;
    
    @Consumes({"application/json", "text/plain"})
    public static class ErrorMsg {
        
        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("ErrorMsg [errcode=").append(_errcode)
                    .append(", errmsg=").append(_errmsg).append(", type=")
                    .append(_type).append(", media_id=").append(_mediaid)
                    .append(", create_at=").append(_createat).append("]");
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
        
        @JSONField(name="type")
        public String getType() {
            return _type;
        }

        @JSONField(name="type")
        public void setType(String type) {
            this._type = type;
        }

        @JSONField(name="media_id")
        public String getMediaid() {
            return _mediaid;
        }

        @JSONField(name="media_id")
        public void setMediaid(String mediaid) {
            this._mediaid = mediaid;
        }

        @JSONField(name="created_at")
        public Integer getCreateat() {
            return _createat;
        }

        @JSONField(name="created_at")
        public void setCreateat(Integer createat) {
            this._createat = createat;
        }

        private int _errcode = 0;
        private String _errmsg = null;
        
        private String _type;
        private String _mediaid;
        private Integer _createat;
    }
}
