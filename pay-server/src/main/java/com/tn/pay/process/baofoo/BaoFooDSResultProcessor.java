package com.tn.pay.process.baofoo;

import com.alibaba.fastjson.JSON;
import com.tn.pay.constant.BaoFooDSConstant;
import com.tn.pay.enums.PaymentStatusEnum;
import com.tn.pay.module.PaymentChannel;
import com.tn.pay.module.WaitPaymentRecord;
import com.tn.pay.process.ResultProcessor;
import com.tn.pay.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import static com.tn.pay.constant.BaoFooDSConstant.*;

/**
 * 支付[代扣]结果处理类
 */
@Slf4j
public class BaoFooDSResultProcessor extends ResultProcessor {

    @Override
    public void process(WaitPaymentRecord record, PaymentChannel channel, Map<String, String> map) throws Exception {
        String resp_code = map.get(RESP_CODE);
        String resp_msg = map.get(RESP_MSG);
        String trans_no = map.get(TRANS_NO);
        String succ_amt = map.get(SUCC_AMT);
        String order_stat = map.get(ORDER_STAT);

        String member_id = map.get(MEMBER_ID);
        String terminal_id = map.get(TERMINAL_ID);
        String txn_sub_type = map.get(TXN_SUB_TYPE);
        String biz_type = map.get(BIZ_TYPE);
        String trans_serial_no = map.get(TRANS_SERIAL_NO);
        String trade_date = map.get(TRADE_DATE);
        String orig_trade_date = map.get(ORIG_TRADE_DATE);
        String trans_id = map.get(TRANS_ID);
        String orig_trans_id = map.get(ORIG_TRANS_ID);
        String success_time = map.get(SUCCESS_TIME);

        String paymentTime = StringUtils.isEmpty(success_time) ? trade_date : success_time;
        paymentTime = StringUtils.isEmpty(paymentTime) ? DateUtil.getNowTime() : paymentTime;

        /**
         * 代扣查询
         * 1 如果是0000 判断状态处理
         * 代扣
         * 1 直接成功
         * 2 交易未明，等待查询
         * 剩余所有失败
         */
        log.info("结果处理-流水号:{}，结果:{}", record.getCallFlow(), map);
        if (BaoFooDSConstant.SUCCESS_OK.equals(resp_code) && PaymentStatusEnum.WAIT_PAY_1.getStatus().equals(record.getPaymentStatus())) {
            // 查询返回
            if ("S".equals(order_stat)) {
                record.setPaymentStatus(PaymentStatusEnum.SUCCESS_2.getStatus());
                record.setPaymentTime(paymentTime);
                record.setPaymentMsg(resp_msg);
                noticeFinance(record);
            } else if ("I".equals(order_stat)) {
                record.setPaymentStatus(PaymentStatusEnum.WAIT_PAY_1.getStatus());
                record.setPaymentMsg(resp_msg);
            } else {
                record.setPaymentStatus(PaymentStatusEnum.FAILURE_3.getStatus());
                record.setPaymentMsg(resp_msg);
                noticeFinance(record);
            }

            log.info("结果处理-流水号:{}，查询结果:{}", record.getCallFlow(), JSON.toJSONString(record));
            record.updateById();
            return;
        } else if (BaoFooDSConstant.SUCCESS.contains(resp_code)) {
            record.setPaymentStatus(PaymentStatusEnum.SUCCESS_2.getStatus());
            record.setPaymentTime(paymentTime);
            record.setPaymentMsg(resp_msg);
            noticeFinance(record);
        } else if (BaoFooDSConstant.WAIT_QUERY.contains(resp_code)) {
            record.setPaymentStatus(PaymentStatusEnum.WAIT_PAY_1.getStatus());
            record.setPaymentMsg(resp_msg);
        } else {
            record.setPaymentStatus(PaymentStatusEnum.FAILURE_3.getStatus());
            record.setPaymentMsg(resp_msg);
            noticeFinance(record);
        }

        log.info("结果处理-流水号:{}，交易结果:{}", record.getCallFlow(), JSON.toJSONString(record));
        record.updateById();
    }

}
