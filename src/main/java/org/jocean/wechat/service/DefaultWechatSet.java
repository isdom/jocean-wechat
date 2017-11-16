package org.jocean.wechat.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.jocean.j2se.eventbus.EventBusAware;
import org.jocean.j2se.jmx.MBeanRegister;
import org.jocean.j2se.jmx.MBeanRegisterAware;
import org.jocean.wechat.WechatAPI;
import org.jocean.wechat.WechatSet;
import org.jocean.wechat.mbean.WechatSetMBean;

import com.google.common.eventbus.EventBus;

public class DefaultWechatSet implements WechatSet, EventBusAware, WechatSetMBean, MBeanRegisterAware {

    @Override
    public Map<String, Map<String, String>> getWxApis() {
        final Map<String, Map<String, String>> info = new HashMap<>();
        for (DefaultWechatAPI api : _wxapis) {
            info.put(api.getName(), api.info());
        }
        
        return info;
    }
    
    @Override
    public void setMBeanRegister(final MBeanRegister register) {
        register.registerMBean("name=wxs", this);
    }
    
    @Override
    public void setEventBus(final EventBus eventbus) {
        eventbus.post(this);
    }

    @Override
    public WechatAPI get(final String name) {
        for (WechatAPI wxapi : this._wxapis) {
            if (name.equals(wxapi.getName())) {
                return wxapi;
            }
        }
        return null;
    }
    
    @Inject
    @Named("wxapis")
    Collection<DefaultWechatAPI> _wxapis;
}
