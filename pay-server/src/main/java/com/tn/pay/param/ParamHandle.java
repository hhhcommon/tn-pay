package com.tn.pay.param;

import com.tn.pay.module.PaymentChannel;
import com.tn.pay.module.WaitPaymentRecord;

import java.util.Map;

/**
 * 支付参数组装接口
 */
public interface ParamHandle {

    Map<String, Object> generateParam(WaitPaymentRecord record, PaymentChannel channel) throws Exception;
}
