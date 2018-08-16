package org.jocean.wechat.spi;

public class PayBaseResponse {
    //协议层
    private String return_code; //SUCCESS/FAIL
    private String return_msg;

    //协议返回的具体数据（以下字段在return_code 为SUCCESS 的时候有返回）
    private String appid; //String(32) 	wx8888888888888888 	微信分配的公众账号ID
    private String mch_id;//	String(32) 	1900000109 	微信支付分配的商户号
    private String device_info;//否 	String(32) 	013467007045764 	微信支付分配的终端设备号
    private String nonce_str;//是 	String(32) 	5K8264ILTKCH16CQ2502SI8ZNMTM67VS 	随机字符串，不长于32位
    private String sign;//是 	String(32) 	C380BEC2BFD727A4B6845133519F3AD6 	签名，详见签名算法
    private String result_code;//业务结果  是 	String(16) 	SUCCESS 	SUCCESS/FAIL
    private String err_code;//错误代码 SYSTEMERROR
    private String err_code_des;//系统错误
    private String openid;// 	是 	String(128) 	wxd930ea5d5a258f4f 	用户在商户appid下的唯一标识
    private String is_subscribe;// 	否 	String(1) 	Y 	用户是否关注公众账号，Y-关注，N-未关注，仅在公众账号类型支付有效
    private String trade_type;// 	是 	String(16) 	JSAPI 	JSAPI、NATIVE、APP
    ////private String bank_type;// 	是 	String(16) 	CMC 	银行类型，采用字符串类型的银行标识，银行类型见银行列表
    //private int total_fee;// 	是 	Int 	100 	订单总金额，单位为分
    //private int settlement_total_fee;// 	否 	Int 	100 	应结订单金额=订单金额-非充值代金券金额，应结订单金额<=订单金额。
    //private String fee_type;// 	否 	String(8) 	CNY 	货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
    //private int 	cash_fee;// 	是 	Int 	100 	现金支付金额订单现金支付金额，详见支付金额
    //private String 	cash_fee_type;// 	否 	String(16) 	CNY 	货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
    //private int coupon_fee;// 	否 	Int 	10 	代金券金额<=订单金额，订单金额-代金券金额=现金支付金额，详见支付金额
    //private int coupon_count;// 	否 	Int 	1 	代金券使用数量
    //private int coupon_type_$n;// 	否 	Int 	CASH CASH--充值代金券 NO_CASH---非充值代金券 订单使用代金券时有返回（取值：CASH、NO_CASH）。$n为下标,从0开始编号，举例：coupon_type_$0
    //private String coupon_id_$n;// 	否 	String(20) 	10000 	代金券ID,$n为下标，从0开始编号
    //private int coupon_fee_$n;// 	否 	Int 	100 	单个代金券支付金额,$n为下标，从0开始编号
    private String transaction_id;// 	是 	String(32) 	1217752501201407033233368018 	微信支付订单号
    private String out_trade_no;// 	是 	String(32) 	1212321211201407033568112322 	商户系统的订单号，与请求一致。
    //private String attach;// 	否 	String(128) 	123456 	商家数据包，原样返回
    //private String 	time_end;// 	是 	String(14) 	20141030133525 	支付完成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则


    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getErr_code() {
        return err_code;
    }

    public void setErr_code(String err_code) {
        this.err_code = err_code;
    }

    public String getErr_code_des() {
        return err_code_des;
    }

    public void setErr_code_des(String err_code_des) {
        this.err_code_des = err_code_des;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getIs_subscribe() {
        return is_subscribe;
    }

    public void setIs_subscribe(String is_subscribe) {
        this.is_subscribe = is_subscribe;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    @Override
    public String toString() {
        return "PayBaseResponse{" +
                "return_code='" + return_code + '\'' +
                ", return_msg='" + return_msg + '\'' +
                ", appid='" + appid + '\'' +
                ", mch_id='" + mch_id + '\'' +
                ", device_info='" + device_info + '\'' +
                ", nonce_str='" + nonce_str + '\'' +
                ", sign='" + sign + '\'' +
                ", result_code='" + result_code + '\'' +
                ", err_code='" + err_code + '\'' +
                ", err_code_des='" + err_code_des + '\'' +
                ", openid='" + openid + '\'' +
                ", is_subscribe='" + is_subscribe + '\'' +
                ", trade_type='" + trade_type + '\'' +
                ", transaction_id='" + transaction_id + '\'' +
                ", out_trade_no='" + out_trade_no + '\'' +
                '}';
    }
}
