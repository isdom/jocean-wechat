package org.jocean.wechat;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jocean.http.Interact;
import org.jocean.rpc.annotation.OnInteract;
import org.jocean.rpc.annotation.RpcBuilder;
import org.jocean.rpc.annotation.RpcResource;
import org.jocean.rpc.bean.BasicResponse;

import com.alibaba.fastjson.annotation.JSONField;

import rx.Observable;
import rx.Observable.Transformer;

// ref: https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay-1.shtml
//      https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay2_0.shtml
//      https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay2_1.shtml
//      https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay3_1.shtml
//      https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay3_2.shtml
//      https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay3_3.shtml
//      https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay4_0.shtml
//      https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay4_1.shtml
//      https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay4_2.shtml
//      https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay4_3.shtml
//      https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay7_0.shtml
//      https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay7_1.shtml
//      https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay7_2.shtml
//      https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay2_0.shtml
//      https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay2_0.shtml#part-6
//      https://pay.weixin.qq.com/docs/merchant/development/shangmi/introduction.html
//      https://pay.weixin.qq.com/docs/merchant/development/shangmi/key-and-certificate.html
//      https://pay.weixin.qq.com/docs/merchant/development/shangmi/guide.html
//      https://pay.weixin.qq.com/docs/merchant/development/shangmi/tools-and-sdk.html

public interface WechatPayAPIV3 {

    interface PayAPIV3Builder<BUILDER> {
        @RpcResource("signer")
        public BUILDER signer(final Transformer<Interact, Interact> signer);

        @HeaderParam("Accept")
        public BUILDER hdrAccept(final String accept);

        @HeaderParam("User-Agent")
        public BUILDER hdrUserAgent(final String ua);

        @HeaderParam("Accept-Language")
        public BUILDER hdrAcceptLanguage(final String lang);
    }

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
    interface PayAPIV3Response extends BasicResponse {
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
        public String getRequestID();

        @JSONField(deserialize = false)
        @HeaderParam("Request-ID")
        public void setRequestID(final String id);

        @JSONField(serialize = false)
        public String getWechatpayNonce();

        @JSONField(deserialize = false)
        @HeaderParam("Wechatpay-Nonce")
        public void setWechatpayNonce(final String nonce);

        @JSONField(serialize = false)
        public String getWechatpaySignature();

        @JSONField(deserialize = false)
        @HeaderParam("Wechatpay-Signature")
        public void setWechatpaySignature(final String signature);

        @JSONField(serialize = false)
        public String getWechatpayTimestamp();

        @JSONField(deserialize = false)
        @HeaderParam("Wechatpay-Timestamp")
        public void setWechatpayTimestamp(final String timestamp);

        @JSONField(serialize = false)
        public String getWechatpaySerial();

        @JSONField(deserialize = false)
        @HeaderParam("Wechatpay-Serial")
        public void setWechatpaySerial(final String serial);

        @JSONField(serialize = false)
        public String getWechatpaySignatureType();

        @JSONField(deserialize = false)
        @HeaderParam("Wechatpay-Signature-Type")
        public void setWechatpaySignatureType(final String signatureType);
    }

	// https://pay.weixin.qq.com/wiki/doc/apiv3/open/pay/chapter4_3_3.shtml
	// 商家转账到零钱开发指引
	// https://api.mch.weixin.qq.com/v3/transfer/batches
    interface TransferBatchesResponse extends PayAPIV3Response {
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
    interface TransferBatchesBuilder extends PayAPIV3Builder<TransferBatchesBuilder> {
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

        @POST
        @Path("https://api.mch.weixin.qq.com/v3/transfer/batches")
        @Consumes(MediaType.APPLICATION_JSON)
        // @OnResponse("org.jocean.wechat.WXProtocol.CHECK_WXRESP")
        @OnInteract("signer")
        Observable<TransferBatchesResponse> call();
    }

    interface EncryptCertificate {
        @JSONField(name = "algorithm")
        public String getAlgorithm();

        @JSONField(name = "algorithm")
        public void getAlgorithm(final String algorithm);

        @JSONField(name = "nonce")
        public String getNonce();

        @JSONField(name = "nonce")
        public void setNonce(final String nonce);

        @JSONField(name = "associated_data")
        public String getAssociatedData();

        @JSONField(name = "associated_data")
        public void setAssociatedData(final String associated_data);

        @JSONField(name = "ciphertext")
        public String getCiphertext();

        @JSONField(name = "ciphertext")
        public void setCiphertext(final String ciphertext);
    }

    interface CertificateInfo {
        @JSONField(name = "serial_no")
        public String getSerialNo();

        @JSONField(name = "serial_no")
        public void setSerialNo(final String serial_no);

        @JSONField(name = "effective_time")
        public String getEffectiveTime();

        @JSONField(name = "effective_time")
        public void setEffectiveTime(final String effective_time);

        @JSONField(name = "expire_time")
        public String getExpireTime();

        @JSONField(name = "expire_time")
        public void setExpireTime(final String expire_time);

        @JSONField(name = "encrypt_certificate")
        public EncryptCertificate getEncryptCertificate();

        @JSONField(name = "encrypt_certificate")
        public void setEncryptCertificate(final EncryptCertificate encrypt_certificate);
    }

    interface CertificatesResponse extends PayAPIV3Response {
        @JSONField(name = "data")
        public CertificateInfo[] getCertificateInfos();

        @JSONField(name = "data")
        public void setCertificateInfos(final CertificateInfo[] infos);
    }

    // https://pay.weixin.qq.com/wiki/doc/apiv3/apis/wechatpay5_1.shtml
    // GET 获取平台证书列表
    @RpcBuilder
    interface CertificatesBuilder extends PayAPIV3Builder<CertificatesBuilder> {
        @GET
        @Path("https://api.mch.weixin.qq.com/v3/certificates")
        @Consumes(MediaType.APPLICATION_JSON)
        // @OnResponse("org.jocean.wechat.WXProtocol.CHECK_WXRESP")
        @OnInteract("signer")
        Observable<CertificatesResponse> call();
    }

    //    "mchid": "1900001109",
    //    "out_batch_no": "plfk2020042013",
    //    "batch_id": "1030000071100999991182020050700019480001",
    //    "appid": "wxf636efh567hg4356",
    //    "batch_status": "ACCEPTED",
    //    "batch_type": "API",
    //    "batch_name": "2019年1月深圳分部报销单",
    //    "batch_remark": "2019年1月深圳分部报销单",
    //    "close_reason": "OVERDUE_CLOSE",
    //    "total_amount": 4000000,
    //    "total_num": 200,
    //    "create_time": "2015-05-20T13:29:35.120+08:00",
    //    "update_time": "2015-05-20T13:29:35.120+08:00",
    //    "success_amount": 3900000,
    //    "success_num": 199,
    //    "fail_amount": 100000,
    //    "fail_num": 1
    interface TransferBatch {
        @JSONField(name = "mchid")
        public String getMchid();

        @JSONField(name = "mchid")
        public void setMchid(final String mchid);

        @JSONField(name = "out_batch_no")
        public String getOutBatchNo();

        @JSONField(name = "out_batch_no")
        public void setOutBatchNo(final String out_batch_no);

        @JSONField(name = "batch_id")
        public String getBatchId();

        @JSONField(name = "batch_id")
        public void getBatchId(final String batch_id);

        @JSONField(name = "appid")
        public String getAppid();

        @JSONField(name = "appid")
        public void setAppid(final String appid);

        @JSONField(name = "batch_status")
        public String getBatchStatus();

        @JSONField(name = "batch_status")
        public void setBatchStatus(final String batch_status);

        @JSONField(name = "batch_type")
        public String getBatchType();

        @JSONField(name = "batch_type")
        public void setBatchType(final String batch_type);

        @JSONField(name = "batch_name")
        public String getBatchName();

        @JSONField(name = "batch_name")
        public void setBatchName(final String batch_name);

        @JSONField(name = "batch_remark")
        public String getBatchRemark();

        @JSONField(name = "batch_remark")
        public void setBatchRemark(final String batch_remark);

        @JSONField(name = "close_reason")
        public String getCloseReason();

        @JSONField(name = "close_reason")
        public void setCloseReason(final String close_reason);

        @JSONField(name = "total_amount")
        public String getTotalAmount();

        @JSONField(name = "total_amount")
        public void setTotalAmount(final int total_amount);

        @JSONField(name = "total_num")
        public String getTotalNum();

        @JSONField(name = "total_num")
        public void setTotalNum(final int total_num);

        @JSONField(name = "success_amount")
        public String getSuccessAmount();

        @JSONField(name = "success_amount")
        public void setSuccessAmount(final int success_amount);

        @JSONField(name = "success_num")
        public String getSuccessNum();

        @JSONField(name = "success_num")
        public void setSuccessNum(final int success_num);

        @JSONField(name = "fail_amount")
        public String getFailAmount();

        @JSONField(name = "fail_amount")
        public void setFailAmount(final int fail_amount);

        @JSONField(name = "fail_num")
        public String getFailNum();

        @JSONField(name = "fail_num")
        public void setFailNum(final int fail_num);

        @JSONField(name = "create_time")
        public String getCreateTime();

        @JSONField(name = "create_time")
        public void setCreateTime(final String create_time);

        @JSONField(name = "update_time")
        public String getUpdateTime();

        @JSONField(name = "update_time")
        public void setUpdateTime(final String update_time);
    }

    interface TransferDetailStatus {
        @JSONField(name = "detail_id")
        public String getDetailId();

        @JSONField(name = "detail_id")
        public void setDetailId(final String detail_id);

        //  商家明细单号
        //  字段名：       out_detail_no
        //  类型[长度限制]  string[1,32]
        //  必填：         是
        //  描述：         商户系统内部区分转账批次单下不同转账明细单的唯一标识，要求此参数只能由数字、大小写字母组成
        //  示例值:        x23zy545Bd5436
        @JSONField(name = "out_detail_no")
        public String getOutDetailNo();

        @JSONField(name = "out_detail_no")
        public void setOutDetailNo(final String out_detail_no);

        @JSONField(name = "detail_status")
        public String getDetailStatus();

        @JSONField(name = "detail_status")
        public void setDetailStatus(final String detail_status);
    }

    interface QueryTransferBatchesResponse extends PayAPIV3Response {
        @JSONField(name = "limit")
        public int getLimit();

        @JSONField(name = "limit")
        public void setLimit(final int limit);

        @JSONField(name = "offset")
        public int getOffset();

        @JSONField(name = "offset")
        public void setOffset(final int offset);

        @JSONField(name = "transfer_batch")
        public TransferBatch getTransferBatch();

        @JSONField(name = "transfer_batch")
        public void setTransferBatch(final TransferBatch transfer_batch);

        @JSONField(name = "transfer_detail_list")
        public TransferDetailStatus[] getTransferDetails();

        @JSONField(name = "transfer_detail_list")
        public void setTransferDetails(final TransferDetailStatus[] transferDetails);
    }

    // https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter4_3_5.shtml
    @RpcBuilder
    interface QueryTransferBatchesByOutBatchNoBuilder extends PayAPIV3Builder<QueryTransferBatchesByOutBatchNoBuilder> {

        // 商家批次单号   out_batch_no    string[1,32]    是   商户系统内部的商家批次单号，要求此参数只能由数字、大小写字母组成，在商户系统内部唯一
        @PathParam("out_batch_no")
        public QueryTransferBatchesByOutBatchNoBuilder outBatchNo(final String out_batch_no);

        // 是否查询转账明细单    need_query_detail   boolean 是   枚举值：
        //  true：是；
        //  false：否，默认否。
        //  商户可选择是否查询指定状态的转账明细单，当转账批次单状态为“FINISHED”（已完成）时，才会返回满足条件的转账明细单
        @QueryParam("need_query_detail")
        public QueryTransferBatchesByOutBatchNoBuilder needQueryDetail(final boolean need_query_detail);

        // 请求资源起始位置 offset  int 否   该次请求资源（转账明细单）的起始位置，从0开始，默认值为0
        //  示例值：1
        @QueryParam("offset")
        public QueryTransferBatchesByOutBatchNoBuilder offset(final int offset);

        // 最大资源条数  limit   int 否
        //  该次请求可返回的最大资源（转账明细单）条数，最小20条，最大100条，不传则默认20条。不足20条按实际条数返回
        //  示例值：20
        @QueryParam("limit")
        public QueryTransferBatchesByOutBatchNoBuilder limit(final int limit);

        // 明细状态    detail_status   string[1,32]    否
        // 查询指定状态的转账明细单，当need_query_detail为true时，该字段必填
        //      ALL：全部。需要同时查询转账成功和转账失败的明细单
        //      SUCCESS：转账成功。只查询转账成功的明细单
        //      FAIL：转账失败。只查询转账失败的明细单
        //      示例值：FAIL
        @QueryParam("detail_status")
        public QueryTransferBatchesByOutBatchNoBuilder detailStatus(final String detail_status);

        @GET
        @Path("https://api.mch.weixin.qq.com/v3/transfer/batches/out-batch-no/{out_batch_no}")
        @Consumes(MediaType.APPLICATION_JSON)
        // @OnResponse("org.jocean.wechat.WXProtocol.CHECK_WXRESP")
        @OnInteract("signer")
        Observable<QueryTransferBatchesResponse> call();
    }

    //    {
    //        "out_batch_no": "plfk2020042013",
    //        "batch_id": "1030000071100999991182020050700019480001",
    //        "appid": "wxf636efh567hg4356",
    //        "out_detail_no": "x23zy545Bd5436",
    //        "detail_id": "1040000071100999991182020050700019500100",
    //        "detail_status": "SUCCESS",
    //        "transfer_amount": 200000,
    //        "transfer_remark": "2020年4月报销",
    //        "fail_reason": "ACCOUNT_FROZEN",
    //        "openid": "o-MYE42l80oelYMDE34nYD456Xoy",
    //        "user_name": "757b340b45ebef5467rter35gf464344v3542sdf4t6re4tb4f54ty45t4yyry45",
    //        "initiate_time": "2015-05-20T13:29:35.120+08:00",
    //        "update_time": "2015-05-20T13:29:35.120+08:00"
    //    }
    interface QueryTransferDetailResponse extends PayAPIV3Response {

        // 商户号  mchid   string[1,32]    是   微信支付分配的商户号
        // 示例值：1900001109
        @JSONField(name = "mchid")
        public String getMchid();

        @JSONField(name = "mchid")
        public void setMchid(final String mchid);

        // 商家批次单号  out_batch_no    string[1,32]    是   商户系统内部的商家批次单号，在商户系统内部唯一
        // 示例值：plfk2020042013
        @JSONField(name = "out_batch_no")
        public String getOutBatchNo();

        @JSONField(name = "out_batch_no")
        public void setOutBatchNo(final String out_batch_no);

        // 微信批次单号  batch_id    string[1,64]    是   微信批次单号，微信商家转账系统返回的唯一标识
        // 示例值：1030000071100999991182020050700019480001
        @JSONField(name = "batch_id")
        public String getBatchId();

        @JSONField(name = "batch_id")
        public void setBatchId(final String batch_id);

        // 直连商户的appid  appid   string[1,32]    是   申请商户号的appid或商户号绑定的appid（企业号corpid即为此appid）
        // 示例值：wxf636efh567hg4356
        @JSONField(name = "appid")
        public String getAppid();

        @JSONField(name = "appid")
        public void setAppid(final String appid);

        //商家明细单号  out_detail_no   string[1,32]    是   商户系统内部区分转账批次单下不同转账明细单的唯一标识
        // 示例值：x23zy545Bd5436
        @JSONField(name = "out_detail_no")
        public String getOutDetailNo();

        @JSONField(name = "out_detail_no")
        public void setOutDetailNo(final String out_detail_no);

        // 微信明细单号  detail_id   string[1,64]    是   微信支付系统内部区分转账批次单下不同转账明细单的唯一标识
        // 示例值：1040000071100999991182020050700019500100
        @JSONField(name = "detail_id")
        public String getDetailId();

        @JSONField(name = "detail_id")
        public void setDetailId(final String detail_id);

        // 明细状态    detail_status   string[1,32]    是   枚举值：
        //  PROCESSING:转账中。正在处理中，转账结果尚未明确
        //  SUCCESS:转账成功
        //  FAIL:转账失败。需要确认失败原因后，再决定是否重新发起对该笔明细单的转账（并非整个转账批次单）
        //  示例值：SUCCESS
        @JSONField(name = "detail_status")
        public String getDetailStatus();

        @JSONField(name = "detail_status")
        public void setDetailStatus(final String detail_status);

        // 转账金额    transfer_amount int 是   转账金额单位为分
        // 示例值：200000
        @JSONField(name = "transfer_amount")
        public int getTransferAmount();

        @JSONField(name = "transfer_amount")
        public void setTransferAmount(final int transfer_amount);

        // 转账备注    transfer_remark string[1,32]    是   单条转账备注（微信用户会收到该备注），UTF8编码，最多允许32个字符
        // 示例值：2020年4月报销
        @JSONField(name = "transfer_remark")
        public String getTransferRemark();

        @JSONField(name = "transfer_remark")
        public void setTransferRemark(final String transfer_remark);

        // 明细失败原因  fail_reason string[1,64]    否   如果转账失败则有失败原因
        //  ACCOUNT_FROZEN：账户冻结
        //  REAL_NAME_CHECK_FAIL：用户未实名
        //  NAME_NOT_CORRECT：用户姓名校验失败
        //  OPENID_INVALID：Openid校验失败
        //  TRANSFER_QUOTA_EXCEED：超过用户单笔收款额度
        //  DAY_RECEIVED_QUOTA_EXCEED：超过用户单日收款额度
        //  MONTH_RECEIVED_QUOTA_EXCEED：超过用户单月收款额度
        //  DAY_RECEIVED_COUNT_EXCEED：超过用户单日收款次数
        //  PRODUCT_AUTH_CHECK_FAIL：产品权限校验失败
        //  OVERDUE_CLOSE：转账关闭
        //  ID_CARD_NOT_CORRECT：用户身份证校验失败
        //  ACCOUNT_NOT_EXIST：用户账户不存在
        //  TRANSFER_RISK：转账存在风险
        //  REALNAME_ACCOUNT_RECEIVED_QUOTA_EXCEED：用户账户收款受限，请引导用户在微信支付查看详情
        //  RECEIVE_ACCOUNT_NOT_PERMMIT：未配置该用户为转账收款人
        //  PAYER_ACCOUNT_ABNORMAL：商户账户付款受限，可前往商户平台-违约记录获取解除功能限制指引
        //  PAYEE_ACCOUNT_ABNORMAL：用户账户收款异常，请引导用户完善其在微信支付的身份信息以继续收款
        //  TRANSFER_REMARK_SET_FAIL：转账备注设置失败，请调整对应文案后重新再试
        //  示例值：ACCOUNT_FROZEN
        @JSONField(name = "fail_reason")
        public String getFailReason();

        @JSONField(name = "fail_reason")
        public void setFailReason(final String fail_reason);

        //  用户在直连商户应用下的用户标示 openid  string[1,128]   是   用户在直连商户appid下的唯一标识
        //  示例值：o-MYE42l80oelYMDE34nYD456Xoy
        @JSONField(name = "openid")
        public String getOpenid();

        @JSONField(name = "openid")
        public void setOpenid(final String openid);

        //  收款用户姓名  user_name   string[1,1024]  否   1、商户转账时传入了收款用户姓名、查询时会返回收款用户姓名；
        //  2、收款方姓名采用标准RSA算法，公钥由微信侧提供
        //  3、 该字段需进行加密处理，加密方法详见敏感信息加密说明。(提醒：必须在HTTP头中上送Wechatpay-Serial)
        //  示例值：757b340b45ebef5467rter35gf464344v3542sdf4t6re4tb4f54ty45t4yyry45
        @JSONField(name = "user_name")
        public String getUserName();

        @JSONField(name = "user_name")
        public void setUserName(final String user_name);

        //  转账发起时间  initiate_time   string[1,32]    是   转账发起的时间，遵循rfc3339标准格式，格式为yyyy-MM-DDTHH:mm:ss.sss+TIMEZONE，yyyy-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss.sss表示时分秒毫秒，TIMEZONE表示时区（+08:00表示东八区时间，领先UTC 8小时，即北京时间）。例如：2015-05-20T13:29:35.120+08:00表示北京时间2015年05月20日13点29分35秒
        //  示例值：2015-05-20T13:29:35.120+08:00
        @JSONField(name = "initiate_time")
        public String getInitiateTime();

        @JSONField(name = "initiate_time")
        public void setInitiateTime(final String initiate_time);

        //  明细更新时间  update_time string[1,32]    是   明细最后一次状态变更的时间，遵循rfc3339标准格式，格式为yyyy-MM-DDTHH:mm:ss.sss+TIMEZONE，yyyy-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss.sss表示时分秒毫秒，TIMEZONE表示时区（+08:00表示东八区时间，领先UTC 8小时，即北京时间）。例如：2015-05-20T13:29:35.120+08:00表示北京时间2015年05月20日13点29分35秒
        //  示例值：2015-05-20T13:29:35.120+08:00
        @JSONField(name = "update_time")
        public String getUpdateTime();

        @JSONField(name = "update_time")
        public void setUpdateTime(final String update_time);
    }

    // https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter4_3_6.shtml
    // 商家明细单号查询明细单API
    // 商户可以通过该接口查询单笔转账明细单。
    // 注意：
    // • API只支持查询最近30天内的转账明细单，30天之前的转账明细单请登录商户平台查询。
    // • 转账明细单中涉及金额的字段单位为“分”。
    // • 如果查询单号对应的数据不存在，那么数据不存在的原因可能是：
    //    （1）转账还在处理中；
    //    （2）转账批次单受理失败或还未开始处理导致转账明细单没有落地。
    // 在上述情况下，商户首先需要检查该商家明细单号是否确实是自己发起，以及是否是该批次下的，如果商户确认是自己发起且是该批次下的，
    // 则请商户不要直接当做转账失败处理，请商户隔几分钟再尝试查询（请勿转账和查询并发处理）。如果商户误把还在转账处理中的明细单直接当转账失败处理，
    // 商户应当自行承担因此产生的所有损失和责任。
    // 接口限频： 单个商户 50QPS，如果超过频率限制，会报错FREQUENCY_LIMITED，请降低频率请求。
    @RpcBuilder
    interface QueryTransferDetailByOutDetailNoBuilder extends PayAPIV3Builder<QueryTransferDetailByOutDetailNoBuilder> {

        // 商家批次单号   out_batch_no    string[1,32]    是   商户系统内部的商家批次单号，要求此参数只能由数字、大小写字母组成，在商户系统内部唯一
        // 示例值：plfk2020042013
        @PathParam("out_batch_no")
        public QueryTransferDetailByOutDetailNoBuilder outBatchNo(final String out_batch_no);

        // 商家明细单号   out_detail_no   string[1,32]    是   商户系统内部区分转账批次单下不同转账明细单的唯一标识，要求此参数只能由数字、大小写字母组成
        // 示例值：x23zy545Bd5436
        @PathParam("out_detail_no")
        public QueryTransferDetailByOutDetailNoBuilder outDetailNo(final String out_detail_no);

        @GET
        @Path("https://api.mch.weixin.qq.com/v3/transfer/batches/out-batch-no/{out_batch_no}/details/out-detail-no/{out_detail_no}")
        @Consumes(MediaType.APPLICATION_JSON)
        // @OnResponse("org.jocean.wechat.WXProtocol.CHECK_WXRESP")
        @OnInteract("signer")
        Observable<QueryTransferDetailResponse> call();
    }

    // https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter4_3_2.shtml
    // 微信批次单号查询批次单API
    // 商户可以通过该接口查询转账批次单以及指定状态的转账明细单。
    // 注意：
    //  • API只支持查询最近30天内的转账批次单，30天之前的转账批次单请登录商户平台查询。
    //  • 转账明细单只会在批次单完成的情况下返回，如果需要在批次处理过程中查询转账明细单，请通过转账明细单查询接口来查询。
    //  • 转账批次单中涉及金额的字段单位为“分”。
    //  • 如果查询单号对应的数据不存在，那么数据不存在的原因可能是：
    //    （1）批次还在受理中；
    //    （2）批次受理失败导致转账批次单没有落地。
    //    在上述情况下，商户首先需要检查该商家批次单号是否确实是自己发起的，如果商户确认是自己发起的，则请商户不要直接当做受理失败处理，请商户隔几分钟
    //      再尝试查询（请勿转账和查询并发处理），或者商户可以通过相同的商家批次单号再次发起转账。如果商户误把还在受理中的批次单直接当受理失败处理，
    //      商户应当自行承担因此产生的所有损失和责任。
    //  接口限频： 单个商户 50QPS，如果超过频率限制，会报错FREQUENCY_LIMITED，请降低频率请求。
    @RpcBuilder
    interface QueryTransferBatchesByBatchIdBuilder extends PayAPIV3Builder<QueryTransferBatchesByBatchIdBuilder> {

        // 微信批次单号  batch_id    string[1,64]    是   微信批次单号，微信商家转账系统返回的唯一标识
        //  示例值：1030000071100999991182020050700019480001
        @PathParam("batch_id")
        public QueryTransferBatchesByBatchIdBuilder batchId(final String batch_id);

        // 是否查询转账明细单    need_query_detail   boolean 是   枚举值：
        //  true：是；
        //  false：否，默认否。
        //  商户可选择是否查询指定状态的转账明细单，当转账批次单状态为“FINISHED”（已完成）时，才会返回满足条件的转账明细单
        @QueryParam("need_query_detail")
        public QueryTransferBatchesByBatchIdBuilder needQueryDetail(final boolean need_query_detail);

        // 请求资源起始位置 offset  int 否   该次请求资源（转账明细单）的起始位置，从0开始，默认值为0
        //  示例值：1
        @QueryParam("offset")
        public QueryTransferBatchesByBatchIdBuilder offset(final int offset);

        // 最大资源条数  limit   int 否
        //  该次请求可返回的最大资源（转账明细单）条数，最小20条，最大100条，不传则默认20条。不足20条按实际条数返回
        //  示例值：20
        @QueryParam("limit")
        public QueryTransferBatchesByBatchIdBuilder limit(final int limit);

        // 明细状态    detail_status   string[1,32]    否
        // 查询指定状态的转账明细单，当need_query_detail为true时，该字段必填
        //      ALL：全部。需要同时查询转账成功和转账失败的明细单
        //      SUCCESS：转账成功。只查询转账成功的明细单
        //      FAIL：转账失败。只查询转账失败的明细单
        //      示例值：FAIL
        @QueryParam("detail_status")
        public QueryTransferBatchesByBatchIdBuilder detailStatus(final String detail_status);

        @GET
        @Path("https://api.mch.weixin.qq.com/v3/transfer/batches/batch-id/{batch_id}")
        @Consumes(MediaType.APPLICATION_JSON)
        // @OnResponse("org.jocean.wechat.WXProtocol.CHECK_WXRESP")
        @OnInteract("signer")
        Observable<QueryTransferBatchesResponse> call();
    }

    // https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter4_3_3.shtml
    // 微信明细单号查询明细单API
    // 商户可以通过该接口查询单笔转账明细单。
    // 注意：
    // • API只支持查询最近30天内的转账明细单，30天之前的转账明细单请登录商户平台查询。
    // • 转账明细单中涉及金额的字段单位为“分”。
    // • 如果查询单号对应的数据不存在，那么数据不存在的原因可能是：
    //    （1）转账还在处理中；
    //    （2）转账批次单受理失败或还未开始处理导致转账明细单没有落地。
    // 在上述情况下，商户首先需要检查该商家明细单号是否确实是自己发起，以及是否是该批次下的，如果商户确认是自己发起且是该批次下的，
    // 则请商户不要直接当做转账失败处理，请商户隔几分钟再尝试查询（请勿转账和查询并发处理）。如果商户误把还在转账处理中的明细单直接当转账失败处理，
    // 商户应当自行承担因此产生的所有损失和责任。
    // 接口限频： 单个商户 50QPS，如果超过频率限制，会报错FREQUENCY_LIMITED，请降低频率请求。
    @RpcBuilder
    interface QueryTransferDetailByDetailIdBuilder extends PayAPIV3Builder<QueryTransferDetailByDetailIdBuilder> {

        // 微信批次单号   batch_id    string[1,64]    是   微信批次单号，微信商家转账系统返回的唯一标识
        // 示例值：1030000071100999991182020050700019480001
        @PathParam("batch_id")
        public QueryTransferDetailByDetailIdBuilder batchId(final String batch_id);

        // 微信明细单号   detail_id   string[1,64]    是   微信支付系统内部区分转账批次单下不同转账明细单的唯一标识
        // 示例值：1040000071100999991182020050700019500100
        @PathParam("detail_id")
        public QueryTransferDetailByDetailIdBuilder detailId(final String detail_id);

        @GET
        @Path("https://api.mch.weixin.qq.com/v3/transfer/batches/batch-id/{batch_id}/details/detail-id/{detail_id}")
        @Consumes(MediaType.APPLICATION_JSON)
        // @OnResponse("org.jocean.wechat.WXProtocol.CHECK_WXRESP")
        @OnInteract("signer")
        Observable<QueryTransferDetailResponse> call();
    }
}
