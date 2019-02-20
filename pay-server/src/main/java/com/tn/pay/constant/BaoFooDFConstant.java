package com.tn.pay.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * baofoo [代付]
 */
public class BaoFooDFConstant {

    //---------------------------------------------------------------
    // 代付参数
    //---------------------------------------------------------------

    public static final String TRANS_CONTENT = "trans_content";
    public static final String TRANS_HEAD = "trans_head";
    public static final String TRANS_REQDATAS = "trans_reqDatas";
    public static final String TRANS_REQDATA = "trans_reqData";

    public static final String RETURN_CODE = "return_code";//响应码
    public static final String RETURN_MSG = "return_msg";//响应信息

    public static final String TRANS_NO = "trans_no";//商户订单号,一单唯一
    public static final String TRANS_ORDERID = "trans_orderid";//宝付订单号
    public static final String TRANS_BATCHID = "trans_batchid";//宝付批次号

    public static final String TRANS_MONEY = "trans_money";//转账金额
    public static final String TO_ACC_NAME = "to_acc_name";//收款人姓名
    public static final String TO_ACC_NO = "to_acc_no";//收款人银行帐号
    public static final String TO_BANK_NAME = "to_bank_name";//收款人银行名称
    public static final String TO_ACC_DEPT = "to_acc_dept";//收款人开户行机构名

    public static final String TRANS_FEE = "trans_fee";//交易手续费
    public static final String STATE = "state";//0：转账中；1：转账成功；-1：转账失败；2：转账退款；
    public static final String TRANS_REMARK = "trans_remark";//错误信息备注
    public static final String TRANS_STARTTIME = "trans_starttime";//交易申请时间
    public static final String TRANS_ENDTIME = "trans_endtime";//交易完成时间


    //---------------------------------------------------------------
    // 代付返回码
    //---------------------------------------------------------------
    public static final String SUCCESS = "0000";//代付请求交易成功（交易已受理）
    public static final String SUCCESS_UNKNOWN = "0300";//代付交易未明，请发起该笔订单查询
    public static final String SUCCESS_NOT_FOUND = "0401";//代付交易查证信息不存在(请根据对账文件)
    public static final String SUCCESS_BUSY = "0999";//代付主机系统繁忙

    public static final String SUCCESS_OK = "200";//代付交易成功

    //0001-0199 有关商户端上送报文格式检查导致的错误
    public static final List<String> ERRORS = new ArrayList<>();
    //0201-0599 有关商户相关业务检查导致的错误
    public static final List<String> FAILS = new ArrayList<>();
    //其他 视为受理但失败

    //等待查询状态
    public static final List<String> WAIT_QUERY = new ArrayList<>();

    static {
        for (int i = 1; i <= 199; i++) {
            ERRORS.add(String.format("%04d", i));
        }
        for (int i = 201; i <= 599; i++) {
            FAILS.add(String.format("%04d", i));
        }
        WAIT_QUERY.add(SUCCESS);
        WAIT_QUERY.add(SUCCESS_UNKNOWN);
        WAIT_QUERY.add(SUCCESS_NOT_FOUND);
        WAIT_QUERY.add(SUCCESS_BUSY);
    }

}
