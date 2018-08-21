package org.jocean.wechat.spi;

public class PayBaseResponse {
    //协议层
    protected String return_code; //SUCCESS/FAIL
    protected String return_msg;

    // 协议返回的具体数据（以下字段在return_code 为SUCCESS 的时候有返回）
    //  小程序ID    appid   是   String(32)  wxd678efh567hg6787  微信分配的小程序ID
    protected String appid;
    // 商户号  mch_id  是   String(32)  1230000109  微信支付分配的商户号
    protected String mch_id;//	String(32) 	1900000109 	微信支付分配的商户号

    // 随机字符串    nonce_str   是   String(32)  5K8264ILTKCH16CQ2502SI8ZNMTM67VS    随机字符串，不长于32位。推荐随机数生成算法
    protected String nonce_str;

    // 签名   sign    是   String(32)  C380BEC2BFD727A4B6845133519F3AD6    签名，详见签名生成算法
    protected String sign;

    // 业务结果 result_code 是   String(16)  SUCCESS SUCCESS/FAIL
    protected String result_code;

    // 错误代码 err_code    否   String(32)  SYSTEMERROR 错误码
    protected String err_code;

    // 错误代码描述   err_code_des    否   String(128) 系统错误    结果信息描述
    protected String err_code_des;

    // 以下字段在return_code 、result_code、trade_state都为SUCCESS时有返回 ，
    // 如trade_state不为 SUCCESS，则只返回out_trade_no（必传）和attach（选传）。
    // 设备号  device_info 否   String(32)  013467007045764 微信支付分配的终端设备号，
    protected String device_info;

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(final String return_code) {
        this.return_code = return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(final String return_msg) {
        this.return_msg = return_msg;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(final String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(final String mch_id) {
        this.mch_id = mch_id;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(final String device_info) {
        this.device_info = device_info;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(final String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(final String sign) {
        this.sign = sign;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(final String result_code) {
        this.result_code = result_code;
    }

    public String getErr_code() {
        return err_code;
    }

    public void setErr_code(final String err_code) {
        this.err_code = err_code;
    }

    public String getErr_code_des() {
        return err_code_des;
    }

    public void setErr_code_des(final String err_code_des) {
        this.err_code_des = err_code_des;
    }
}
