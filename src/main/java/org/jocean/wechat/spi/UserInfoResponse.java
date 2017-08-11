package org.jocean.wechat.spi;

import java.util.Arrays;

import com.alibaba.fastjson.annotation.JSONField;

public class UserInfoResponse {

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("UserInfoResponse [errcode=").append(errcode)
                .append(", errmsg=").append(errmsg)
                .append(", subscribe=").append(subscribe)
                .append(", openid=").append(openid)
                .append(", nickname=").append(nickname)
                .append(", sex=").append(sex)
                .append(", city=").append(city)
                .append(", country=").append(country)
                .append(", province=").append(province)
                .append(", language=").append(language)
                .append(", headimgurl=").append(headimgurl)
                .append(", subscribe_time=").append(subscribe_time)
                .append(", unionid=").append(unionid)
                .append(", remark=").append(remark)
                .append(", groupid=").append(groupid)
                .append(", tagid_list=").append(Arrays.toString(tagid_list)).append("]");
        return builder.toString();
    }

    @JSONField(name="errcode")
    public String getErrcode() {
        return errcode;
    }

    @JSONField(name="errcode")
    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    @JSONField(name="errmsg")
    public String getErrmsg() {
        return errmsg;
    }

    @JSONField(name="errmsg")
    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    @JSONField(name="subscribe")
    public int getSubscribe() {
        return subscribe;
    }

    @JSONField(name="subscribe")
    public void setSubscribe(int subscribe) {
        this.subscribe = subscribe;
    }

    @JSONField(name="openid")
    public String getOpenid() {
        return openid;
    }

    @JSONField(name="openid")
    public void setOpenid(String openid) {
        this.openid = openid;
    }

    @JSONField(name="nickname")
    public String getNickname() {
        return nickname;
    }

    @JSONField(name="nickname")
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @JSONField(name="sex")
    public int getSex() {
        return sex;
    }

    @JSONField(name="sex")
    public void setSex(int sex) {
        this.sex = sex;
    }

    @JSONField(name="city")
    public String getCity() {
        return city;
    }

    @JSONField(name="city")
    public void setCity(String city) {
        this.city = city;
    }

    @JSONField(name="country")
    public String getCountry() {
        return country;
    }

    @JSONField(name="country")
    public void setCountry(String country) {
        this.country = country;
    }

    @JSONField(name="province")
    public String getProvince() {
        return province;
    }

    @JSONField(name="province")
    public void setProvince(String province) {
        this.province = province;
    }

    @JSONField(name="language")
    public String getLanguage() {
        return language;
    }

    @JSONField(name="language")
    public void setLanguage(String language) {
        this.language = language;
    }

    @JSONField(name="headimgurl")
    public String getHeadimgurl() {
        return headimgurl;
    }

    @JSONField(name="headimgurl")
    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    @JSONField(name="subscribe_time")
    public long getSubscribe_time() {
        return subscribe_time;
    }

    @JSONField(name="subscribe_time")
    public void setSubscribe_time(long subscribe_time) {
        this.subscribe_time = subscribe_time;
    }

    @JSONField(name="unionid")
    public String getUnionid() {
        return unionid;
    }

    @JSONField(name="unionid")
    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    @JSONField(name="remark")
    public String getRemark() {
        return remark;
    }

    @JSONField(name="remark")
    public void setRemark(String remark) {
        this.remark = remark;
    }

    @JSONField(name="groupid")
    public int getGroupid() {
        return groupid;
    }

    @JSONField(name="groupid")
    public void setGroupid(int groupid) {
        this.groupid = groupid;
    }

    @JSONField(name="tagid_list")
    public int[] getTagid_list() {
        return tagid_list;
    }

    @JSONField(name="tagid_list")
    public void setTagid_list(int[] tagid_list) {
        this.tagid_list = tagid_list;
    }

    private String errcode;

    private String errmsg;

    private int subscribe;

    private String openid;

    private String nickname;

    private int sex;

    private String city;
    
    private String country;
    
    private String province;
    
    private String language;

    private String headimgurl;
    
    private long subscribe_time;

    private String unionid;

    private String remark;

    private int groupid;
    
    private int[] tagid_list;
}
