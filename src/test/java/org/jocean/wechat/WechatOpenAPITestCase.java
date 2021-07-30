package org.jocean.wechat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

    @Test
    public final void testDecodeAuthorizationInfoAsJson() {
        final String text = "{\"authorization_info\":{\"authorizer_appid\":\"testappid\",\"authorizer_refresh_token\":\"refreshtoken@@@test\",\"func_info\":[{\"funcscope_category\":{\"id\":17}},{\"funcscope_category\":{\"id\":18},\"confirm_info\":{\"need_confirm\":0,\"already_confirm\":0,\"can_confirm\":0}},{\"funcscope_category\":{\"id\":19}},{\"funcscope_category\":{\"id\":25},\"confirm_info\":{\"need_confirm\":0,\"already_confirm\":0,\"can_confirm\":0}}]}}";
        final WechatOpenAPI.AuthorizerInfoResponse resp = JSON.parseObject(text, WechatOpenAPI.AuthorizerInfoResponse.class);
        System.out.println(resp);
        assertNotNull(resp.getAuthorizationInfo().getAuthorizerAppid());

    }
}
