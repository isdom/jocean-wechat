
package org.jocean.wechat;

import org.jocean.http.BodyBuilder;

import rx.Observable;

public interface WechatPayAPI {

    public String getName();
    
    public interface SendRedpackContext {
        
        public SendRedpackContext setNonceStr(final String nonce_str);
        
        public SendRedpackContext setMchBillno(final String mch_billno);
        
        public SendRedpackContext setSendName(final String send_name);
        
        public SendRedpackContext setReOpenid(final String re_openid);
        
        public SendRedpackContext setTotalAmount(final int total_amount);
        
        public SendRedpackContext setTotalNum(final int total_num);
        
        public SendRedpackContext setWishing(final String wishing);
        
        public SendRedpackContext setClientIp(final String client_ip);
        
        public SendRedpackContext setActName(final String act_name);
        
        public SendRedpackContext setRemark(final String remark);
        
        public SendRedpackContext setSceneId(final String scene_id);
        
        public SendRedpackContext setRiskInfo(final String risk_info);
        
        public Observable<SendRedpackResult> call();
    }
    
    public interface SendRedpackResult {
        
        public String getReturnCode();
        
        public String getReturnMsg();
        
        public String getResultCode();
        
        public String getErrCode();
        
        public String getErrCodeDes();
        
        public String getSendListid();
        
        public String getTotalAmount();
        
        public String getMchBillno();
        
        public String getMchId();
        
        public String getWxappid();
        
        public String getReOpenid();
    }
    
    public SendRedpackContext sendRedpack();
    
    public SendRedpackContext sendRedpack(final BodyBuilder bb);
}
