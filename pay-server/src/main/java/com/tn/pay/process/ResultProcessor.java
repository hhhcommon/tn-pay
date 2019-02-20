package com.tn.pay.process;

import com.alibaba.fastjson.JSON;
import com.tn.pay.module.PaymentChannel;
import com.tn.pay.module.WaitPaymentRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 支付结果处理父类
 */
@Slf4j
@Component
public abstract class ResultProcessor {

    public abstract void process(WaitPaymentRecord record, PaymentChannel channel, Map<String, String> map) throws Exception;

    /**
     * 通知账务(放款成功/放款失败)
     */
    @Async
    public void noticeFinance(WaitPaymentRecord record) {
        log.info("账务回调-指令号:{}，方法入参：{}", record.getCallFlow(), JSON.toJSONString(record));
    }
}
