package org.jocean.wechat.spi;

import javax.ws.rs.QueryParam;

public class UserInfoRequest {
    @QueryParam("access_token")
    private  String accessToken;

    @QueryParam("openid")
    private String openid;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(final String accessToken) {
        this.accessToken = accessToken;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(final String openid) {
        this.openid = openid;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("UserInfoRequest [accessToken=").append(accessToken)
            .append(", openid=").append(openid)
            .append("]");
        return builder.toString();
    }

    
}
