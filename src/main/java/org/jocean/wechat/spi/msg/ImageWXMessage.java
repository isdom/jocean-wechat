package org.jocean.wechat.spi.msg;

import com.alibaba.fastjson.annotation.JSONField;

public class ImageWXMessage extends BaseWXMessage {

    @JSONField(name="PicUrl")
    public String getPicUrl() {
        return _picUrl;
    }

    @JSONField(name="PicUrl")
    public void setPicUrl(final String picUrl) {
        this._picUrl = picUrl;
    }

    @JSONField(name="MediaId")
    public String getMediaId() {
        return _mediaId;
    }

    @JSONField(name="MediaId")
    public void setMediaId(final String mediaId) {
        this._mediaId = mediaId;
    }

    private String  _picUrl;
    private String  _mediaId;

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ImageWXMessage [picUrl=").append(_picUrl).append(", mediaId=").append(_mediaId)
            .append(", base=").append(super.toString()).append("]");
        return builder.toString();
    }
}
