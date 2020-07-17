package org.jocean.wechat;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jocean.http.FullMessage;
import org.jocean.rpc.annotation.ConstParams;
import org.jocean.rpc.annotation.OnResponse;
import org.jocean.wechat.WXProtocol.Code2SessionResponse;
import org.jocean.wechat.WXProtocol.OAuthAccessTokenResponse;
import org.jocean.wechat.WXProtocol.UserInfoResponse;
import org.jocean.wechat.WXProtocol.WXAPIResponse;
import org.jocean.wechat.spi.FetchTicketResponse;

import com.alibaba.fastjson.annotation.JSONField;

import io.netty.handler.codec.http.HttpResponse;
import rx.Observable;

public interface WechatMPAPI {
    interface NeedAccessToken<BUILDER> {
        @QueryParam("access_token")
        BUILDER accessToken(final String accessToken);
    }

    interface GetUserInfoBuilder extends NeedAccessToken<GetUserInfoBuilder> {

        @QueryParam("openid")
        GetUserInfoBuilder openid(final String openid);

        @GET
        @Path("https://api.weixin.qq.com/cgi-bin/user/info")
        @ConstParams({"lang", "zh_CN"})
        @Consumes(MediaType.APPLICATION_JSON)
        @OnResponse("org.jocean.wechat.WXProtocol.CHECK_WXRESP")
        Observable<UserInfoResponse> call();
    }

    public GetUserInfoBuilder getUserInfo();

    interface GetSnsUserInfoBuilder extends NeedAccessToken<GetSnsUserInfoBuilder> {

        @QueryParam("openid")
        GetSnsUserInfoBuilder openid(final String openid);

        @GET
        @Path("https://api.weixin.qq.com/sns/userinfo")
        @ConstParams({"lang", "zh_CN"})
        @Consumes(MediaType.APPLICATION_JSON)
        @OnResponse("org.jocean.wechat.WXProtocol.CHECK_WXRESP")
        Observable<UserInfoResponse> call();
    }

    public GetSnsUserInfoBuilder getSnsUserInfo();

    interface GetJsapiTicketBuilder extends NeedAccessToken<GetJsapiTicketBuilder> {

        @GET
        @Path("https://api.weixin.qq.com/cgi-bin/ticket/getticket")
        @ConstParams({"type", "jsapi"})
        @Consumes(MediaType.APPLICATION_JSON)
        Observable<FetchTicketResponse> call();
    }

    public GetJsapiTicketBuilder getJsapiTicket();

    interface GetOAuthAccessTokenBuilder {
        @QueryParam("appid")
        GetOAuthAccessTokenBuilder authorizerAppid(final String authorizerAppid);

        @QueryParam("code")
        GetOAuthAccessTokenBuilder code(final String code);

        @QueryParam("component_appid")
        GetOAuthAccessTokenBuilder componentAppid(final String componentAppid);

        @QueryParam("component_access_token")
        GetOAuthAccessTokenBuilder componentAccesstoken(final String componentAccesstoken);

        @GET
        @Path("https://api.weixin.qq.com/sns/oauth2/component/access_token")
        @ConstParams({"grant_type", "authorization_code"})
        @Consumes(MediaType.APPLICATION_JSON)
        @OnResponse("org.jocean.wechat.WXProtocol.CHECK_WXRESP")
        Observable<OAuthAccessTokenResponse> call();
    }

    public GetOAuthAccessTokenBuilder getOAuthAccessToken();

    interface Code2SessionBuilder {
        @QueryParam("appid")
        Code2SessionBuilder authorizerAppid(final String authorizerAppid);

        @QueryParam("js_code")
        Code2SessionBuilder code(final String code);

        @QueryParam("component_appid")
        Code2SessionBuilder componentAppid(final String componentAppid);

        @QueryParam("component_access_token")
        Code2SessionBuilder componentAccesstoken(final String componentAccesstoken);

        @GET
        @Path("https://api.weixin.qq.com/sns/component/jscode2session")
        @ConstParams({"grant_type", "authorization_code"})
        @Consumes(MediaType.APPLICATION_JSON)
        @OnResponse("org.jocean.wechat.WXProtocol.CHECK_WXRESP")
        Observable<Code2SessionResponse> call();
    }

    // 小程序: Mina
    // refer: https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1492585163_FtTNA&token=&lang=zh_CN
    public Code2SessionBuilder code2session();

    interface DownloadMediaBuilder extends NeedAccessToken<GetUserInfoBuilder> {
        @QueryParam("media_id")
        DownloadMediaBuilder mediaId(final String mediaId);

        @GET
        @Path("https://api.weixin.qq.com/cgi-bin/media/get")
//        @Consumes(MediaType.APPLICATION_JSON)
//        @OnResponse("org.jocean.wechat.WXProtocol.CHECK_WXRESP")
        Observable<FullMessage<HttpResponse>> call();
    }

    public DownloadMediaBuilder downloadMedia();

    interface UploadTempMediaBuilder {

    }

    public class ShorturlReq {

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

    interface GetShorturlBuilder extends NeedAccessToken<GetShorturlBuilder> {
        @Produces(MediaType.APPLICATION_JSON)
        GetUserInfoBuilder body(final ShorturlReq req);

        @POST
        @Path("https://api.weixin.qq.com/cgi-bin/shorturl")
        @Consumes(MediaType.APPLICATION_JSON)
        @OnResponse("org.jocean.wechat.WXProtocol.CHECK_WXRESP")
        Observable<ShorturlResponse> call();
    }

    public GetShorturlBuilder getShorturl();

    public interface KfInfo {
        @JSONField(name="kf_id")
        public String getKfid();

        @JSONField(name="kf_id")
        public void setKfid(final String id);

        @JSONField(name="kf_account")
        public String getKfaccount();

        @JSONField(name="kf_account")
        public void setKfaccount(final String account);

        @JSONField(name="kf_headimgurl")
        public String getKfheadimgurl();

        @JSONField(name="kf_headimgurl")
        public void setKfheadimgurl(final String headimgurl);

        @JSONField(name="kf_nick")
        public String getKfnick();

        @JSONField(name="kf_nick")
        public void setKfnick(final String nick);

        @JSONField(name="kf_wx")
        public String getKfwx();

        @JSONField(name="kf_wx")
        public void setKfwx(final String wx);
    }

    public interface GetKflistResponse extends WXAPIResponse {
        @JSONField(name="kf_list")
        public KfInfo[] getKflist();

        @JSONField(name="kf_list")
        public void setKflist(final KfInfo[] kfs);
    }

    // https://developers.weixin.qq.com/doc/offiaccount/Customer_Service/Customer_Service_Management.html#0
    interface GetKflistBuilder extends NeedAccessToken<GetKflistBuilder> {

        @GET
        @Path("https://api.weixin.qq.com/cgi-bin/customservice/getkflist")
        @Consumes(MediaType.APPLICATION_JSON)
        @OnResponse("org.jocean.wechat.WXProtocol.CHECK_WXRESP")
        Observable<GetKflistResponse> call();
    }

    public GetKflistBuilder getKflist();

    public interface InviteWorkerResponse extends WXAPIResponse {
    }

    public class InviteWorkerRequest {
        // 完整客服帐号，格式为：帐号前缀@公众号微信号
        @JSONField(name="kf_account")
        public String getKfaccount() {
            return this.kf_account;
        }

        // 完整客服帐号，格式为：帐号前缀@公众号微信号
        @JSONField(name="kf_account")
        public void setKfaccount(final String account) {
            this.kf_account = account;
        }

        // 接收绑定邀请的客服微信号
        @JSONField(name="invite_wx")
        public String getInvitewx() {
            return this.invite_wx;
        }

        // 接收绑定邀请的客服微信号
        @JSONField(name="invite_wx")
        public void setInvitewx(final String wx) {
            this.invite_wx = wx;
        }

        private String kf_account;
        private String invite_wx;
    }

    // 新添加的客服帐号是不能直接使用的，只有客服人员用微信号绑定了客服账号后，方可登录Web客服进行操作。
    // 此接口发起一个绑定邀请到客服人员微信号，客服人员需要在微信客户端上用该微信号确认后帐号才可用。
    // 尚未绑定微信号的帐号可以进行绑定邀请操作，邀请未失效时不能对该帐号进行再次绑定微信号邀请。
    interface InviteWorkerBuilder extends NeedAccessToken<InviteWorkerBuilder> {

        @Produces(MediaType.APPLICATION_JSON)
        InviteWorkerBuilder body(final InviteWorkerRequest req);

        @POST
        @Path("https://api.weixin.qq.com/customservice/kfaccount/inviteworker")
        @Consumes(MediaType.APPLICATION_JSON)
        @OnResponse("org.jocean.wechat.WXProtocol.CHECK_WXRESP")
        Observable<InviteWorkerResponse> call();
    }

    public InviteWorkerBuilder inviteWorker();
}
