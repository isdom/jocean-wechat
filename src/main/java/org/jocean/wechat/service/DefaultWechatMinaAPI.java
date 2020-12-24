/**
 *
 */
package org.jocean.wechat.service;

import org.jocean.http.ContentUtil;
import org.jocean.http.RpcRunner;
import org.jocean.idiom.jmx.MBeanRegister;
import org.jocean.idiom.jmx.MBeanRegisterAware;
import org.jocean.wechat.WXProtocol;
import org.jocean.wechat.WXProtocol.Code2SessionResponse;
import org.jocean.wechat.WechatMinaAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import rx.Observable;
import rx.Observable.Transformer;


public class DefaultWechatMinaAPI implements WechatMinaAPI, MBeanRegisterAware {

    @SuppressWarnings("unused")
    private static final Logger LOG =
            LoggerFactory.getLogger(DefaultWechatMinaAPI.class);

    @Override
    public void setMBeanRegister(final MBeanRegister register) {
        register.registerMBean("name=wxmina", new MinaInfoMXBean() {
            @Override
            public String getName() {
                return _name;
            }

            @Override
            public String getAppid() {
                return _appid;
            }

            @Override
            public String getSecret() {
                return "***";
            }});
    }

    @Override
    public String getName() {
        return this._name;
    }

    @Override
    public String getAppid() {
        return this._appid;
    }

    @Override
    public Transformer<RpcRunner, Code2SessionResponse> code2session(final String code) {
        return runners -> runners.flatMap( runner -> runner.name("wxmina.code2session").execute(
        interact-> {
            try {
                return interact
                    .uri("https://api.weixin.qq.com")
                    .path("/sns/jscode2session")
                    .paramAsQuery("appid", this._appid)
                    .paramAsQuery("secret", this._secret)
                    .paramAsQuery("js_code", code)
                    .paramAsQuery("grant_type", "authorization_code")
                    .responseAs(ContentUtil.ASJSON, Code2SessionResponse.class)
                    .compose(WXProtocol.checkWXResp());
            } catch (final Exception e) {
                return Observable.error(e);
            }
        }));
    }

    @Value("${mina.name}")
    String _name;

    @Value("${mina.appid}")
    String _appid;

    @Value("${mina.secret}")
    String _secret;
}
