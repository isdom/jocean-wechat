/**
 *
 */
package org.jocean.wechat.service;

import org.jocean.wechat.WXOpenComponent;
import org.springframework.beans.factory.annotation.Value;

public class DefaultWXOpenComponent implements WXOpenComponent {

    /* (non-Javadoc)
     * @see org.jocean.wechat.WXOpenComponent#getComponentAppid()
     */
    @Override
    public String getComponentAppid() {
        return _appid;
    }

    /* (non-Javadoc)
     * @see org.jocean.wechat.WXOpenComponent#getComponentSecret()
     */
    @Override
    public String getComponentSecret() {
        return _secret;
    }

    /* (non-Javadoc)
     * @see org.jocean.wechat.WXOpenComponent#getComponentToken()
     */
    @Override
    public String getComponentToken() {
        return _componentToken;
    }

    /* (non-Javadoc)
     * @see org.jocean.wechat.WXOpenComponent#getComponentTokenExpireInMs()
     */
    @Override
    public long getComponentTokenExpireInMs() {
        return _componentTokenExpireInMs;
    }

    /* (non-Javadoc)
     * @see org.jocean.wechat.WXOpenComponent#getComponentVerifyTicket()
     */
    @Override
    public String getComponentVerifyTicket() {
        return _componentVerifyTicket;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("DefaultWXOpenComponent [componentAppid=").append(_appid).append(", componentSecret=").append(_secret)
                .append(", componentToken=").append(_componentToken).append(", componentTokenExpireInMs=")
                .append(_componentTokenExpireInMs).append(", componentVerifyTicket=").append(_componentVerifyTicket)
                .append("]");
        return builder.toString();
    }

    @Value("${wxopen.appid}")
    String _appid;

    @Value("${wxopen.secret}")
    String _secret;

    @Value("${component.token}")
    String _componentToken = null;

    @Value("${component.token.expire}")
    long _componentTokenExpireInMs = 0;

    @Value("${component.verify.ticket}")
    String _componentVerifyTicket = null;
}
