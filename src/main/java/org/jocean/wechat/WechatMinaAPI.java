
package org.jocean.wechat;

import org.jocean.http.Interact;
import org.jocean.wechat.spi.Code2SessionResponse;

import rx.Observable;

public interface WechatMinaAPI {
    
    public String getName();
    
    public String getAppid();
    
    public Observable<Code2SessionResponse> code2session(final Interact interact, final String code);
}
