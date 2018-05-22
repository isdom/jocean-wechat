package org.jocean.wechat.service;

import java.util.concurrent.TimeUnit;

import org.jocean.http.ContentUtil;
import org.jocean.http.Interact;
import org.jocean.http.MessageUtil;
import org.jocean.http.TransportException;
import org.jocean.idiom.rx.RxObservables;
import org.jocean.idiom.rx.RxObservables.RetryPolicy;
import org.jocean.wechat.WXCommonAPI;
import org.jocean.wechat.WXProtocol;
import org.jocean.wechat.WXProtocol.UserInfoResponse;
import org.jocean.wechat.WXProtocol.WXAPIResponse;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.annotation.JSONField;

import rx.Observable;
import rx.Observable.Transformer;
import rx.functions.Func1;

public class DefaultWXCommonAPI implements WXCommonAPI {

    @Override
    public Func1<Interact, Observable<UserInfoResponse>> getUserInfo(final String accessToken, final String openid) {
        return interact-> {
            try {
                return interact
                    .uri("https://api.weixin.qq.com").path("/cgi-bin/user/info")
                    .paramAsQuery("access_token", accessToken)
                    .paramAsQuery("openid", openid)
                    .paramAsQuery("lang", "zh_CN")
                    .execution()
                    .compose(MessageUtil.responseAs(UserInfoResponse.class, MessageUtil::unserializeAsJson))
                    .compose(timeoutAndRetry())
                    .doOnNext(WXProtocol.CHECK_WXRESP);
            } catch (final Exception e) {
                return Observable.error(e);
            }
        };
    }

    @Override
    public Func1<Interact, Observable<UserInfoResponse>> getSnsUserInfo(final String oauth2Token, final String openid) {
        return interact-> {
            try {
                return interact
                    .uri("https://api.weixin.qq.com").path("/sns/userinfo")
                    .paramAsQuery("access_token", oauth2Token)
                    .paramAsQuery("openid", openid)
                    .paramAsQuery("lang", "zh_CN")
                    .execution()
                    .compose(MessageUtil.responseAs(UserInfoResponse.class, MessageUtil::unserializeAsJson))
                    .compose(timeoutAndRetry())
                    .doOnNext(WXProtocol.CHECK_WXRESP);
            } catch (final Exception e) {
                return Observable.error(e);
            }
        };
    }

    static class CustomMessageReq {
        static class Text {
            @JSONField(name = "content")
            public String getCntent() {
                return this._content;
            }

            @JSONField(name = "content")
            public void setcContent(final String content) {
                this._content = content;
            }

            private String _content;
        }

        @JSONField(name = "touser")
        public String getToUser() {
            return this._touser;
        }

        @JSONField(name = "touser")
        public void setToUser(final String touser) {
            this._touser = touser;
        }

        @JSONField(name = "msgtype")
        public String getMsgType() {
            return this._msgtype;
        }

        @JSONField(name = "msgtype")
        public void setMsgType(final String msgtype) {
            this._msgtype = msgtype;
        }

        @JSONField(name = "text")
        public Text getText() {
            return this._text;
        }

        @JSONField(deserialize = false)
        public void setTextContent(final String content) {
            this._text = new Text();
            this._text.setcContent(content);
        }

        private String _touser;
        private String _msgtype;
        private Text _text;
    }

    @Override
    public Func1<Interact, Observable<WXAPIResponse>> sendCustomMessageInText(final String accessToken,
            final String openid, final String content) {
        return interact-> {
            try {
                final CustomMessageReq req = new CustomMessageReq();
                req.setToUser(openid);
                req.setMsgType("text");
                req.setTextContent(content);
                return interact
                    .uri("https://api.weixin.qq.com").path("/cgi-bin/message/custom/send")
                    .paramAsQuery("access_token", accessToken)
                    .body(req, ContentUtil.TOJSON)
                    .execution()
                    .compose(MessageUtil.responseAs(WXAPIResponse.class, MessageUtil::unserializeAsJson))
                    .compose(timeoutAndRetry())
                    .doOnNext(WXProtocol.CHECK_WXRESP);
            } catch (final Exception e) {
                return Observable.error(e);
            }
        };
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

    private <T> Transformer<T, T> timeoutAndRetry() {
        return org -> org.timeout(this._timeoutInMS, TimeUnit.MILLISECONDS).retryWhen(retryPolicy());
    }

    @Value("${api.retrytimes}")
    private final int _maxRetryTimes = 3;

    @Value("${api.retryinterval}")
    private final int _retryIntervalBase = 100; // 100 ms

    @Value("${api.timeoutInMs}")
    private final int _timeoutInMS = 10000;
}
