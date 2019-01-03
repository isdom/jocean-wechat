package org.jocean.wechat.service;

import org.jocean.http.ContentUtil;
import org.jocean.http.RpcRunner;
import org.jocean.wechat.WXCommonAPI;
import org.jocean.wechat.WXProtocol;
import org.jocean.wechat.WXProtocol.UserInfoResponse;
import org.jocean.wechat.WXProtocol.WXAPIResponse;

import com.alibaba.fastjson.annotation.JSONField;

import io.netty.handler.codec.http.HttpMethod;
import rx.Observable;
import rx.Observable.Transformer;

public class DefaultWXCommonAPI implements WXCommonAPI {

    @Override
    public Transformer<RpcRunner, UserInfoResponse> getUserInfo(final String accessToken, final String openid) {
        return runners -> runners.flatMap( runner -> runner.name("wxcommon.getUserInfo").execute(
        interact-> {
            try {
                return interact
                    .uri("https://api.weixin.qq.com")
                    .path("/cgi-bin/user/info")
                    .paramAsQuery("access_token", accessToken)
                    .paramAsQuery("openid", openid)
                    .paramAsQuery("lang", "zh_CN")
                    .responseAs(ContentUtil.ASJSON, UserInfoResponse.class)
                    .doOnNext(WXProtocol.CHECK_WXRESP);
            } catch (final Exception e) {
                return Observable.error(e);
            }
        }));
    }

    @Override
    public Transformer<RpcRunner, UserInfoResponse> getSnsUserInfo(final String oauth2Token, final String openid) {
        return runners -> runners.flatMap( runner -> runner.name("wxcommon.getSnsUserInfo").execute(
        interact-> {
            try {
                return interact
                    .uri("https://api.weixin.qq.com")
                    .path("/sns/userinfo")
                    .paramAsQuery("access_token", oauth2Token)
                    .paramAsQuery("openid", openid)
                    .paramAsQuery("lang", "zh_CN")
                    .responseAs(ContentUtil.ASJSON, UserInfoResponse.class)
                    .doOnNext(WXProtocol.CHECK_WXRESP);
            } catch (final Exception e) {
                return Observable.error(e);
            }
        }));
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
    public Transformer<RpcRunner, WXAPIResponse> sendCustomMessageInText(final String accessToken,
            final String openid, final String content) {
        return runners -> runners.flatMap( runner -> runner.name("wxcommon.sendCustomMessageInText").execute(
        interact-> {
            try {
                final CustomMessageReq req = new CustomMessageReq();
                req.setToUser(openid);
                req.setMsgType("text");
                req.setTextContent(content);
                return interact.method(HttpMethod.POST)
                    .uri("https://api.weixin.qq.com").path("/cgi-bin/message/custom/send")
                    .paramAsQuery("access_token", accessToken)
                    .body(req, ContentUtil.TOJSON)
                    .responseAs(ContentUtil.ASJSON, WXAPIResponse.class)
                    .doOnNext(WXProtocol.CHECK_WXRESP);
            } catch (final Exception e) {
                return Observable.error(e);
            }
        }));
    }
}
