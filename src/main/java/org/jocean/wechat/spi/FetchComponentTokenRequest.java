/**
 *
 */
package org.jocean.wechat.spi;

import javax.ws.rs.Path;

import com.alibaba.fastjson.annotation.JSONField;


/**
 * @author isdom
 *
 */
@Path("https://api.weixin.qq.com/cgi-bin/component/api_component_token")
public class FetchComponentTokenRequest {

    @JSONField(name="component_appid")
    public String getComponentAppid() {
        return this._component_appid;
    }

    @JSONField(name="component_appid")
    public void setComponentAppid(final String appid) {
        this._component_appid = appid;
    }

    @JSONField(name="component_appsecret")
    public String getComponentSecret() {
        return this._component_appsecret;
    }

    @JSONField(name="component_appsecret")
    public void setComponentSecret(final String secret) {
        this._component_appsecret = secret;
    }

    @JSONField(name="component_verify_ticket")
    public String getComponentVerifyTicket() {
        return this._component_verify_ticket;
    }

    @JSONField(name="component_verify_ticket")
    public void setComponentVerifyTicket(final String ticket) {
        this._component_verify_ticket = ticket;
    }

    private String _component_appid;

    private String _component_appsecret;

    private String _component_verify_ticket;
}
