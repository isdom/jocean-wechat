package org.jocean.wechat.spi;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * 统一下单API需要提交的数据
 * https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=9_1
 * https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_sl_api.php?chapter=9_1
 */
@Consumes({"application/xml","text/xml"})
@JacksonXmlRootElement(localName="xml")
@Path("https://api.mch.weixin.qq.com/pay/unifiedorder")
public class UnifiedOrderRequest {

    // 服务商ID    appid   是   String(32)  wxd678efh567hg6787  微信分配的公众账号ID
    @JacksonXmlProperty(localName="appid")
    public String getAppid() {
        return appid;
    }

    // 服务商ID    appid   是   String(32)  wxd678efh567hg6787  微信分配的公众账号ID
    @JacksonXmlProperty(localName="appid")
    public void setAppid(final String appid) {
        this.appid = appid;
    }

    // 商户号  mch_id  是   String(32)  1230000109  微信支付分配的商户号
    @JacksonXmlProperty(localName="mch_id")
    public String getMchId() {
        return mch_id;
    }

    // 商户号  mch_id  是   String(32)  1230000109  微信支付分配的商户号
    @JacksonXmlProperty(localName="mch_id")
    public void setMchId(final String mch_id) {
        this.mch_id = mch_id;
    }

    // 小程序的APPID    sub_appid   是   String(32)  wx8888888888888888  当前调起支付的小程序APPID
    @JacksonXmlProperty(localName="sub_appid")
    public String getSubAppid() {
        return this.sub_appid;
    }

    // 小程序的APPID    sub_appid   是   String(32)  wx8888888888888888  当前调起支付的小程序APPID
    @JacksonXmlProperty(localName="sub_appid")
    public void setSubAppid(final String sub_appid) {
        this.sub_appid = sub_appid;
    }

    // 子商户号    sub_mch_id  是   String(32)  1900000109  微信支付分配的子商户号
    @JacksonXmlProperty(localName="sub_mch_id")
    public String getSubMchId() {
        return this.sub_mch_id;
    }

    // 子商户号    sub_mch_id  是   String(32)  1900000109  微信支付分配的子商户号
    @JacksonXmlProperty(localName="sub_mch_id")
    public void setSubMchId(final String sub_mch_id) {
        this.sub_mch_id = sub_mch_id;
    }

    //  设备号 device_info 否   String(32)  013467007045764 终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
    @JacksonXmlProperty(localName="device_info")
    public String getDeviceInfo() {
        return device_info;
    }

    //  设备号 device_info 否   String(32)  013467007045764 终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
    @JacksonXmlProperty(localName="device_info")
    public void setDeviceInfo(final String device_info) {
        this.device_info = device_info;
    }

    //  随机字符串   nonce_str   是   String(32)  5K8264ILTKCH16CQ2502SI8ZNMTM67VS    随机字符串，不长于32位。推荐随机数生成算法
    @JacksonXmlProperty(localName="nonce_str")
    public String getNonceStr() {
        return nonce_str;
    }

    //  随机字符串   nonce_str   是   String(32)  5K8264ILTKCH16CQ2502SI8ZNMTM67VS    随机字符串，不长于32位。推荐随机数生成算法
    @JacksonXmlProperty(localName="nonce_str")
    public void setNonceStr(final String nonce_str) {
        this.nonce_str = nonce_str;
    }

    //  签名  sign    是   String(32)  C380BEC2BFD727A4B6845133519F3AD6    签名，详见签名生成算法
    @JacksonXmlProperty(localName="sign")
    public String getSign() {
        return sign;
    }

    //  签名  sign    是   String(32)  C380BEC2BFD727A4B6845133519F3AD6    签名，详见签名生成算法
    @JacksonXmlProperty(localName="sign")
    public void setSign(final String sign) {
        this.sign = sign;
    }

    //  签名类型    sign_type   否   String(32)  HMAC-SHA256 签名类型，目前支持HMAC-SHA256和MD5，默认为MD5
    @JacksonXmlProperty(localName="sign_type")
    public String getSignType() {
        return this.sign_type;
    }

    //  签名类型    sign_type   否   String(32)  HMAC-SHA256 签名类型，目前支持HMAC-SHA256和MD5，默认为MD5
    @JacksonXmlProperty(localName="sign_type")
    public void setSignType(final String sign_type) {
        this.sign_type = sign_type;
    }

    // 商品描述 body    是   String(128) 腾讯充值中心-QQ会员充值    商品简单描述，该字段须严格按照规范传递，具体请见参数规定
    @JacksonXmlProperty(localName="body")
    public String getBody() {
        return this.body;
    }

    // 商品描述 body    是   String(128) 腾讯充值中心-QQ会员充值    商品简单描述，该字段须严格按照规范传递，具体请见参数规定
    @JacksonXmlProperty(localName="body")
    public void setBody(final String body) {
        this.body = body;
    }

    // 商品详情    detail  否   String(6000)        商品详细描述，对于使用单品优惠的商户，改字段必须按照规范上传，详见“单品优惠参数说明”
    @JacksonXmlProperty(localName="detail")
    public String getDetail() {
        return this.detail;
    }

    // 商品详情    detail  否   String(6000)        商品详细描述，对于使用单品优惠的商户，改字段必须按照规范上传，详见“单品优惠参数说明”
    @JacksonXmlProperty(localName="detail")
    public void setDetail(final String detail) {
        this.detail = detail;
    }

    // 附加数据 attach  否   String(127) 说明  附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
    @JacksonXmlProperty(localName="attach")
    public String getAttach() {
        return this.attach;
    }

    // 附加数据 attach  否   String(127) 说明  附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
    @JacksonXmlProperty(localName="attach")
    public void setAttach(final String attach) {
        this.attach = attach;
    }

    // 商户订单号    out_trade_no    是   String(32)  1217752501201407033233368018    商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*且在同一个商户号下唯一。详见商户订单号
    @JacksonXmlProperty(localName="out_trade_no")
    public String getOutTradeNo() {
        return out_trade_no;
    }

    // 商户订单号    out_trade_no    是   String(32)  1217752501201407033233368018    商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*且在同一个商户号下唯一。详见商户订单号
    @JacksonXmlProperty(localName="out_trade_no")
    public void setOutTradeNo(final String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    // 货币类型 fee_type    否   String(16)  CNY 符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
    @JacksonXmlProperty(localName="fee_type")
    public String getFeeType() {
        return fee_type;
    }

    // 货币类型 fee_type    否   String(16)  CNY 符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
    @JacksonXmlProperty(localName="fee_type")
    public void setFeeType(final String fee_type) {
        this.fee_type = fee_type;
    }

    // 总金额  total_fee   是   Int 888 订单总金额，只能为整数，详见支付金额
    @JacksonXmlProperty(localName="total_fee")
    public int getTotalFee() {
        return total_fee;
    }

    // 总金额  total_fee   是   Int 888 订单总金额，只能为整数，详见支付金额
    @JacksonXmlProperty(localName="total_fee")
    public void setTotalFee(final int total_fee) {
        this.total_fee = total_fee;
    }

    // 终端IP spbill_create_ip    是   String(16)  123.12.12.123   APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
    @JacksonXmlProperty(localName="spbill_create_ip")
    public String getSpbillCreateIp() {
        return spbill_create_ip;
    }

    // 终端IP spbill_create_ip    是   String(16)  123.12.12.123   APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
    @JacksonXmlProperty(localName="spbill_create_ip")
    public void setSpbillCreateIp(final String spbill_create_ip) {
        this.spbill_create_ip = spbill_create_ip;
    }

    // 交易起始时间   time_start  否   String(14)  20091225091010  订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
    @JacksonXmlProperty(localName="time_start")
    public String getTimeStart() {
        return time_start;
    }

    // 交易起始时间   time_start  否   String(14)  20091225091010  订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
    @JacksonXmlProperty(localName="time_start")
    public void setTimeStart(final String time_start) {
        this.time_start = time_start;
    }

    // 交易结束时间   time_expire 否   String(14)  20091227091010
    // 订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。订单失效时间是针对订单号而言的，由于在请求支付的时候有一个必传参数prepay_id只有两小时的有效期，所以在重入时间超过2小时的时候需要重新请求下单接口获取新的prepay_id。其他详见时间规则
    // 建议：最短失效时间间隔大于1分钟
    @JacksonXmlProperty(localName="time_expire")
    public String getTimeExpire() {
        return time_expire;
    }

    // 交易结束时间   time_expire 否   String(14)  20091227091010
    // 订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。订单失效时间是针对订单号而言的，由于在请求支付的时候有一个必传参数prepay_id只有两小时的有效期，所以在重入时间超过2小时的时候需要重新请求下单接口获取新的prepay_id。其他详见时间规则
    // 建议：最短失效时间间隔大于1分钟
    @JacksonXmlProperty(localName="time_expire")
    public void setTimeExpire(final String time_expire) {
        this.time_expire = time_expire;
    }

    // 订单优惠标记   goods_tag   否   String(32)  WXG 订单优惠标记，代金券或立减优惠功能的参数，说明详见代金券或立减优惠
    @JacksonXmlProperty(localName="goods_tag")
    public String getGoodsTag() {
        return goods_tag;
    }

    // 订单优惠标记   goods_tag   否   String(32)  WXG 订单优惠标记，代金券或立减优惠功能的参数，说明详见代金券或立减优惠
    @JacksonXmlProperty(localName="goods_tag")
    public void setGoodsTag(final String goods_tag) {
        this.goods_tag = goods_tag;
    }

    // 通知地址 notify_url  是   String(256) http://www.weixin.qq.com/wxpay/pay.php  接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
    @JacksonXmlProperty(localName="notify_url")
    public String getNotifyUrl() {
        return notify_url;
    }

    // 通知地址 notify_url  是   String(256) http://www.weixin.qq.com/wxpay/pay.php  接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
    @JacksonXmlProperty(localName="notify_url")
    public void setNotifyUrl(final String notify_url) {
        this.notify_url = notify_url;
    }

    // 交易类型 trade_type  是   String(16)  JSAPI   小程序取值如下：JSAPI，详细说明见参数规定
    @JacksonXmlProperty(localName="trade_type")
    public String getTradeType() {
        return trade_type;
    }

    // 交易类型 trade_type  是   String(16)  JSAPI   小程序取值如下：JSAPI，详细说明见参数规定
    @JacksonXmlProperty(localName="trade_type")
    public void setTradeType(final String trade_type) {
        this.trade_type = trade_type;
    }

    // 用户标识 openid  否   String(128) oUpF8uMuAJO_M2pxb1Q9zNjWeS6o    trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。openid如何获取，可参考【获取openid】。
    @JacksonXmlProperty(localName="openid")
    public String getOpenid() {
        return openid;
    }

    // 用户标识 openid  否   String(128) oUpF8uMuAJO_M2pxb1Q9zNjWeS6o    trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。openid如何获取，可参考【获取openid】。
    @JacksonXmlProperty(localName="openid")
    public void setOpenid(final String openid) {
        this.openid = openid;
    }

    // 用户子标识    sub_openid  否   String(128) oUpF8uMuAJO_M2pxb1Q9zNjWeS6o    trade_type=JSAPI，此参数必传，用户在子商户appid下的唯一标识。openid和sub_openid可以选传其中之一，如果选择传sub_openid,则必须传sub_appid。下单前需要调用【网页授权获取用户信息】接口获取到用户的Openid。
    @JacksonXmlProperty(localName="sub_openid")
    public String getSubOpenid() {
        return sub_openid;
    }

    // 用户子标识    sub_openid  否   String(128) oUpF8uMuAJO_M2pxb1Q9zNjWeS6o    trade_type=JSAPI，此参数必传，用户在子商户appid下的唯一标识。openid和sub_openid可以选传其中之一，如果选择传sub_openid,则必须传sub_appid。下单前需要调用【网页授权获取用户信息】接口获取到用户的Openid。
    @JacksonXmlProperty(localName="sub_openid")
    public void setSubOpenid(final String sub_openid) {
        this.sub_openid = sub_openid;
    }

    private String appid;
    private String mch_id ;
    private String sub_appid;
    private String sub_mch_id;
    private String device_info = "WEB";//终端设备号(门店号或收银设备ID)，默认请传"WEB"

    private String attach;//附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
    private String sign;//签名，详见签名生成算法
    private String sign_type = "MD5";
    private String body ;//商品或支付单简要描述
    private String detail ;//商品名称明细列表

    private String nonce_str ;
    private String notify_url;
    private String out_trade_no;//商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
    private int total_fee;
    private String fee_type = "CNY";
    private String spbill_create_ip;//APP和网页支付提交用户端ip

    private String time_start;//订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
    private String time_expire;//订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。其他详见时间规则
    private String goods_tag;//商品标记，代金券或立减优惠功能的参数，说明详见代金券或立减优惠
    private String trade_type = "JSAPI";//trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义。

    private String openid;
    private String sub_openid;

    @Override
    public String toString() {
        return new StringBuilder().append("UnifiedOrderRequest [sign_type=").append(sign_type).append(", appid=").append(appid)
                .append(", mch_id=").append(mch_id).append(", sub_appid=").append(sub_appid).append(", sub_mch_id=")
                .append(sub_mch_id).append(", device_info=").append(device_info).append(", attach=").append(attach)
                .append(", sign=").append(sign).append(", body=").append(body).append(", detail=").append(detail)
                .append(", nonce_str=").append(nonce_str).append(", notify_url=").append(notify_url)
                .append(", out_trade_no=").append(out_trade_no).append(", total_fee=").append(total_fee)
                .append(", fee_type=").append(fee_type).append(", spbill_create_ip=").append(spbill_create_ip)
                .append(", time_start=").append(time_start).append(", time_expire=").append(time_expire)
                .append(", goods_tag=").append(goods_tag).append(", trade_type=").append(trade_type).append(", openid=")
                .append(openid).append(", sub_openid=").append(sub_openid).append("]").toString();
    }
}
