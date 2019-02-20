package com.tn.pay.process.baofoo;

import com.alibaba.fastjson.JSON;
import com.tn.pay.constant.BaoFooBindPayConstant;
import com.tn.pay.constant.BaoFooDSConstant;
import com.tn.pay.enums.PaymentStatusEnum;
import com.tn.pay.module.PaymentChannel;
import com.tn.pay.module.WaitPaymentRecord;
import com.tn.pay.process.ResultProcessor;
import com.tn.pay.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;


/**
 * 支付[代扣]结果处理类
 */
@Slf4j
public class BaoFooBindPayResultProcessor extends ResultProcessor {

    @Override
    public void process(WaitPaymentRecord record, PaymentChannel channel, Map<String, String> map) throws Exception {
        String send_time = map.get(BaoFooBindPayConstant.SEND_TIME);
        String msg_id = map.get(BaoFooBindPayConstant.MSG_ID);
        String resp_code = map.get(BaoFooBindPayConstant.RESP_CODE);
        String biz_resp_code = map.get(BaoFooBindPayConstant.BIZ_RESP_CODE);
        String biz_resp_msg = map.get(BaoFooBindPayConstant.BIZ_RESP_MSG);
        String succ_amt = map.get(BaoFooBindPayConstant.SUCC_AMT);
        String succ_time = map.get(BaoFooBindPayConstant.SUCC_TIME);
        String order_id = map.get(BaoFooBindPayConstant.ORDER_ID);
        String trans_id = map.get(BaoFooBindPayConstant.TRANS_ID);

        String paymentTime = StringUtils.isEmpty(succ_time) ? DateUtil.getNowTime() : succ_time;

        /**
         * 协议支付查询
         * 1 如果是0000 判断状态处理
         * 协议支付
         * 1 直接成功
         * 2 交易未明，等待查询
         * 剩余所有失败
         */
        log.info("结果处理-流水号:{}，结果:{}", record.getCallFlow(), map);
        if (BaoFooDSConstant.SUCCESS_OK.equals(biz_resp_code) && PaymentStatusEnum.WAIT_PAY_1.getStatus().equals(record.getPaymentStatus())) {
            // 查询返回
            if ("S".equals(resp_code)) {
                record.setPaymentTime(paymentTime);
                record.setPaymentMsg(biz_resp_msg);
                record.setPaymentStatus(PaymentStatusEnum.SUCCESS_2.getStatus());
                noticeFinance(record);
            } else if ("I".equals(resp_code)) {
                record.setPaymentMsg(biz_resp_msg);
                record.setPaymentStatus(PaymentStatusEnum.WAIT_PAY_1.getStatus());
            } else {
                record.setPaymentMsg(biz_resp_msg);
                record.setPaymentStatus(PaymentStatusEnum.FAILURE_3.getStatus());
                noticeFinance(record);
            }

            log.info("结果处理-流水号:{}，查询结果:{}", record.getCallFlow(), JSON.toJSONString(record));
            record.updateById();
            return;
        } else if (BaoFooDSConstant.SUCCESS.contains(biz_resp_code)) {
            record.setPaymentTime(paymentTime);
            record.setPaymentMsg(biz_resp_msg);
            record.setPaymentStatus(PaymentStatusEnum.SUCCESS_2.getStatus());
            noticeFinance(record);
        } else if (BaoFooDSConstant.WAIT_QUERY.contains(biz_resp_code)) {
            record.setPaymentMsg(biz_resp_msg);
            record.setPaymentStatus(PaymentStatusEnum.WAIT_PAY_1.getStatus());
        } else {
            record.setPaymentMsg(biz_resp_msg);
            record.setPaymentStatus(PaymentStatusEnum.FAILURE_3.getStatus());
            noticeFinance(record);
        }

        log.info("结果处理-流水号:{}，交易结果:{}", record.getCallFlow(), JSON.toJSONString(record));
        record.updateById();
    }

}
