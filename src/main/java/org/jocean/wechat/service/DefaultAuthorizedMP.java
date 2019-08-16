package org.jocean.wechat.service;

import org.jocean.wechat.AuthorizedMP;
import org.springframework.beans.factory.annotation.Value;

public class DefaultAuthorizedMP implements AuthorizedMP {

    @Override
    public String getComponentAppid() {
        return this._componentAppid;
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
        return this._refreshToken;
    }

    @Override
    public long getAccessTokenExpireInMs() {
        return this._tokenExpireInMs;
    }

    @Override
    public String getJsapiTicket() {
        return this._jsapiTicket;
    }

    @Value("${wxopen.appid}")
    String _componentAppid;

    @Value("${mp.appid}")
    String _appid;

    @Value("${mp.token}")
    String _token;

    @Value("${mp.token.expire}")
    long _tokenExpireInMs;

    @Value("${mp.refreshToken}")
    String _refreshToken;

    @Value("${mp.jsapiTicket}")
    String _jsapiTicket;
}
