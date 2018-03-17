
package org.jocean.wechat;

import org.jocean.http.Interact;
import org.jocean.wechat.spi.Code2SessionResponse;

import rx.Observable;
import rx.functions.Func1;

public interface WechatMinaAPI {
    
    public String getName();
    
    public String getAppid();
    
    public Func1<Interact, Observable<Code2SessionResponse>> code2session(final String code);
}
