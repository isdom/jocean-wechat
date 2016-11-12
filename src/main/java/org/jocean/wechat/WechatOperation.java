
package org.jocean.wechat;

import rx.Observable;

public interface WechatOperation {
    public String getAppid();
    public Observable<String> getAccessToken(final boolean forceRefresh);
    public Observable<String> getJsapiTicket();
    
    public String getAccessToken();
}
