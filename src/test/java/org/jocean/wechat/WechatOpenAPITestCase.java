package org.jocean.wechat;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.alibaba.fastjson.JSON;

public class WechatOpenAPITestCase {

    @Test
    public final void testDecodeJson() {

        final String text = "{\"pre_auth_code\":\"preauthcode\",\"expires_in\":1800}";
        final WechatOpenAPI.PreAuthCodeResponse resp = JSON.parseObject(text, WechatOpenAPI.PreAuthCodeResponse.class);
        assertEquals("preauthcode", resp.getPreAuthCode());
        assertEquals(1800, resp.getExpires());
    }

}
