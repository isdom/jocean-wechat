/**
 * 
 */
package org.jocean.wechat.spi;

import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.alibaba.fastjson.annotation.JSONField;


/**
 * @author isdom
 *
 */
@Path("https://api.weixin.qq.com/cgi-bin/qrcode/create")
public class CreateQrcodeRequest {

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("CreateQrcodeRequest [accessToken=").append(_accessToken).append(", expireSeconds=")
                .append(_expireSeconds).append(", actionName=").append(_actionName).append(", actionInfo=")
                .append(_actionInfo).append("]");
        return builder.toString();
    }

    @JSONField(serialize = false)
    public String getAccessToken() {
        return _accessToken;
    }

    public void setAccessToken(final String token) {
        this._accessToken = token;
    }

    @JSONField(name = "expire_seconds", ordinal=1)
    public int getExpireSeconds() {
        return _expireSeconds;
    }

    public void setExpireSeconds(int expireSeconds) {
        this._expireSeconds = expireSeconds;
    }

    @JSONField(name = "action_name", ordinal=2)
    public String getActionName() {
        return _actionName;
    }

    public void setActionName(final String actionName) {
        this._actionName = actionName;
    }

    @JSONField(name = "action_info", ordinal=3)
    public ActionInfo getActionInfo() {
        return _actionInfo;
    }

    public void setScenestr(final String scenestr) {
        this._actionInfo = new ActionInfo(scenestr);
    }

    @QueryParam("access_token")
    private String _accessToken;

    private int _expireSeconds;
    
    private String _actionName;
    
    private ActionInfo _actionInfo;

    public static class ActionInfo {
        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append("[scene=").append(_scene).append("]");
            return builder.toString();
        }

        ActionInfo(final String scenestr) {
            this._scene = new Scene(scenestr);
        }

        @JSONField(name = "scene")
        public Scene getScene() {
            return _scene;
        }

        private final Scene _scene;
    }

    public static class Scene {
        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append("[scenestr=").append(_scenestr).append("]");
            return builder.toString();
        }

        Scene(final String scenestr) {
            this._scenestr = scenestr;
        }

        @JSONField(name = "scene_str")
        public String getScenestr() {
            return _scenestr;
        }

        private final String _scenestr;
    }
}
