package org.jocean.wechat;

public interface AuthorizedMP {

    public String getComponentAppid();

    public String getAppid();

    public String getAccessToken();

    public String getRefreshToken();

    public long getAccessTokenExpireInMs();
}
