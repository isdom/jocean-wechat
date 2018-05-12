package org.jocean.wechat;

import com.alibaba.fastjson.annotation.JSONField;

import rx.functions.Action1;

public class WXProtocol {
    public interface WXAPIResponse {
        @JSONField(name="errcode")
        public String getErrcode();

        @JSONField(name="errcode")
        public void setErrcode(final String errcode);

        @JSONField(name="errmsg")
        public String getErrmsg();

        @JSONField(name="errmsg")
        public void setErrmsg(final String errmsg);
    }

    public static final Action1<WXAPIResponse> CHECK_WXRESP = resp-> {
        if (null != resp.getErrcode()) {
            throw new RuntimeException(resp.toString());
        }
    };

    public interface WXUserInfo {

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

    public interface UserInfoResponse extends WXAPIResponse, WXUserInfo {
    }

    public interface OAuthAccessTokenResponse extends WXAPIResponse {
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
}
