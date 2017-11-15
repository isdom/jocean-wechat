package org.jocean.wechat.service;

import java.util.concurrent.ConcurrentHashMap;

import org.jocean.j2se.eventbus.EventBusAware;
import org.jocean.wechat.WechatAPI;
import org.jocean.wechat.WechatSet;

import com.google.common.eventbus.EventBus;

public class DefaultWechatSet extends ConcurrentHashMap<String, WechatAPI> implements WechatSet, EventBusAware {

    private static final long serialVersionUID = 1L;

    @Override
    public void setEventBus(final EventBus eventbus) {
        eventbus.post(this);
    }
}
