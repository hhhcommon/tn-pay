package com.tn.pay.param.baofoo;

import com.tn.pay.module.WaitPaymentRecord;

import java.util.HashMap;
import java.util.Map;

import static com.tn.pay.constant.BaoFooDFConstant.TRANS_NO;

/**
 * 宝付[代付]查询参数组装类
 */
public class BaoFooDFQueryHandle extends BaoFooDFHandle {

    @Override
    protected Map<String, String> getData(WaitPaymentRecord waitPaymentRecord) {
        Map<String, String> data = new HashMap<>();
        data.put(TRANS_NO, waitPaymentRecord.getTransNo());
        return data;
    }
}
