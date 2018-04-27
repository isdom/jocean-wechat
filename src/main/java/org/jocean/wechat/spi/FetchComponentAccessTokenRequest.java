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
public class FetchComponentAccessTokenRequest {

    public String getComponentAppid() {
        return _component_appid;
    }

    public void setComponentAppid(final String appid) {
        this._component_appid = appid;
    }

    public String getComponentSecret() {
        return _component_appsecret;
    }

    public void setComponentSecret(final String secret) {
        this._component_appsecret = secret;
    }

    public String getComponentVerifyTicket() {
        return _component_appsecret;
    }

    public void setComponentVerifyTicket(final String ticket) {
        this._component_verify_ticket = ticket;
    }

    @JSONField(name="component_appid")
    private String _component_appid;

    @JSONField(name="component_appsecret")
    private String _component_appsecret;

    @JSONField(name="component_verify_ticket")
    private String _component_verify_ticket;
}
