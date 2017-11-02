package org.jocean.wechat;

import rx.Observable;

public interface WXTokenSource {
    public String getAppid();
    
    public Observable<String> getAccessToken(final boolean forceRefresh);
}
