package com.tn.pay.param.baofoo;

import com.tn.pay.enums.PaymentModeEnum;
import com.tn.pay.module.PaymentChannel;
import com.tn.pay.module.WaitPaymentRecord;
import com.tn.pay.param.ParamHandle;
import com.tn.pay.utils.DateUtil;
import com.tn.pay.utils.StringUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.tn.pay.constant.BaoFooDSConstant.*;

/**
 * 宝付[代收]参数组装类
 */
public class BaoFooDSParamHandle implements ParamHandle {

    @Override
    public Map<String, Object> generateParam(WaitPaymentRecord record, PaymentChannel channel) throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put(TRANS_ID, record.getTransNo());
        data.put(TRANS_SERIAL_NO, "CALL" + StringUtil.getUUID());
        if (PaymentModeEnum.REPAYMENT.getCode().equals(channel.getPaymentMode())) {
            data.put(TXN_SUB_TYPE, "13");
        } else if (PaymentModeEnum.BIND_PAY.getCode().equals(channel.getPaymentMode())) {
            data.put(TXN_SUB_TYPE, "73");
        }
        data.put(BIZ_TYPE, "0000");
        data.put(PAY_CM, "1");//1,不校验， 2，四要素强校验
//        data.put(REQ_RESERVED, "[success_time]yyyyMMddHHmmss[/success_time]");
        data.put(TERMINAL_ID, channel.getTerminalNo());
        data.put(MEMBER_ID, channel.getMemberNo());
        data.put(PAY_CODE, record.getBankCode());
        data.put(TXN_AMT, String.valueOf(record.getPaymentAmount().multiply(BigDecimal.valueOf(100)).setScale(0)));
        String orderDate = record.getOrderDate();
        orderDate = DateUtil.transSmallDate(orderDate);
        data.put(TRADE_DATE, orderDate);

        data.put(ID_CARD_TYPE, record.getCertType());
        data.put(ID_CARD, record.getCertNo());
        data.put(ACC_NO, record.getCustAccount());
        data.put(ID_HOLDER, record.getAccountName());
        data.put(MOBILE, record.getMobile());
        data.put(PROTOCOL_NO, record.getProtocolNo());
        return data;
    }

}
