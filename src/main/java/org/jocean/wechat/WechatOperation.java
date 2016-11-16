
package org.jocean.wechat;

import org.jocean.idiom.store.BlobRepo.Blob;

import rx.Observable;

public interface WechatOperation {
    public String getAppid();
    public Observable<String> getAccessToken(final boolean forceRefresh);
    public Observable<String> getJsapiTicket();
    
    public String getAccessToken();
    
    public Observable<Blob> downloadMedia(final String mediaId);
}
