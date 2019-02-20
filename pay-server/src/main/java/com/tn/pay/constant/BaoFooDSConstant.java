package com.tn.pay.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * baofoo [代扣]
 */
public class BaoFooDSConstant {

    //---------------------------------------------------------------
    // 代扣参数
    //---------------------------------------------------------------

    public static final String TRANS_ID = "trans_id";//商户订单号,一单唯一
    public static final String ORIG_TRANS_ID = "orig_trans_id";//查询商户订单号,一单唯一
    public static final String TRANS_SERIAL_NO = "trans_serial_no";//商户流水号,每次请求都不可重复
    public static final String TXN_SUB_TYPE = "txn_sub_type";//交易子类 默认[代付73，代付查询79]
    public static final String BIZ_TYPE = "biz_type";//接入类型 默认0000
    public static final String PAY_CM = "pay_cm";//默认为：2 1:不进行信息严格验证 2:对四要素进行严格校验
    public static final String TERMINAL_ID = "terminal_id";//终端号
    public static final String MEMBER_ID = "member_id";//商户号
    public static final String PAY_CODE = "pay_code";//银行编码
    public static final String TXN_AMT = "txn_amt";//交易金额(单位：分)
    public static final String TRADE_DATE = "trade_date";//订单日期(yyyyMMddhhmmss)
    public static final String ORIG_TRADE_DATE = "orig_trade_date";//必填 查询订单日期(yyyyMMddhhmmss)
    public static final String REQ_RESERVED = "req_reserved";//选填 [success_time]yyyyMMddHHmmss[/success_time]
    //四要素和协议号只是存在一个，同时存在只使用签约协议号
    public static final String ACC_NO = "acc_no";//银行卡卡号
    public static final String ID_CARD = "id_card";//身份证号
    public static final String ID_CARD_TYPE = "id_card_type";//证件类型 01 身份证
    public static final String ID_HOLDER = "id_holder";//持卡人姓名
    public static final String MOBILE = "mobile";//银行卡绑定手机号
    public static final String PROTOCOL_NO = "protocol_no";//签约协议号

    public static final String RESP_CODE = "resp_code";//应答码
    public static final String RESP_MSG = "resp_msg";//应答信息
    public static final String TRANS_NO = "trans_no";//宝付交易号
    public static final String SUCC_AMT = "succ_amt";//成功金额(单位：分)
    public static final String ORDER_STAT = "order_stat";//S：交易成功 F：交易失败 I：处理中 FF:：交易失败；订单暂不存在
    public static final String SUCCESS_TIME = "success_time";//成功交易时间

    //---------------------------------------------------------------
    // 代扣返回码
    //---------------------------------------------------------------
    public static final String SUCCESS_OK = "0000";//代付交易成功
    public static final String SUCCESS_REPEAT = "BF00114";//订单已支付成功，请勿重复支付

    public static final String SUCCESS_EXCEPTION = "BF00100";//系统异常，请联系宝付
    public static final String SUCCESS_BUSY = "BF00112";//系统繁忙，请稍后再试
    public static final String SUCCESS_UNKNOWN = "BF00113";//交易结果未知，请稍后查询
    public static final String SUCCESS_PROCESSING = "BF00115";//交易处理中，请稍后查询
    public static final String SUCCESS_RISK = "BF00144";//该交易有风险,订单处理中
    public static final String SUCCESS_OVERTIME = "BF00202";//交易超时，请稍后查询

    //成功状态
    public static final List<String> SUCCESS = new ArrayList<>();
    //待查询状态
    public static final List<String> WAIT_QUERY = new ArrayList<>();
    //其他 视为受理但失败

    static {
        SUCCESS.add(SUCCESS_OK);
        SUCCESS.add(SUCCESS_REPEAT);

        WAIT_QUERY.add(SUCCESS_EXCEPTION);
        WAIT_QUERY.add(SUCCESS_BUSY);
        WAIT_QUERY.add(SUCCESS_UNKNOWN);
        WAIT_QUERY.add(SUCCESS_PROCESSING);
        WAIT_QUERY.add(SUCCESS_RISK);
        WAIT_QUERY.add(SUCCESS_OVERTIME);
    }


}
