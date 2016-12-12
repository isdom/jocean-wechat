
package org.jocean.wechat;

import org.jocean.idiom.store.BlobRepo.Blob;

import rx.Observable;

public interface WechatOperation {
    public String getAppid();
    public Observable<String> getAccessToken(final boolean forceRefresh);
    public Observable<String> getJsapiTicket();
    
    public String getAccessToken();
    
    public Observable<Blob> downloadMedia(final String accessToken, final String mediaId);
    
    //  input mediaId (see : https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1444738726&token=&lang=zh_CN)
    //  output Blob : media download
    public Observable<Blob> downloadMedia(final String mediaId);
    
    public Observable<String> uploadMedia(final Blob blob);
}
