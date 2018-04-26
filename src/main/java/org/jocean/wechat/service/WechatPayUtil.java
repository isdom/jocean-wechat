
package org.jocean.wechat.service;

import org.jocean.wechat.WechatPayAPI;

public class WechatPayUtil {

    public static WechatPayAPI buildPayAPI(
            final String appid,   //微信分配的公众账号ID
            final String mchid,   //微信支付分配的商户号
            final String key,     //商户平台设置的密钥key
            final String certAsBase64,        //商户证书
            final String certPassword //证书密码
            ) {
        final DefaultWechatPayAPI api = new DefaultWechatPayAPI();
        api._appid = appid;
        api._mch_id = mchid;
        api._key = key;
        api._certAsBase64 = certAsBase64;
        api._certPassword = certPassword;

        return api;
    }
}
