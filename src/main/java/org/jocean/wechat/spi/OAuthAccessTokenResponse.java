/**
 * 
 */
package org.jocean.wechat.spi;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * {"errcode":40029,"errmsg":"invalid code"}
 *
 * @author bluces
 *
 */

public class OAuthAccessTokenResponse {
    @JSONField(name="errcode")
    private String errcode;

    @JSONField(name="errmsg")
    private String errmsg;

    @JSONField(name="access_token")
    private String accessToken;

    @JSONField(name="refresh_token")
    private String refreshToken;

    @JSONField(name="scope")
    private String scope;

    @JSONField(name="openid")
    private String openid;

    @JSONField(name="expires_in")
    private String _expiresIn;

    @JSONField(name="expires_in")
    public String getExpiresIn() {
        return _expiresIn;
    }

    public void setExpiresIn(String _expiresIn) {
        this._expiresIn = _expiresIn;
    }

    @JSONField(name="errcode")
    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    @JSONField(name="errmsg")
    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    @JSONField(name="access_token")
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @JSONField(name="refresh_token")
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @JSONField(name="scope")
    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @JSONField(name="openid")
    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    @Override
    public String toString() {
        return "OAuthAccessTokenResponse{" +
                "errcode='" + errcode + '\'' +
                ", errmsg='" + errmsg + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", scope='" + scope + '\'' +
                ", openid='" + openid + '\'' +
                '}';
    }
}
