package com.tn.pay.constant;

/**
 * baofoo [协议支付]
 */
public class BaoFooBindPayConstant {

    //---------------------------------------------------------------
    // 协议支付参数
    //---------------------------------------------------------------

    public static final String SEND_TIME = "send_time";//报文发送日期时间yyyy-MM-dd hh:mm:ss
    public static final String MSG_ID = "msg_id";//商户流水号
    public static final String VERSION = "version";//版本号
    public static final String TERMINAL_ID = "terminal_id";//终端号
    public static final String MEMBER_ID = "member_id";//商户号

    public static final String TXN_TYPE = "txn_type";//交易类型 08
    public static final String TRANS_ID = "trans_id";//商户订单号
    public static final String DGTL_ENVLP = "dgtl_envlp";//数字信封
    public static final String USER_ID = "user_id";//用户在商户平台唯一ID (和绑卡时要一致)

    public static final String PROTOCOL_NO = "protocol_no";//签约协议号（密文）
    public static final String TXN_AMT = "txn_amt";//交易金额(单位：分)
    public static final String ORIG_TRANS_ID = "orig_trans_id";//原商户订单号
    public static final String ORIG_TRADE_DATE = "orig_trade_date";//原商户交易时间

    public static final String RISK_ITEM = "risk_item";//风控参数
    public static final String GOODSCATEGORY = "goodsCategory";//风控参数
    public static final String CHPAYIP = "chPayIp";//风控参数
    public static final String SIGNATURE = "signature";//签名域

    //---------------------------------------------------------------
    // 协议支付返回参数
    //---------------------------------------------------------------

    public static final String RESP_CODE = "resp_code";//应答码

    public static final String BIZ_RESP_CODE = "biz_resp_code";//业务返回码
    public static final String BIZ_RESP_MSG = "biz_resp_msg";//业务返回说明
    public static final String SUCC_TIME = "succ_time";//成功金额
    public static final String ORDER_ID = "order_id";//宝付订单号

    public static final String SUCC_AMT = "succ_amt";//成功金额(单位：分)

}
