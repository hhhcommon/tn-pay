package com.tn.pay.param.baofoo;

import com.tn.pay.module.WaitPaymentRecord;

import java.util.HashMap;
import java.util.Map;

import static com.tn.pay.constant.BaoFooDFConstant.*;

/**
 * 宝付[代付]参数组装类
 */
public class BaoFooDFParamHandle extends BaoFooDFHandle {

    protected Map<String, String> getData(WaitPaymentRecord waitPaymentRecord) {
        Map<String, String> data = new HashMap<>();
        data.put(TRANS_NO, waitPaymentRecord.getTransNo());//商户订单号
        data.put(TRANS_MONEY, String.valueOf(waitPaymentRecord.getPaymentAmount()));//转账金额(单位：元)
        data.put(TO_ACC_NAME, waitPaymentRecord.getAccountName());//收款人姓名
        data.put(TO_ACC_NO, waitPaymentRecord.getCustAccount());//收款人银行帐号
        data.put(TO_BANK_NAME, waitPaymentRecord.getBankName());//收款人银行名称
        return data;
    }
}
