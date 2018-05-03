package org.jocean.wechat.spi;


import javax.ws.rs.Consumes;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419318482&lang=zh_CN
 *
 * <xml>
 * <AppId><![CDATA[appId]]</AppId>
 * <Encrypt><![CDATA[msg_encrypt]]</Encrypt>
 * </xml>
 */

@Consumes({"application/xml","text/xml"})
@JacksonXmlRootElement(localName="xml")
public class EncryptedMessage {

    @JacksonXmlProperty(localName="AppId")
    public String getAppId() {
        return this._appId;
    }

    @JacksonXmlProperty(localName="AppId")
    public void setAppId(final String appId) {
        this._appId = appId;
    }

    @JacksonXmlProperty(localName="Encrypt")
    public String getEncrypt() {
        return this._encrypt;
    }

    @JacksonXmlProperty(localName="Encrypt")
    public void setEncrypt(final String encrypt) {
        this._encrypt = encrypt;
    }


    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("EncryptedMessage [appId=").append(_appId).append(", encrypt=").append(_encrypt).append("]");
        return builder.toString();
    }

    //  第三方平台appid
    private String _appId;

    private String _encrypt;
}
