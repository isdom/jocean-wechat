package org.jocean.wechat.service;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Named;

import org.jocean.j2se.eventbus.EventBusAware;
import org.jocean.wechat.WechatAPI;
import org.jocean.wechat.WechatSet;

import com.google.common.eventbus.EventBus;

public class DefaultWechatSet implements WechatSet, EventBusAware {

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
    Collection<WechatAPI> _wxapis;
}
