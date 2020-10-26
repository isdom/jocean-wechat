package org.jocean.wechat.service;

import org.jocean.wechat.AuthorizedMP;
import org.springframework.beans.factory.annotation.Value;

public class MonopolizedMP implements AuthorizedMP {

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("MonopolizedMP [appid=").append(_appid).append(", token=").append(_token)
                .append(", jsapiTicket=").append(_jsapiTicket).append("]");
        return builder.toString();
    }

    @Override
    public String getComponentAppid() {
        return null;
    }

    @Override
    public String getAppid() {
        return this._appid;
    }

    @Override
    public String getAccessToken() {
        return this._token;
    }

    @Override
    public String getRefreshToken() {
//        return this._refreshToken;
        return null;
    }

    @Override
    public long getAccessTokenExpireInMs() {
//        return this._tokenExpireInMs;
        return 0;
    }

    @Override
    public String getJsapiTicket() {
        return this._jsapiTicket;
    }

    @Value("${mp.appid}")
    String _appid;

    @Value("${mp.token}")
    String _token;

//    @Value("${mp.token.expire}")
//    long _tokenExpireInMs;

//    @Value("${mp.refreshToken}")
//    String _refreshToken;

    @Value("${mp.ticket}")
    String _jsapiTicket;
}
