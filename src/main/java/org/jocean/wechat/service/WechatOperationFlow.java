/**
 *
 */
package org.jocean.wechat.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;

import org.jocean.event.api.AbstractFlow;
import org.jocean.event.api.BizStep;
import org.jocean.event.api.EventUtils;
import org.jocean.event.api.annotation.OnEvent;
import org.jocean.http.Feature;
import org.jocean.http.TransportException;
import org.jocean.http.rosa.SignalClient;
import org.jocean.idiom.ExceptionUtils;
import org.jocean.idiom.rx.RxObservables;
import org.jocean.idiom.rx.RxObservables.RetryPolicy;
import org.jocean.j2se.jmx.MBeanRegister;
import org.jocean.j2se.jmx.MBeanRegisterAware;
import org.jocean.j2se.jmx.MBeanUtil;
import org.jocean.netty.BlobRepo.Blob;
import org.jocean.wechat.WechatOperation;
import org.jocean.wechat.spi.DownloadMediaRequest;
import org.jocean.wechat.spi.DownloadMediaResponse;
import org.jocean.wechat.spi.FetchAccessTokenRequest;
import org.jocean.wechat.spi.FetchAccessTokenResponse;
import org.jocean.wechat.spi.FetchTicketRequest;
import org.jocean.wechat.spi.FetchTicketResponse;
import org.jocean.wechat.spi.UploadMediaRequest;
import org.jocean.wechat.spi.UploadMediaResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import com.google.common.primitives.Bytes;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.ThreadLocalRandom;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;


public class WechatOperationFlow extends AbstractFlow<WechatOperationFlow>
    implements WechatOperation, MBeanRegisterAware {
	
    private static final Logger LOG = 
            LoggerFactory.getLogger(WechatOperationFlow.class);
    
    public Observable<Blob> downloadMedia(final String accessToken, final String mediaId) {
        final DownloadMediaRequest req = new DownloadMediaRequest();
        req.setAccessToken(accessToken);
        req.setMediaId(mediaId);
        
        return _signalClient.interaction()
            .request(req)
            .feature(Feature.ENABLE_LOGGING_OVER_SSL)
            .feature(Feature.ENABLE_COMPRESSOR)
            .feature(new SignalClient.UsingMethod(GET.class))
            .feature(new SignalClient.ConvertResponseTo(DownloadMediaResponse.class))
            .<DownloadMediaResponse>build()
            .retryWhen(retryPolicy())
            .map(new Func1<DownloadMediaResponse, Blob>() {
            @Override
            public Blob call(final DownloadMediaResponse resp) {
                final byte[] content = resp.getMsgbody();
                final String contentType = resp.getContentType();
                return Blob.Util.fromByteArray(content, contentType, null, null);
            }});
    }
    
    @Override
    public Observable<Blob> downloadMedia(final String mediaId) {
        return getAccessToken(false)
            .flatMap(new Func1<String, Observable<Blob>>() {
                @Override
                public Observable<Blob> call(final String accessToken) {
                    return downloadMedia(accessToken, mediaId);
                }});
    }
    
    final static String CONTENT_DISPOSITION = "Content-Disposition";
    
    @Override
    public Observable<String> uploadMedia(final Blob blob) {
        return getAccessToken(false)
            .flatMap(new Func1<String, Observable<String>>() {
                @Override
                public Observable<String> call(final String accessToken) {
                    final UploadMediaRequest req = new UploadMediaRequest();
                    req.setAccessToken(accessToken);
                    
                    final String contentType = blob.contentType();
                    final String type = contentType.startsWith("image") ? "image" : "voice";
                    req.setType(type);
                    final String typeSuffix = contentType.substring(contentType.lastIndexOf('/') + 1);
                    
                    final String multipartDataBoundary = Long.toHexString(ThreadLocalRandom.current().nextLong()).toLowerCase();
                    final String name = "media";
                    
                    final String part = "--" + multipartDataBoundary + "\r\n" +
                                    CONTENT_DISPOSITION + ": form-data; name=\""+ name + "\"; filename=\"ossobj." + typeSuffix + "\"" + "\r\n" +
//                                    CONTENT_LENGTH + ": " + file1.length() + "\r\n" +
                                    HttpHeaderNames.CONTENT_TYPE + ": " + contentType + "\r\n" +
                                    HttpHeaderNames.CONTENT_TRANSFER_ENCODING + ": binary" + "\r\n" +
                                    "\r\n";
                    final String end = "\r\n--" + multipartDataBoundary + "--\r\n";
                    
                    //  TODO, fix by io stream 
                    try(final InputStream is = blob.inputStream()) {
                        req.setBody( 
                            Bytes.concat(part.getBytes(CharsetUtil.UTF_8), 
                                ByteStreams.toByteArray(is),
                                end.getBytes(CharsetUtil.UTF_8))
                                );
                    } catch (IOException e) {
                        LOG.warn("exception when ByteStreams.toByteArray, detail: {}", 
                            ExceptionUtils.exception2detail(e));
                    }
                    
                    req.setContentType("multipart/form-data; boundary=" + multipartDataBoundary);
                    req.setContentLength(Integer.toString(req.getBody().length));
                    
                    return _signalClient.interaction()
                        .request(req)
                        .feature(Feature.ENABLE_LOGGING_OVER_SSL)
                        .feature(Feature.ENABLE_COMPRESSOR)
                        .feature(new SignalClient.UsingMethod(POST.class))
                        .feature(new SignalClient.ConvertResponseTo(UploadMediaResponse.class))
                        .<UploadMediaResponse>build()
                        .retryWhen(retryPolicy())
                        .flatMap(new Func1<UploadMediaResponse, Observable<String>>() {
                            @Override
                            public Observable<String> call(final UploadMediaResponse resp) {
                                final String mediaId = resp.getErrormsg().getMediaid();
                                if (null != mediaId) {
                                    return Observable.just(mediaId);
                                } else {
                                }
                                return Observable.error(new RuntimeException(resp.getErrormsg().toString()));
                            }
                        });
                }
            });
    }
    
    @Override
    public String getAppid() {
        return this._appid;
    }
    

    @Override
    public void setMBeanRegister(MBeanRegister register) {
        register.registerMBean("name=wcop", MBeanUtil.createAndConfigureMBean(this));
    }
    
    @Override
    public Observable<String> getAccessToken(final boolean forceRefresh) {
        return Observable.create(new OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                selfEventReceiver().acceptEvent("onSubscribeAccessToken", subscriber, forceRefresh);
            }});
    }

    @Override
    public String getAccessToken() {
        return getAccessToken(false)
            .timeout(1, TimeUnit.SECONDS)
            .toBlocking()
            .single();
    }
    
    @Override
    public Observable<String> getJsapiTicket() {
        return Observable.create(new OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                selfEventReceiver().acceptEvent("onSubscribeTicket", subscriber);
            }});
    }
    
    public BizStep start() {
        return INIT;
    }
    
    public void stop() {
        selfEventReceiver().acceptEvent("stop");
    }
    
    private final BizStep INIT = new BizStep("weixin.init") {
        @OnEvent(event = "onSubscribeAccessToken")
        private BizStep onSubscribeAccessToken(
                final Subscriber<? super String> subscriber, 
                final boolean forceRefresh) {
            LOG.info("onSubscribeAccessToken {}", subscriber);
            if (isAccessTokenValid() && !forceRefresh) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(_accessToken);
                    subscriber.onCompleted();
                }
                LOG.info("access_token_cached_not_expired, so reuse. cached token {}",
                        _accessToken);
            } else if (isFetchingAccessToken()) {
                handleFetchAccessTokenSubscriber(subscriber);
                LOG.info("access_token_in_fetching, so wait for new token.");
            } else {
                handleFetchAccessTokenSubscriber(subscriber);
                if (forceRefresh) {
                    LOG.info("access_token_force_refresh, so start to fetch. old token {}/expire timestamp {}",
                            _accessToken, new Date(_accessTokenExpireInMs));
                } else {
                    LOG.info("access_token_cached_expired_or_not_cache, so start to fetch. old token {}/expire timestamp {}",
                            _accessToken, new Date(_accessTokenExpireInMs));
                }
                // start to fetch
                startFetchAccessToken();
            }
            return BizStep.CURRENT_BIZSTEP;
        }
        
        @OnEvent(event = "onAccessTokenError")
        private BizStep onAccessTokenError(final Throwable e) {
            LOG.error("onAccessTokenError, detail {}",
                    ExceptionUtils.exception2detail(e));
            clearAccessToken();
            for (Subscriber<? super String> subscriber : _subscribers4accessToken) {
                if (!subscriber.isUnsubscribed()) {
                    try {
                        subscriber.onError(e);
                    } catch (Exception e1) {
                        LOG.warn("exception when invoke {}.onError, detail: {}",
                                subscriber, ExceptionUtils.exception2detail(e1));
                    }
                }
            }
            _subscribers4accessToken.clear();
            return BizStep.CURRENT_BIZSTEP;
        }
            
        @OnEvent(event = "onAccessTokenResponse")
        private BizStep onAccessTokenResponse(final FetchAccessTokenResponse resp) {
            _accessToken = resp.getAccessToken();
            _accessTokenExpireInMs = System.currentTimeMillis() 
                    + (Long.parseLong(resp.getExpiresIn()) - 30) * 1000L;
            LOG.info("on fetch access token response {}, update token {} and expires timestamp in {}", 
                    resp, _accessToken, new Date(_accessTokenExpireInMs));
            for (Subscriber<? super String> subscriber : _subscribers4accessToken) {
                if (!subscriber.isUnsubscribed()) {
                    try {
                        subscriber.onNext(_accessToken);
                        subscriber.onCompleted();
                    } catch (Exception e) {
                        LOG.warn("exception when invoke {}.onNext, detail: {}",
                                subscriber, ExceptionUtils.exception2detail(e));
                    }
                }
            }
            _subscribers4accessToken.clear();
            return BizStep.CURRENT_BIZSTEP;
        }
        
        @OnEvent(event = "onSubscribeTicket")
        private BizStep onSubscribeTicket(final Subscriber<? super String> subscriber) {
            LOG.info("onSubscribeTicket {}", subscriber);
            if (isJsapiTicketValid()) {
                subscriber.onNext(_jsapiTicket);
                subscriber.onCompleted();
                LOG.info("ticket_cached_not_expired, so reuse. cached ticket {}",
                        _jsapiTicket);
            } else if (isFetchingJsapiTicket()) {
                _subscribers4ticket.add(subscriber);
                LOG.info("ticket_in_fetching, so wait for new ticket.");
            } else {
                _subscribers4ticket.add(subscriber);
                // start to fetch
                startFetchTicket();
                LOG.info("ticket_cached_expired_or_not_cache, so start to fetch. old ticket {}/expire timestamp {}",
                        _jsapiTicket, _jsapiTicketExpireInMs);
            }
            return BizStep.CURRENT_BIZSTEP;
        }
        
        @OnEvent(event = "onTicketError")
        private BizStep onTicketError(final Throwable e) {
            LOG.error("onTicketError, detail {}",
                    ExceptionUtils.exception2detail(e));
            _jsapiTicket = null;
            _jsapiTicketExpireInMs = 0;
            for (Subscriber<? super String> subscriber : _subscribers4ticket) {
                subscriber.onError(e);
            }
            _subscribers4ticket.clear();
            return BizStep.CURRENT_BIZSTEP;
        }
            
        @OnEvent(event = "onTicketResponse")
        private BizStep onTicketResponse(final FetchTicketResponse resp) {
            _jsapiTicket = resp.getTicket();
            _jsapiTicketExpireInMs = System.currentTimeMillis() 
                    + (Long.parseLong(resp.getExpiresIn()) - 30) * 1000L;
            LOG.info("on fetch ticket response {}, update ticket {} and expires timestamp in ms {}", 
                    resp, _jsapiTicket, _jsapiTicketExpireInMs);
            for (Subscriber<? super String> subscriber : _subscribers4ticket) {
                subscriber.onNext(_jsapiTicket);
                subscriber.onCompleted();
            }
            _subscribers4ticket.clear();
            return BizStep.CURRENT_BIZSTEP;
        }
        
        @OnEvent(event = "stop")
        private BizStep stop() {
            //  TODO, add unsubcribe
            return null;
        }
    }
    .freeze();
    
    private void startFetchAccessToken() {
        
        this._fetchingAccessToken = true;
        clearAccessToken();
        final FetchAccessTokenRequest fetchAccessTokenReq = new FetchAccessTokenRequest();
        
        fetchAccessTokenReq.setAppid(this._appid);
        fetchAccessTokenReq.setSecret(this._appsecret);
        this._signalClient.<FetchAccessTokenResponse>defineInteraction(fetchAccessTokenReq)
            .doOnTerminate(new Action0(){
                @Override
                public void call() {
                    _fetchingAccessToken = false;
                }})
            .timeout(10, TimeUnit.SECONDS)
            .subscribe(EventUtils.receiver2observer(
                selfEventReceiver(),
                "onAccessTokenResponse",
                "onAccessTokenError"));
    }

    private void clearAccessToken() {
        this._accessToken = null;
        this._accessTokenExpireInMs = 0;
    }
    
    private void startFetchTicket() {
        this._fetchingJsapiTicket = true;
        
        getAccessToken(false).flatMap(new Func1<String, Observable<? extends FetchTicketResponse>>() {
            @Override
            public Observable<? extends FetchTicketResponse> call(final String accessToken) {
                final FetchTicketRequest fetchTicketRequest = new FetchTicketRequest();
                fetchTicketRequest.setAccessToken(accessToken);
                fetchTicketRequest.setType("jsapi");
                return _signalClient.<FetchTicketResponse>defineInteraction(fetchTicketRequest);
            }})
        .doOnTerminate(new Action0() {
            @Override
            public void call() {
                _fetchingJsapiTicket = false;
            }})
        .subscribe(EventUtils.receiver2observer(
            selfEventReceiver(),
            "onTicketResponse",
            "onTicketError"));
    }
    
    private boolean isFetchingAccessToken() {
        return this._fetchingAccessToken;
    }

    private boolean isAccessTokenValid() {
        if ( null == this._accessToken ) {
            return false;
        }
        if ( System.currentTimeMillis() >= this._accessTokenExpireInMs) {
            return false;
        }
        return true;
    }

    private boolean isFetchingJsapiTicket() {
        return this._fetchingJsapiTicket;
    }

    private boolean isJsapiTicketValid() {
        if ( null == this._jsapiTicket ) {
            return false;
        }
        if ( System.currentTimeMillis() >= this._jsapiTicketExpireInMs) {
            return false;
        }
        return true;
    }

    public void setAppid(final String appid) {
        this._appid = appid;
    }

    public void setAppsecret(final String secret) {
        this._appsecret = secret;
    }

    public String getAppsecret() {
        return _appsecret;
    }
    
    public void setMaxRetryTimes(final int maxRetryTimes) {
        this._maxRetryTimes = maxRetryTimes;
    }

    public void setRetryIntervalBase(final int retryIntervalBase) {
        this._retryIntervalBase = retryIntervalBase;
    }
    
    private void handleFetchAccessTokenSubscriber(
            final Subscriber<? super String> subscriber) {
        if (!subscriber.isUnsubscribed()) {
            this._subscribers4accessToken.add(subscriber);
            subscriber.add(Subscriptions.create(new Action0() {
                @Override
                public void call() {
                    //  maybe call from other thread
                    _subscribers4accessToken.remove(subscriber);
                }}));
        }
    }

    private Func1<? super Observable<? extends Throwable>, ? extends Observable<?>> retryPolicy() {
        return RxObservables.retryWith(new RetryPolicy<Integer>() {
            @Override
            public Observable<Integer> call(final Observable<Throwable> errors) {
                return errors.compose(RxObservables.retryIfMatch(TransportException.class))
                        .compose(RxObservables.retryMaxTimes(_maxRetryTimes))
                        .compose(RxObservables.retryDelayTo(_retryIntervalBase))
                        ;
            }});
    }

    @Inject
    private SignalClient _signalClient;
    
    private String _appid;
    
    private String _appsecret;
    
    private String _accessToken;
    
    private long _accessTokenExpireInMs = 0;
    
    private final List<Subscriber<? super String>> _subscribers4accessToken = Lists.newCopyOnWriteArrayList();
    
    private boolean _fetchingAccessToken = false;
    
    private String _jsapiTicket;

    private long _jsapiTicketExpireInMs = 0;
    
    private final List<Subscriber<? super String>> _subscribers4ticket = Lists.newCopyOnWriteArrayList();
    
    private boolean _fetchingJsapiTicket = false;
    
    private int _maxRetryTimes = 3;
    private int _retryIntervalBase = 2;
}

/*
获取access token
access_token是公众号的全局唯一票据，公众号调用各接口时都需使用access_token。开发者需要进行妥善保存。access_token的存储至少
    要保留512个字符空间。access_token的有效期目前为2个小时，需定时刷新，重复获取将导致上次获取的access_token失效。

公众平台的API调用所需的access_token的使用及生成方式说明：

1、为了保密appsecrect，第三方需要一个access_token获取和刷新的中控服务器。而其他业务逻辑服务器所使用的access_token均来自于
    该中控服务器，不应该各自去刷新，否则会造成access_token覆盖而影响业务；
2、目前access_token的有效期通过返回的expire_in来传达，目前是7200秒之内的值。中控服务器需要根据这个有效时间提前去刷新新
    access_token。在刷新过程中，中控服务器对外输出的依然是老access_token，此时公众平台后台会保证在刷新短时间内，
    新老access_token都可用，这保证了第三方业务的平滑过渡；
3、access_token的有效时间可能会在未来有调整，所以中控服务器不仅需要内部定时主动刷新，还需要提供被动刷新access_token的接口，
    这样便于业务服务器在API调用获知access_token已超时的情况下，可以触发access_token的刷新流程。
    
如果第三方不使用中控服务器，而是选择各个业务逻辑点各自去刷新access_token，那么就可能会产生冲突，导致服务不稳定。

公众号可以使用AppID和AppSecret调用本接口来获取access_token。AppID和AppSecret可在微信公众平台官网-开发者中心页中获得
    （需要已经成为开发者，且帐号没有异常状态）。注意调用所有微信接口时均需使用https协议。

接口调用请求说明

http请求方式: GET
https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
参数说明

参数  是否必须    说明
grant_type  是   获取access_token填写client_credential
appid   是   第三方用户唯一凭证
secret  是   第三方用户唯一凭证密钥，即appsecret
返回说明

正常情况下，微信会返回下述JSON数据包给公众号：

{"access_token":"ACCESS_TOKEN","expires_in":7200}
参数  说明
access_token    获取到的凭证
expires_in  凭证有效时间，单位：秒

错误时微信会返回错误码等信息，JSON数据包示例如下（该示例为AppID无效错误）:

{"errcode":40013,"errmsg":"invalid appid"}
*/
