package org.jocean.wechat.service;

import java.util.Arrays;

import javax.ws.rs.core.MediaType;

import org.jocean.http.ByteBufSlice;
import org.jocean.http.ContentUtil;
import org.jocean.http.Interact;
import org.jocean.http.MessageBody;
import org.jocean.http.MessageUtil;
import org.jocean.http.RpcRunner;
import org.jocean.idiom.DisposableWrapper;
import org.jocean.idiom.DisposableWrapperUtil;
import org.jocean.wechat.WXCommonAPI;
import org.jocean.wechat.WXProtocol;
import org.jocean.wechat.WXProtocol.UserInfoResponse;
import org.jocean.wechat.WXProtocol.WXAPIResponse;
import org.jocean.wechat.WXProtocol.WXRespError;

import com.alibaba.fastjson.annotation.JSONField;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.EmptyHttpHeaders;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.PlatformDependent;
import rx.Observable;
import rx.Observable.Transformer;
import rx.functions.Action1;

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
            public String getContent() {
                return this._content;
            }

            @JSONField(name = "content")
            public void setContent(final String content) {
                this._content = content;
            }

            private String _content;
        }

        static class Image {
            @JSONField(name = "media_id")
            public String getMediaId() {
                return this._mediaId;
            }

            @JSONField(name = "media_id")
            public void setMediaId(final String mediaId) {
                this._mediaId = mediaId;
            }

            private String _mediaId;
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

        @JSONField(name = "image")
        public Image getImage() {
            return this._image;
        }

        @JSONField(deserialize = false)
        public void setTextContent(final String content) {
            this._text = new Text();
            this._text.setContent(content);
        }

        @JSONField(deserialize = false)
        public void setImage(final String mediaId) {
            this._image = new Image();
            this._image.setMediaId(mediaId);
        }

        private String _touser;
        private String _msgtype;
        private Text   _text;
        private Image  _image;
    }

    @Override
    public Transformer<RpcRunner, WXAPIResponse> sendCustomMessageInText(final String accessToken,
            final String openid, final String content) {
        return runners -> runners.flatMap( runner -> runner.name("wxcommon.sendCustomMessageInText").execute(
        interact-> {
            final CustomMessageReq req = new CustomMessageReq();
            req.setToUser(openid);
            req.setMsgType("text");
            req.setTextContent(content);
            return sendCustomMessage(interact, accessToken, req);
        }));
    }

    @Override
    public Transformer<RpcRunner, WXAPIResponse> sendCustomMessageInImage(final String accessToken,
            final String openid, final String mediaId) {
        return runners -> runners.flatMap( runner -> runner.name("wxcommon.sendCustomMessageInImage").execute(
        interact-> {
            final CustomMessageReq req = new CustomMessageReq();
            req.setToUser(openid);
            req.setMsgType("image");
            req.setImage(mediaId);
            return sendCustomMessage(interact, accessToken, req);
        }));
    }

    private Observable<WXAPIResponse> sendCustomMessage(final Interact interact, final String accessToken,
            final CustomMessageReq req) {
        return interact.method(HttpMethod.POST)
            .uri("https://api.weixin.qq.com").path("/cgi-bin/message/custom/send")
            .paramAsQuery("access_token", accessToken)
            .body(req, ContentUtil.TOJSON)
            .responseAs(ContentUtil.ASJSON, WXAPIResponse.class)
            .doOnNext(WXProtocol.CHECK_WXRESP);
    }

    static class ShorturlReq {

        ShorturlReq(final String url) {
            this.long_url = url;
        }

        String action = "long2short";
        String long_url;

        public String getAction() {
            return action;
        }

        public String getLong_url() {
            return long_url;
        }
    }

    public interface ShorturlResponse extends WXAPIResponse {

        @JSONField(name = "short_url")
        public String getShorturl();

        @JSONField(name = "short_url")
        public void setShortUrl(final String url);
    }

    @Override
    public Transformer<RpcRunner, String> getShorturl(final String accessToken, final String url) {
        return runners -> runners.flatMap( runner -> runner.name("wxcommon.getShorturl").execute(
        interact-> {
            try {
                return interact.method(HttpMethod.POST)
                    .uri("https://api.weixin.qq.com")
                    .path("/cgi-bin/shorturl")
                    .paramAsQuery("access_token", accessToken)
                    .body(new ShorturlReq(url), ContentUtil.TOJSON)
                    .responseAs(ContentUtil.ASJSON, ShorturlResponse.class)
                    .doOnNext(WXProtocol.CHECK_WXRESP)
                    .map(resp -> resp.getShorturl());
            } catch (final Exception e) {
                return Observable.error(e);
            }
        }));
    }

    // https://developers.weixin.qq.com/miniprogram/dev/api-backend/getTempMedia.html
    @Override
    public Transformer<RpcRunner, MessageBody> getTempMedia(final String accessToken, final String mediaId) {
        return runners -> runners.flatMap(runner -> runner.name("wxcommon.getTempMedia").execute(interact ->
            interact.method(HttpMethod.GET)
                .uri("https://api.weixin.qq.com")
                .path("/cgi-bin/media/get")
                .paramAsQuery("access_token", accessToken)
                .paramAsQuery("media_id", mediaId)
                .response()
                .flatMap(fullmsg -> {
                    final String contentType = fullmsg.message().headers().get(HttpHeaderNames.CONTENT_TYPE);
                    if (contentType.startsWith(MediaType.APPLICATION_JSON) || contentType.startsWith(MediaType.TEXT_PLAIN)) {
                        // error return as json
                        return fullmsg.body().compose(MessageUtil.body2bean(ContentUtil.ASJSON, WXProtocol.WXAPIResponse.class))
                            .flatMap(resp -> Observable.error(new WXRespError(resp, resp.toString())));
                    } else {
                        return fullmsg.body();
                    }
                })
        ));
    }

    // https://developers.weixin.qq.com/miniprogram/dev/api-backend/uploadTempMedia.html
    @Override
    public Transformer<RpcRunner, UploadTempMediaResponse> uploadTempMedia(final String accessToken,
            final String name,
            final String filename,
            final Observable<? extends MessageBody> media) {
        return runners -> runners.flatMap(runner -> runner.name("wxcommon.uploadTempMedia").execute(interact ->
            interact.method(HttpMethod.POST)
                .uri("https://api.weixin.qq.com")
                .path("/cgi-bin/media/upload")
                .paramAsQuery("access_token", accessToken)
                .paramAsQuery("type", "image")
                .body(media.compose(tomultipart(name, filename)))
                .responseAs(ContentUtil.ASJSON, UploadTempMediaResponse.class)
                .doOnNext(WXProtocol.CHECK_WXRESP)));
    }

    private static String getNewMultipartDelimiter() {
        // construct a generated delimiter
        return Long.toHexString(PlatformDependent.threadLocalRandom().nextLong());
    }

    private Transformer<MessageBody, MessageBody> tomultipart(final String name, final String filename) {
        return bodys -> bodys.flatMap(body -> {
            final String multipartBoundary = getNewMultipartDelimiter();
            final String contentType = HttpHeaderValues.MULTIPART_FORM_DATA + "; " + HttpHeaderValues.BOUNDARY + '='
                    + multipartBoundary;

            final byte[] head = headOf(name, filename, body, multipartBoundary);
            final byte[] end = endOf(multipartBoundary);
            final int contentLength = head.length + end.length + body.contentLength();

            return Observable.<MessageBody>just(new MessageBody() {
                @Override
                public HttpHeaders headers() {
                    return EmptyHttpHeaders.INSTANCE;
                }
                @Override
                public String contentType() {
                    return contentType;
                }
                @Override
                public int contentLength() {
                    return contentLength;
                }
                @Override
                public Observable<? extends ByteBufSlice> content() {
                    return Observable.concat(bytes2bbs(head), body.content(), bytes2bbs(end));
                }});
        });
    }

    private byte[] endOf(final String multipartBoundary) {
        return ("\r\n--" + multipartBoundary + "--\r\n").getBytes(CharsetUtil.UTF_8);
    }

    private byte[] headOf(
            final String name,
            final String filename, final MessageBody body,
            final String multipartBoundary) {
        final StringBuilder sb = new StringBuilder();

        sb.append("--");
        sb.append(multipartBoundary);
        sb.append("\r\n");

        sb.append(HttpHeaderNames.CONTENT_DISPOSITION);
        sb.append(": ");
        sb.append(HttpHeaderValues.FORM_DATA);
        sb.append("; ");
        sb.append(HttpHeaderValues.NAME);
        sb.append("=\"");
        sb.append(name);
        sb.append("\"; ");
        sb.append(HttpHeaderValues.FILENAME);
        sb.append("=\"");
        sb.append(filename);
        sb.append("\"\r\n");

        if (body.contentLength() > 0) {
            sb.append(HttpHeaderNames.CONTENT_LENGTH);
            sb.append(": ");
            sb.append(body.contentLength());
            sb.append("\r\n");
        }

        sb.append(HttpHeaderNames.CONTENT_TYPE);
        sb.append(": ");
        sb.append(body.contentType());
        sb.append("\r\n\r\n");

        return sb.toString().getBytes(CharsetUtil.UTF_8);
    }

    private Observable<? extends ByteBufSlice> bytes2bbs(final byte[] bytes) {
        final Iterable<? extends DisposableWrapper<? extends ByteBuf>> elements = Arrays
                .asList(DisposableWrapperUtil.wrap(Unpooled.wrappedBuffer(bytes), (Action1<ByteBuf>) null));

        return Observable.just((ByteBufSlice) new ByteBufSlice() {
            @Override
            public void step() {
            }

            @Override
            public Iterable<? extends DisposableWrapper<? extends ByteBuf>> element() {
                return elements;
            }
        });
    }
}
