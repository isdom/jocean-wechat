package org.jocean.wechat.service;

import java.util.concurrent.ConcurrentHashMap;

import org.jocean.wechat.WechatAPI;
import org.jocean.wechat.WechatSet;

public class DefaultWechatSet extends ConcurrentHashMap<String, WechatAPI> implements WechatSet {

    private static final long serialVersionUID = 1L;
}
