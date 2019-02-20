package com.tn.pay.param.baofoo;

import com.tn.pay.enums.PaymentModeEnum;
import com.tn.pay.module.PaymentChannel;
import com.tn.pay.module.WaitPaymentRecord;
import com.tn.pay.param.ParamHandle;
import com.tn.pay.utils.DateUtil;
import com.tn.pay.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

import static com.tn.pay.constant.BaoFooDSConstant.*;

/**
 * 宝付[代收]查询参数组装类
 */
public class BaoFooDSQueryHandle implements ParamHandle {

    @Override
    public Map<String, Object> generateParam(WaitPaymentRecord record, PaymentChannel channel) throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put(ORIG_TRANS_ID, record.getTransNo());
        data.put(TRANS_SERIAL_NO, "CALL" + StringUtil.getUUID());
        if (PaymentModeEnum.REPAYMENT.getCode().equals(channel.getPaymentMode())) {
            data.put(TXN_SUB_TYPE, "31");
        } else if (PaymentModeEnum.BIND_PAY.getCode().equals(channel.getPaymentMode())) {
            data.put(TXN_SUB_TYPE, "79");
        }
        data.put(BIZ_TYPE, "0000");
        data.put(TERMINAL_ID, channel.getTerminalNo());
        data.put(MEMBER_ID, channel.getMemberNo());
        data.put(ORIG_TRADE_DATE, record.getOrderDate());
//        data.put(REQ_RESERVED, "[success_time]yyyyMMddHHmmss[/success_time]");
        data.put(ACC_NO, record.getCustAccount());
        String orderDate = record.getOrderDate();
        orderDate = DateUtil.transSmallDate(orderDate);
        data.put(TRADE_DATE, orderDate);
        return data;
    }

}
