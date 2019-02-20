package com.tn.pay.process.baofoo;

import com.alibaba.fastjson.JSON;
import com.tn.pay.constant.BaoFooDFConstant;
import com.tn.pay.enums.PaymentStatusEnum;
import com.tn.pay.module.PaymentChannel;
import com.tn.pay.module.WaitPaymentRecord;
import com.tn.pay.process.ResultProcessor;
import com.tn.pay.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import static com.tn.pay.constant.BaoFooDFConstant.*;

/**
 * 支付[代付]结果处理类
 */
@Slf4j
public class BaoFooDFResultProcessor extends ResultProcessor {

    @Override
    public void process(WaitPaymentRecord record, PaymentChannel channel, Map<String, String> map) throws Exception {
        String return_code = map.get(RETURN_CODE);
        String return_msg = map.get(RETURN_MSG);
        String trans_orderid = map.get(TRANS_ORDERID);
        String trans_batchid = map.get(TRANS_BATCHID);
        String trans_no = map.get(TRANS_NO);
        String trans_money = map.get(TRANS_MONEY);
        String to_acc_name = map.get(TO_ACC_NAME);
        String to_acc_no = map.get(TO_ACC_NO);
        String to_acc_dept = map.get(TO_ACC_DEPT);
        //查询返回特性字段
        String trans_fee = map.get(TRANS_FEE);
        String state = map.get(STATE);
        String trans_starttime = map.get(TRANS_STARTTIME);
        String trans_endtime = map.get(TRANS_ENDTIME);
        String trans_remark = map.get(TRANS_REMARK);

        /**
         * 代付查询
         * 1 如果是0000 判断状态处理
         * 代付
         * 1 200 白名单成功
         * 2 交易未明，等待查询
         * 剩余所有失败
         */
        String paymentDate = StringUtils.isEmpty(trans_endtime) ? trans_starttime : trans_endtime;
        paymentDate = StringUtils.isEmpty(paymentDate) ? DateUtil.getNowTime() : paymentDate;

        log.info("结果处理-流水号:{}，结果:{}", record.getCallFlow(), map);
        if (BaoFooDFConstant.SUCCESS.equals(return_code) && PaymentStatusEnum.WAIT_PAY_1.getStatus().equals(record.getPaymentStatus())) {
            // 查询返回
            if ("1".equals(state)) {
                record.setPaymentStatus(PaymentStatusEnum.SUCCESS_2.getStatus());
                record.setPaymentMsg(return_msg);
                record.setPaymentTime(paymentDate);
                noticeFinance(record);
            } else if ("0".equals(state)) {
                record.setPaymentStatus(PaymentStatusEnum.WAIT_PAY_1.getStatus());
                record.setPaymentMsg(trans_remark);
            } else {
                record.setPaymentStatus(PaymentStatusEnum.FAILURE_3.getStatus());
                record.setPaymentMsg(trans_remark);
                noticeFinance(record);
            }

            log.info("结果处理-流水号:{}，查询结果:{}", record.getCallFlow(), JSON.toJSONString(record));
            record.updateById();
            return;
        } else if (BaoFooDFConstant.SUCCESS_OK.equals(return_code)) {//白名单直接通过，情况较少
            record.setPaymentStatus(PaymentStatusEnum.SUCCESS_2.getStatus());
            record.setPaymentMsg(return_msg);
            record.setPaymentTime(paymentDate);
            noticeFinance(record);
        } else if (BaoFooDFConstant.WAIT_QUERY.contains(return_code)) {
            record.setPaymentStatus(PaymentStatusEnum.WAIT_PAY_1.getStatus());
            record.setPaymentMsg(return_msg);
        } else {
            record.setPaymentStatus(PaymentStatusEnum.FAILURE_3.getStatus());
            record.setPaymentMsg(return_msg);
            noticeFinance(record);
        }

        log.info("结果处理-流水号:{}，交易结果:{}", record.getCallFlow(), JSON.toJSONString(record));
        record.updateById();
    }

}
