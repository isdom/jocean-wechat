
package org.jocean.wechat;

import org.jocean.http.Interact;
import org.jocean.http.MessageBody;

import com.alibaba.fastjson.annotation.JSONField;

import rx.Observable;
import rx.functions.Func1;

public interface WechatAPI {

    public String getName();

    public String getAppid();
    public String getJsapiTicket();

    public String getAccessToken();

    public interface WechatAPIResponse {
        @JSONField(name="errcode")
        public String getErrcode();

        @JSONField(name="errcode")
        public void setErrcode(final String errcode);

        @JSONField(name="errmsg")
        public String getErrmsg();

        @JSONField(name="errmsg")
        public void setErrmsg(final String errmsg);
    }

    public interface UserInfoResponse extends WechatAPIResponse {

        @JSONField(name="subscribe")
        public int getSubscribe();

        @JSONField(name="subscribe")
        public void setSubscribe(final int subscribe);

        @JSONField(name="openid")
        public String getOpenid();

        @JSONField(name="openid")
        public void setOpenid(final String openid);

        @JSONField(name="nickname")
        public String getNickname();

        @JSONField(name="nickname")
        public void setNickname(final String nickname);

        @JSONField(name="sex")
        public int getSex();

        @JSONField(name="sex")
        public void setSex(final int sex);

        @JSONField(name="city")
        public String getCity();

        @JSONField(name="city")
        public void setCity(final String city);

        @JSONField(name="country")
        public String getCountry();

        @JSONField(name="country")
        public void setCountry(final String country);

        @JSONField(name="province")
        public String getProvince();

        @JSONField(name="province")
        public void setProvince(final String province);

        @JSONField(name="language")
        public String getLanguage();

        @JSONField(name="language")
        public void setLanguage(final String language);

        @JSONField(name="headimgurl")
        public String getHeadimgurl();

        @JSONField(name="headimgurl")
        public void setHeadimgurl(final String headimgurl);

        @JSONField(name="subscribe_time")
        public long getSubscribe_time();

        @JSONField(name="subscribe_time")
        public void setSubscribe_time(final long subscribe_time);

        @JSONField(name="unionid")
        public String getUnionid();

        @JSONField(name="unionid")
        public void setUnionid(final String unionid);

        @JSONField(name="remark")
        public String getRemark();

        @JSONField(name="remark")
        public void setRemark(final String remark);

        @JSONField(name="groupid")
        public int getGroupid();

        @JSONField(name="groupid")
        public void setGroupid(final int groupid);

        @JSONField(name="tagid_list")
        public int[] getTagid_list();

        @JSONField(name="tagid_list")
        public void setTagid_list(final int[] tagid_list);
    }

    public Func1<Interact, Observable<UserInfoResponse>> getUserInfo(final String openid);

    public Func1<Interact, Observable<UserInfoResponse>> getSnsapiUserInfo(final String snsapiAccessToken, final String openid);

    public interface OAuthAccessTokenResponse extends WechatAPIResponse {
        @JSONField(name="expires_in")
        public int getExpiresIn();

        @JSONField(name="expires_in")
        public void setExpiresIn(final int expiresIn);

        @JSONField(name="access_token")
        public String getAccessToken();

        @JSONField(name="access_token")
        public void setAccessToken(final String accessToken);

        @JSONField(name="refresh_token")
        public String getRefreshToken();

        @JSONField(name="refresh_token")
        public void setRefreshToken(final String refreshToken);

        @JSONField(name="scope")
        public String getScope();

        @JSONField(name="scope")
        public void setScope(final String scope);

        @JSONField(name="openid")
        public String getOpenid();

        @JSONField(name="openid")
        public void setOpenid(final String openid);
    }

    public Func1<Interact, Observable<OAuthAccessTokenResponse>> getOAuthAccessToken(final String code);

    /**
     * @param interact
     * @param expireSeconds 该二维码有效时间，以秒为单位。 最大不超过2592000（即30天）
     * @param scenestr 场景值ID（字符串形式的ID），字符串类型，长度限制为1到64
     * @return url for download qrcode image
     */
    public Func1<Interact, Observable<String>> createVolatileQrcode(final int expireSeconds, final String scenestr);

    //  input mediaId (see : https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1444738726&token=&lang=zh_CN)
    //  output Blob : media download
    public Func1<Interact, Observable<MessageBody>> downloadMedia(final String mediaId);
}
