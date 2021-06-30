/**
 *
 */
package org.jocean.wechat;

/**
 * @author isdom
 *
 */
public interface WXOpenComponent {

    public String getComponentAppid();

    public String getComponentSecret();

    public String getComponentToken();

    public long getComponentTokenExpireInMs();

    public String getComponentVerifyTicket();
}
