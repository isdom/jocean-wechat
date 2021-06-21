package org.jocean.wechat;

import org.jocean.rpc.annotation.RpcBuilder;
import org.jocean.wechat.spi.FetchComponentTokenResponse;

import rx.Observable;

public interface WechatOpenAPI {
    @RpcBuilder
    interface FetchComponentTokenBuilder {
        Observable<FetchComponentTokenResponse> call();
    }

    public FetchComponentTokenBuilder fetchComponentToken();

}
