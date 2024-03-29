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
import org.jocean.rpc.annotation.RpcBuilder;
import org.jocean.wechat.WXProtocol.AccessTokenResponse;
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

    /**
     * https://developers.weixin.qq.com/doc/offiaccount/User_Management/Get_users_basic_information_UnionID.html
     *
     * 获取用户基本信息（包括UnionID机制）

        开发者可通过OpenID来获取用户基本信息。请使用https协议。

        接口调用请求说明 http请求方式: GET https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN

        参数说明

        参数  是否必须    说明
        access_token    是   调用接口凭证
        openid  是   普通用户的标识，对当前公众号唯一
        lang    否   返回国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语
        返回说明

        正常情况下，微信会返回下述JSON数据包给公众号：

        {
            "subscribe": 1,
            "openid": "o6_bmjrPTlm6_2sgVt7hMZOPfL2M",
            "nickname": "Band",
            "sex": 1,
            "language": "zh_CN",
            "city": "广州",
            "province": "广东",
            "country": "中国",
            "headimgurl":"http://thirdwx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/0",
            "subscribe_time": 1382694957,
            "unionid": " o6_bmasdasdsad6_2sgVt7hMZOPfL",
            "remark": "",
            "groupid": 0,
            "tagid_list":[128,2],
            "subscribe_scene": "ADD_SCENE_QR_CODE",
            "qr_scene": 98765,
            "qr_scene_str": ""
        }
        参数说明

        参数  说明
        subscribe   用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。
        openid  用户的标识，对当前公众号唯一
        nickname    用户的昵称
        sex 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
        city    用户所在城市
        country 用户所在国家
        province    用户所在省份
        language    用户的语言，简体中文为zh_CN
        headimgurl  用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
        subscribe_time  用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
        unionid 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
        remark  公众号运营者对粉丝的备注，公众号运营者可在微信公众平台用户管理界面对粉丝添加备注
        groupid 用户所在的分组ID（兼容旧的用户分组接口）
        tagid_list  用户被打上的标签ID列表
        subscribe_scene 返回用户关注的渠道来源，ADD_SCENE_SEARCH 公众号搜索，ADD_SCENE_ACCOUNT_MIGRATION 公众号迁移，ADD_SCENE_PROFILE_CARD 名片分享，ADD_SCENE_QR_CODE 扫描二维码，ADD_SCENE_PROFILE_LINK 图文页内名称点击，ADD_SCENE_PROFILE_ITEM 图文页右上角菜单，ADD_SCENE_PAID 支付后关注，ADD_SCENE_WECHAT_ADVERTISEMENT 微信广告，ADD_SCENE_OTHERS 其他
        qr_scene    二维码扫码场景（开发者自定义）
        qr_scene_str    二维码扫码场景描述（开发者自定义）
        错误时微信会返回错误码等信息，JSON数据包示例如下（该示例为AppID无效错误）:

        {"errcode":40013,"errmsg":"invalid appid"}
     * @author isdom
     *
     */
    @RpcBuilder
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

    @RpcBuilder
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

    @RpcBuilder
    interface GetJsapiTicketBuilder extends NeedAccessToken<GetJsapiTicketBuilder> {

        @GET
        @Path("https://api.weixin.qq.com/cgi-bin/ticket/getticket")
        @ConstParams({"type", "jsapi"})
        @Consumes(MediaType.APPLICATION_JSON)
        Observable<FetchTicketResponse> call();
    }

    public GetJsapiTicketBuilder getJsapiTicket();

    @RpcBuilder
    interface GetMonopolizedAccessTokenBuilder {
        @QueryParam("appid")
        GetMonopolizedAccessTokenBuilder appid(final String appid);

        @QueryParam("secret")
        GetMonopolizedAccessTokenBuilder secret(final String secret);

        @GET
        @Path("https://api.weixin.qq.com/cgi-bin/token")
        @ConstParams({"grant_type", "client_credential"})
        @Consumes(MediaType.APPLICATION_JSON)
        @OnResponse("org.jocean.wechat.WXProtocol.CHECK_WXRESP")
        Observable<AccessTokenResponse> call();
    }

    public GetMonopolizedAccessTokenBuilder getMonopolizedAccessToken();

    @RpcBuilder
    interface GetMonopolizedOAuthAccessTokenBuilder {
        @QueryParam("appid")
        GetMonopolizedOAuthAccessTokenBuilder appid(final String appid);

        @QueryParam("code")
        GetMonopolizedOAuthAccessTokenBuilder code(final String code);

        @QueryParam("secret")
        GetMonopolizedOAuthAccessTokenBuilder secret(final String secret);

        @GET
        @Path("https://api.weixin.qq.com/sns/oauth2/access_token")
        @ConstParams({"grant_type", "authorization_code"})
        @Consumes(MediaType.APPLICATION_JSON)
        @OnResponse("org.jocean.wechat.WXProtocol.CHECK_WXRESP")
        Observable<OAuthAccessTokenResponse> call();
    }

    public GetMonopolizedOAuthAccessTokenBuilder getMonopolizedOAuthAccessToken();

    @RpcBuilder
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

    /**
     * @deprecated use {@link org.jocean.wechat.WechatOpenAPI.getOAuthAccessToken} instead.
     */
    @Deprecated
    public GetOAuthAccessTokenBuilder getOAuthAccessToken();

    @RpcBuilder
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

    /**
     * @deprecated use {@link org.jocean.wechat.WechatOpenAPI.code2session} instead.
     */
    @Deprecated
    // 小程序: Mina
    // refer: https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1492585163_FtTNA&token=&lang=zh_CN
    public Code2SessionBuilder code2session();

    @RpcBuilder
    interface DownloadMediaBuilder extends NeedAccessToken<DownloadMediaBuilder> {
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

    @RpcBuilder
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
    @RpcBuilder
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
    @RpcBuilder
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

    public interface DelkfResponse extends WXAPIResponse {
    }

    @RpcBuilder
    interface DelkfBuilder extends NeedAccessToken<DelkfBuilder> {

        // 完整客服帐号，格式为：帐号前缀@公众号微信号
        @QueryParam("kf_account")
        DelkfBuilder kfaccount(final String account);

        @GET
        @Path("https://api.weixin.qq.com/customservice/kfaccount/del")
        @Consumes(MediaType.APPLICATION_JSON)
        @OnResponse("org.jocean.wechat.WXProtocol.CHECK_WXRESP")
        Observable<DelkfResponse> call();
    }

    public DelkfBuilder delkf();

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

    @RpcBuilder
    interface SendCustomMessageInTextBuilder extends NeedAccessToken<SendCustomMessageInTextBuilder> {

        @JSONField(name = "touser")
        public SendCustomMessageInTextBuilder toUser(final String touser);

        @JSONField(name = "msgtype")
        public SendCustomMessageInTextBuilder msgType(final String msgtype);

        @JSONField(name = "text")
        public SendCustomMessageInTextBuilder text(final Text text);

        @POST
        @Path("https://api.weixin.qq.com/cgi-bin/message/custom/send")
        @Consumes(MediaType.APPLICATION_JSON)
        @OnResponse("org.jocean.wechat.WXProtocol.CHECK_WXRESP")
        Observable<WXAPIResponse> call();
    }

    public SendCustomMessageInTextBuilder sendCustomMessageInText();
}
