package org.jocean.wechat;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.jocean.http.Interact;
import org.jocean.rpc.annotation.OnInteract;
import org.jocean.rpc.annotation.RpcBuilder;
import org.jocean.rpc.annotation.RpcResource;
import org.jocean.rpc.annotation.StatusCodeAware;

import com.alibaba.fastjson.annotation.JSONField;

import rx.Observable;
import rx.Observable.Transformer;

// ref: https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay-1.shtml
//      https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay2_0.shtml
//      https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay2_0.shtml#part-6

public interface WechatPayAPIV3 {
    //  错误信息
    // 微信支付API v3使用HTTP状态码来表示请求处理的结果。

    // 处理成功的请求，如果有应答的消息体将返回200，若没有应答的消息体将返回204。
    // 已经被成功接受待处理的请求，将返回202。
    // 请求处理失败时，如缺少必要的入参、支付时余额不足，将会返回4xx范围内的错误码。
    // 请求处理时发生了微信支付侧的服务系统错误，将返回500/501/503的状态码。这种情况比较少见。
    // 错误码和错误提示
    // 当请求处理失败时，除了HTTP状态码表示错误之外，API将在消息体返回错误相应说明具体的错误原因。

    // field: 指示错误参数的位置。当错误参数位于请求body的JSON时，填写指向参数的JSON Pointer 。当错误参数位于请求的url或者querystring时，填写参数的变量名。
    // value:错误的值
    // issue:具体错误原因
    public interface PayAPIV3Response {
        // code：详细错误码
        @JSONField(name = "code")
        public String getCode();

        // code：详细错误码
        @JSONField(name = "code")
        public void setCode(final String code);

        // message：错误描述，使用易理解的文字表示错误的原因。
        @JSONField(name = "message")
        public String getMessage();

        // message：错误描述，使用易理解的文字表示错误的原因。
        @JSONField(name = "message")
        public void setMessage(final String message);

        @JSONField(serialize = false)
        public String getWechatpaySerial();

        @JSONField(deserialize = false)
        @HeaderParam("Wechatpay-Serial")
        public void setWechatpaySerial(final String serial);

        @JSONField(serialize = false)
        public String getStatusCode();

        @JSONField(deserialize = false)
        @StatusCodeAware
        public void setStatusCode(final String code);
    }

	// https://pay.weixin.qq.com/wiki/doc/apiv3/open/pay/chapter4_3_3.shtml
	// 商家转账到零钱开发指引
	// https://api.mch.weixin.qq.com/v3/transfer/batches
    public interface TransferBatchesResponse extends PayAPIV3Response {
        // 错误码
        // 状态码 错误码 描述  解决方案
        // 500 SYSTEM_ERROR    系统错误    请勿更换商家转账批次单号，请使用相同参数再次调用API。否则可能造成资金损失
        // 401 APPID_MCHID_NOT_MATCH   商户号和appid没有绑定关系 商户号和appid没有绑定关系
        // 400 PARAM_ERROR 参数错误    根据错误提示，传入正确参数
        //      INVALID_REQUEST 请求参数符合参数格式，但不符合业务规则 根据错误提示，传入正确参数
        // 403 NO_AUTH 商户信息不合法 登录商户平台核对，传入正确信息
        //        NOT_ENOUGH  资金不足    商户账户资金不足，请充值后原单重试，请勿更换商家转账批次单号
        //        ACCOUNTERROR    商户账户付款受限    可前往商户平台-违约记录获取解除功能限制指引
        // 429 QUOTA_EXCEED    超出商户单日转账额度  超出商户单日转账额度，请核实产品设置是否准确
        //      FREQUENCY_LIMITED   频率超限    该笔请求未受理，请降低频率后原单重试，请勿更换商家转账批次单号


        //  商家批次单号
        //  字段名：       out_batch_no
        //  类型[长度限制]  string[1,32]
        //  必填：         是
        //  描述：         商户系统内部的商家批次单号
        //  示例值:        plfk2020042013
        @JSONField(name = "out_batch_no")
        public String getOutBatchNo();

        @JSONField(name = "out_batch_no")
        public void setOutBatchNo(final String out_batch_no);

        //  微信批次单号
        //  字段名：       batch_id
        //  类型[长度限制]  string[1,64]
        //  必填：         是
        //  描述：         微信批次单号，微信商家转账系统返回的唯一标识
        //  示例值:        1030000071100999991182020050700019480001
        @JSONField(name = "batch_id")
        public String getBatchId();

        @JSONField(name = "batch_id")
        public void setBatchId(final String batch_id);

        //  批次创建时间
        //  字段名：       create_time
        //  类型[长度限制]  string[1,32]
        //  必填：         是
        //  描述：         批次受理成功时返回，遵循rfc3339标准格式，格式为yyyy-MM-DDTHH:mm:ss.sss+TIMEZONE，
        //                  yyyy-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss.sss表示时分秒毫秒，
        //                  TIMEZONE表示时区（+08:00表示东八区时间，领先UTC 8小时，即北京时间）。
        //                  例如：2015-05-20T13:29:35.120+08:00 表示北京时间2015年05月20日13点29分35秒
        //  示例值:        2015-05-20T13:29:35.120+08:00
        @JSONField(name = "create_time")
        public String getCreateTime();

        @JSONField(name = "create_time")
        public void setCreateTime(final String create_time);
    }

    class TransferDetail {
        private String out_detail_no;
        private int transfer_amount;
        private String transfer_remark;
        private String openid;
        private String user_name = null;

        public TransferDetail(final String out_detail_no, final int transfer_amount, final String transfer_remark, final String openid) {
            this.out_detail_no = out_detail_no;
            this.transfer_amount = transfer_amount;
            this.transfer_remark = transfer_remark;
            this.openid = openid;
        }

        //  商家明细单号
        //  字段名：       out_detail_no
        //  类型[长度限制]  string[1,32]
        //  必填：         是
        //  描述：         商户系统内部区分转账批次单下不同转账明细单的唯一标识，要求此参数只能由数字、大小写字母组成
        //  示例值:        x23zy545Bd5436
        @JSONField(name = "out_detail_no")
        public String getOutDetailNo() {
            return this.out_detail_no;
        }

        @JSONField(name = "out_detail_no")
        public void setOutDetailNo(final String out_detail_no) {
            this.out_detail_no = out_detail_no;
        }

        //  转账金额
        //  字段名：       transfer_amount
        //  类型[长度限制]  int
        //  必填：         是
        //  描述：         转账金额单位为分
        //  示例值:        200000
        @JSONField(name = "transfer_amount")
        public int getTransferAmount() {
            return this.transfer_amount;
        }

        @JSONField(name = "transfer_amount")
        public void setTransferAmount(final int transfer_amount) {
            this.transfer_amount = transfer_amount;
        }

        //  转账备注
        //  字段名：       transfer_remark
        //  类型[长度限制]  string[1,32]
        //  必填：         是
        //  描述：         单条转账备注（微信用户会收到该备注），UTF8编码，最多允许32个字符
        //  示例值:        2020年4月报销
        @JSONField(name = "transfer_remark")
        public String gettransfer_remark() {
            return this.transfer_remark;
        }

        @JSONField(name = "transfer_remark")
        public void settransfer_remark(final String transfer_remark) {
            this.transfer_remark = transfer_remark;
        }

        //  用户在直连商户应用下的用户标示
        //  字段名：       openid
        //  类型[长度限制]  string[1,128]
        //  必填：         是
        //  描述：         openid是微信用户在公众号appid下的唯一用户标识（appid不同，则获取到的openid就不同），可用于永久标记一个用户
        //  示例值:        o-MYE42l80oelYMDE34nYD456Xoy
        @JSONField(name = "openid")
        public String getOpenid() {
            return this.openid;
        }

        @JSONField(name = "openid")
        public void setOpenid(final String openid) {
            this.openid = openid;
        }

        //  收款用户姓名
        //  字段名：       user_name
        //  类型[长度限制]  string[2,30]
        //  必填：         否
        //  描述：         1、明细转账金额 >= 2,000元，收款用户姓名必填；
        //                2、同一批次转账明细中，收款用户姓名字段需全部填写、或全部不填写；
        //                3、 若传入收款用户姓名，微信支付会校验用户openID与姓名是否一致，并提供电子回单；
        //                4、收款方姓名。采用标准RSA算法，公钥由微信侧提供
        //                5、该字段需进行加密处理，加密方法详见敏感信息加密说明。(提醒：必须在HTTP头中上送Wechatpay-Serial)
        //                6、商户需确保收集用户的姓名信息，以及向微信支付传输用户姓名和账号标识信息做一致性校验已合法征得用户授权
        //  示例值:        o-MYE42l80oelYMDE34nYD456Xoy
        @JSONField(name = "user_name")
        public String getUserName() {
            return this.user_name;
        }

        @JSONField(name = "user_name")
        public void setUserName(final String user_name) {
            this.user_name = user_name;
        }
    }

    // https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter4_3_1.shtml
    // 文档摘录：2022-09-04
    // 商户可以通过该接口同时向多个用户微信零钱进行转账操作。
    // 注意：
    //   • 商户上送敏感信息时使用微信支付平台公钥加密，证书序列号包含在请求HTTP头部的Wechatpay-Serial，详见接口规则。
    //   • 批量转账一旦发起后，不允许撤销，批次受理成功后开始执行转账。
    //   • 转账批次单中涉及金额的字段单位为“分”。
    //   • 当返回错误码为“SYSTEM_ERROR”时，请不要更换商家批次单号，一定要使用原商家批次单号重试，否则可能造成重复转账等资金风险。
    //   • 微信支付视任何不同“商家批次单号（out_batch_no）”的请求为一个全新的批次。在未查询到明确的转账批次单处理结果之前，请勿修改商家批次单号重新提交！如有发生，商户应当自行承担因此产生的所有损失和责任。
    //   • 请商户在自身的系统中合理设置转账频次并做好并发控制，防范错付风险。
    //   • 因商户自身系统设置存在问题导致的资金损失，由商户自行承担。
    @RpcBuilder
    interface TransferBatchesBuilder {
        //  直连商户的appid
        //  字段名：       appid
        //  类型[长度限制]  string[1,32]
        //  必填：         是
        //  描述：         申请商户号的appid或商户号绑定的appid（企业号corpid即为此appid）
        //  示例值:        wxf636efh567hg4356
        @JSONField(name="appid")
        public TransferBatchesBuilder appid(final String appid);

        //  商家批次单号
        //  字段名：       out_batch_no
        //  类型[长度限制]  string[1,32]
        //  必填：         是
        //  描述：         商户系统内部的商家批次单号，要求此参数只能由数字、大小写字母组成，在商户系统内部唯一
        //  示例值:        plfk2020042013
        @JSONField(name="out_batch_no")
        public TransferBatchesBuilder outBatchNo(final String out_batch_no);

        //  批次名称
        //  字段名：       batch_name
        //  类型[长度限制]  string[1,32]
        //  必填：         是
        //  描述：         该笔批量转账的名称
        //  示例值:        2019年1月深圳分部报销单
        @JSONField(name="batch_name")
        public TransferBatchesBuilder batchName(final String batch_name);

        //  批次备注
        //  字段名：       batch_remark
        //  类型[长度限制]  string[1,32]
        //  必填：         是
        //  描述：         转账说明，UTF8编码，最多允许32个字符
        //  示例值:        2019年1月深圳分部报销单
        @JSONField(name="batch_remark")
        public TransferBatchesBuilder batchRemark(final String batch_remark);

        //  转账总金额
        //  字段名：       total_amount
        //  类型[长度限制]  int
        //  必填：         是
        //  描述：         转账金额单位为“分”。转账总金额必须与批次内所有明细转账金额之和保持一致，否则无法发起转账操作
        //  示例值:        4000000
        @JSONField(name="total_amount")
        public TransferBatchesBuilder totalAmount(final int total_amount);

        //  转账总笔数
        //  字段名：       total_num
        //  类型[长度限制]  int
        //  必填：         是
        //  描述：         一个转账批次单最多发起三千笔转账。转账总笔数必须与批次内所有明细之和保持一致，否则无法发起转账操作
        //  示例值:        200
        @JSONField(name="total_num")
        public TransferBatchesBuilder totalNum(final int total_num);

        //  转账明细列表
        //  字段名：       transfer_detail_list
        //  类型[长度限制]  array
        //  必填：         是
        //  描述：         发起批量转账的明细列表，最多三千笔
        @JSONField(name="transfer_detail_list")
        public TransferBatchesBuilder transferDetails(final TransferDetail[] transfer_detail_list);

        @RpcResource("signer")
        public TransferBatchesBuilder signer(final Transformer<Interact, Interact> signer);

        @HeaderParam("Accept")
        public TransferBatchesBuilder hdrAccept(final String accept);

        @HeaderParam("User-Agent")
        public TransferBatchesBuilder hdrUserAgent(final String ua);

        @HeaderParam("Accept-Language")
        public TransferBatchesBuilder hdrAcceptLanguage(final String lang);

        @POST
        @Path("https://api.mch.weixin.qq.com/v3/transfer/batches")
        @Consumes(MediaType.APPLICATION_JSON)
        // @OnResponse("org.jocean.wechat.WXProtocol.CHECK_WXRESP")
        @OnInteract("signer")
        Observable<TransferBatchesResponse> call();
    }
}
