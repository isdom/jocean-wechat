package org.jocean.wechat;

import org.jocean.wechat.spi.OAuthAccessTokenResponse;

import rx.Observable;

public interface WXTokenSource {
    public String getAppid();
    
    public Observable<String> getAccessToken(final boolean forceRefresh);
    
    public Observable<OAuthAccessTokenResponse> getOAuthAccessToken(final String code);
}
