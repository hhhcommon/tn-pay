package com.tn.pay.result;

import com.tn.pay.module.PaymentChannel;

import java.util.Map;

/**
 * baofoo 组装结果接口
 */
public interface ResultHandle {
    Map<String, String> generateResult(PaymentChannel channel, String result) throws Exception;
}
