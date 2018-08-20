package org.jocean.wechat.spi;

public class PayBaseResponse {
    //协议层
    private String return_code; //SUCCESS/FAIL
    private String return_msg;

    // 协议返回的具体数据（以下字段在return_code 为SUCCESS 的时候有返回）
    //  小程序ID    appid   是   String(32)  wxd678efh567hg6787  微信分配的小程序ID
    private String appid;
    // 商户号  mch_id  是   String(32)  1230000109  微信支付分配的商户号
    private String mch_id;//	String(32) 	1900000109 	微信支付分配的商户号

    // 随机字符串    nonce_str   是   String(32)  5K8264ILTKCH16CQ2502SI8ZNMTM67VS    随机字符串，不长于32位。推荐随机数生成算法
    private String nonce_str;

    // 签名   sign    是   String(32)  C380BEC2BFD727A4B6845133519F3AD6    签名，详见签名生成算法
    private String sign;

    // 业务结果 result_code 是   String(16)  SUCCESS SUCCESS/FAIL
    private String result_code;

    // 错误代码 err_code    否   String(32)  SYSTEMERROR 错误码
    private String err_code;

    // 错误代码描述   err_code_des    否   String(128) 系统错误    结果信息描述
    private String err_code_des;

    // 以下字段在return_code 、result_code、trade_state都为SUCCESS时有返回 ，
    // 如trade_state不为 SUCCESS，则只返回out_trade_no（必传）和attach（选传）。
    // 设备号  device_info 否   String(32)  013467007045764 微信支付分配的终端设备号，
    private String device_info;

    // 用户标识 openid  是   String(128) oUpF8uMuAJO_M2pxb1Q9zNjWeS6o    用户在商户appid下的唯一标识
    private String openid;

    // 是否关注公众账号 is_subscribe    否   String(1)   Y   用户是否关注公众账号，Y-关注，N-未关注，仅在公众账号类型支付有效
    private String is_subscribe;

    // 交易类型 trade_type  是   String(16)  JSAPI   调用接口提交的交易类型，取值如下：JSAPI，NATIVE，APP，MICROPAY，详细说明见参数规定
    private String trade_type;

    // 交易状态 trade_state 是   String(32)  SUCCESS
    /*
    SUCCESS—支付成功

    REFUND—转入退款

    NOTPAY—未支付

    CLOSED—已关闭

    REVOKED—已撤销（刷卡支付）

    USERPAYING--用户支付中

    PAYERROR--支付失败(其他原因，如银行返回失败)
    */
    private String trade_state;

    // 付款银行 bank_type   是   String(16)  CMC 银行类型，采用字符串类型的银行标识
    private String bank_type;

    // 标价金额 total_fee   是   Int 100 订单总金额，单位为分
    private Integer total_fee;

    // 应结订单金额    settlement_total_fee    否   Int 100 当订单使用了免充值型优惠券后返回该参数，应结订单金额=订单金额-免充值优惠券金额。
    private Integer settlement_total_fee;

    // 标价币种 fee_type    否   String(8)   CNY 货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
    private String fee_type;

    // 现金支付金额   cash_fee    是   Int 100 现金支付金额订单现金支付金额，详见支付金额
    private Integer 	cash_fee;

    // 金支付币种    cash_fee_type   否   String(16)  CNY 货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
    private String 	cash_fee_type;

    // 代金券金额    coupon_fee  否   Int 100 “代金券”金额<=订单金额，订单金额-“代金券”金额=现金支付金额，详见支付金额
    private Integer coupon_fee;

    // 代金券使用数量  coupon_count    否   Int 1   代金券使用数量
    private Integer coupon_count;

    //private int coupon_type_$n;// 	否 	Int 	CASH CASH--充值代金券 NO_CASH---非充值代金券 订单使用代金券时有返回（取值：CASH、NO_CASH）。$n为下标,从0开始编号，举例：coupon_type_$0
    //private String coupon_id_$n;// 	否 	String(20) 	10000 	代金券ID,$n为下标，从0开始编号
    //private int coupon_fee_$n;// 	否 	Int 	100 	单个代金券支付金额,$n为下标，从0开始编号

    // 微信支付订单号  transaction_id  是   String(32)  1009660380201506130728806387    微信支付订单号
    private String transaction_id;

    // 商户订单号    out_trade_no    是   String(32)  20150806125346  商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。
    private String out_trade_no;

    // 附加数据 attach  否   String(128) 深圳分店    附加数据，原样返回
    private String attach;

    // 支付完成时间   time_end    是   String(14)  20141030133525  订单支付时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
    private String 	time_end;

    // 交易状态描述   trade_state_desc    是   String(256) 支付失败，请重新下单支付    对当前查询订单状态的描述和下一步操作的指引
    private String  trade_state_desc;

    public String getTrade_state() {
        return trade_state;
    }

    public void setTrade_state(final String trade_state) {
        this.trade_state = trade_state;
    }

    public String getBank_type() {
        return bank_type;
    }

    public void setBank_type(final String bank_type) {
        this.bank_type = bank_type;
    }

    public Integer getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(final Integer total_fee) {
        this.total_fee = total_fee;
    }

    public Integer getSettlement_total_fee() {
        return settlement_total_fee;
    }

    public void setSettlement_total_fee(final Integer settlement_total_fee) {
        this.settlement_total_fee = settlement_total_fee;
    }

    public String getFee_type() {
        return fee_type;
    }

    public void setFee_type(final String fee_type) {
        this.fee_type = fee_type;
    }

    public Integer getCash_fee() {
        return cash_fee;
    }

    public void setCash_fee(final Integer cash_fee) {
        this.cash_fee = cash_fee;
    }

    public String getCash_fee_type() {
        return cash_fee_type;
    }

    public void setCash_fee_type(final String cash_fee_type) {
        this.cash_fee_type = cash_fee_type;
    }

    public Integer getCoupon_fee() {
        return coupon_fee;
    }

    public void setCoupon_fee(final Integer coupon_fee) {
        this.coupon_fee = coupon_fee;
    }

    public Integer getCoupon_count() {
        return coupon_count;
    }

    public void setCoupon_count(final Integer coupon_count) {
        this.coupon_count = coupon_count;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(final String attach) {
        this.attach = attach;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(final String time_end) {
        this.time_end = time_end;
    }

    public String getTrade_state_desc() {
        return trade_state_desc;
    }

    public void setTrade_state_desc(final String trade_state_desc) {
        this.trade_state_desc = trade_state_desc;
    }

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

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(final String openid) {
        this.openid = openid;
    }

    public String getIs_subscribe() {
        return is_subscribe;
    }

    public void setIs_subscribe(final String is_subscribe) {
        this.is_subscribe = is_subscribe;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(final String trade_type) {
        this.trade_type = trade_type;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(final String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(final String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("PayBaseResponse [return_code=").append(return_code).append(", return_msg=").append(return_msg)
                .append(", appid=").append(appid).append(", mch_id=").append(mch_id).append(", nonce_str=")
                .append(nonce_str).append(", sign=").append(sign).append(", result_code=").append(result_code)
                .append(", err_code=").append(err_code).append(", err_code_des=").append(err_code_des)
                .append(", device_info=").append(device_info).append(", openid=").append(openid)
                .append(", is_subscribe=").append(is_subscribe).append(", trade_type=").append(trade_type)
                .append(", trade_state=").append(trade_state).append(", bank_type=").append(bank_type)
                .append(", total_fee=").append(total_fee).append(", settlement_total_fee=").append(settlement_total_fee)
                .append(", fee_type=").append(fee_type).append(", cash_fee=").append(cash_fee)
                .append(", cash_fee_type=").append(cash_fee_type).append(", coupon_fee=").append(coupon_fee)
                .append(", coupon_count=").append(coupon_count).append(", transaction_id=").append(transaction_id)
                .append(", out_trade_no=").append(out_trade_no).append(", attach=").append(attach).append(", time_end=")
                .append(time_end).append(", trade_state_desc=").append(trade_state_desc).append("]");
        return builder.toString();
    }

}
