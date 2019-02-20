package com.tn.pay.process.cib;

import com.tn.pay.constant.CibConstant;
import com.tn.pay.enums.PaymentStatusEnum;
import com.tn.pay.module.PaymentChannel;
import com.tn.pay.module.WaitPaymentRecord;
import com.tn.pay.process.ResultProcessor;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 支付结果处理类
 */
public class CibResultProcessor extends ResultProcessor {
    @Override
    public void process(WaitPaymentRecord record, PaymentChannel channel, Map<String, String> map) throws Exception {
        String retCode = map.get("retCode");
        String retMsg = map.get("retMsg");
        String serialNo = map.get("serialNo");
        String traceNo = map.get("traceNo");
        String stateTime = map.get("stateTime");
        String tranStatus = map.get("tranStatus");

        if (CibConstant.SUCCESS.equals(retCode) && StringUtils.isNotEmpty(tranStatus)) {
            if ("0".equals(tranStatus)) {
                record.setPaymentStatus(PaymentStatusEnum.FAILURE_3.getStatus());
                record.setPaymentMsg(retMsg);
                noticeFinance(record);
            } else if ("1".equals(tranStatus)) {
                record.setPaymentStatus(PaymentStatusEnum.SUCCESS_2.getStatus());
                record.setPaymentMsg(retMsg);
                record.setPaymentTime(stateTime);
                noticeFinance(record);
            } else if ("2".equals(tranStatus)) {
                record.setPaymentStatus(PaymentStatusEnum.WAIT_PAY_1.getStatus());
                record.setPaymentMsg(retMsg);
            }
        } else if (CibConstant.SUCCESS.equals(retCode)) {
            record.setPaymentStatus(PaymentStatusEnum.SUCCESS_2.getStatus());
            record.setPaymentMsg(retMsg);
            record.setPaymentTime(stateTime);
            noticeFinance(record);
        } else if (CibConstant.WAIT_QUERY.contains(retCode)) {
            record.setPaymentStatus(PaymentStatusEnum.WAIT_PAY_1.getStatus());
            record.setPaymentMsg(retMsg);
        } else {
            record.setPaymentStatus(PaymentStatusEnum.FAILURE_3.getStatus());
            record.setPaymentMsg(retMsg);
            noticeFinance(record);
        }
        record.updateById();
    }
}
