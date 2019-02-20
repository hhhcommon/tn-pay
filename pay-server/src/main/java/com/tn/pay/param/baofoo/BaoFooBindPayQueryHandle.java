package com.tn.pay.param.baofoo;

import com.tn.pay.constant.BaoFooBindPayConstant;
import com.tn.pay.module.PaymentChannel;
import com.tn.pay.module.WaitPaymentRecord;
import com.tn.pay.param.ParamHandle;
import com.tn.pay.rsa.SignatureUtils;
import com.tn.pay.utils.DateUtil;
import com.tn.pay.utils.SecurityUtil;
import com.tn.pay.utils.UrlUtil;

import java.util.Map;
import java.util.TreeMap;

/**
 * 宝付[协议支付]查询参数组装类
 */
public class BaoFooBindPayQueryHandle implements ParamHandle {

    @Override
    public Map<String, Object> generateParam(WaitPaymentRecord record, PaymentChannel channel) throws Exception {
        String memberNo = channel.getMemberNo();
        String terminalNo = channel.getTerminalNo();
        String priKeyPath = channel.getPriKeyPath();
        String priKeyPassword = channel.getPriKeyPassword();
        String version = channel.getVersion();

        Map<String, String> data = new TreeMap<>();
        data.put(BaoFooBindPayConstant.SEND_TIME, DateUtil.getSimpleDate(record.getOrderDate()));
        data.put(BaoFooBindPayConstant.MSG_ID, "TISN" + record.getCallFlow());
        data.put(BaoFooBindPayConstant.VERSION, version);
        data.put(BaoFooBindPayConstant.TERMINAL_ID, terminalNo);
        data.put(BaoFooBindPayConstant.TXN_TYPE, "07");
        data.put(BaoFooBindPayConstant.MEMBER_ID, memberNo);
        data.put(BaoFooBindPayConstant.ORIG_TRANS_ID, record.getTransNo());
        data.put(BaoFooBindPayConstant.ORIG_TRADE_DATE, DateUtil.getSimpleDate(record.getOrderDate()));

        //签名
        String signVStr = UrlUtil.coverMap2String(data);
        String signature = SecurityUtil.sha1X16(signVStr, "UTF-8");
        String sign = SignatureUtils.encryptByRSA(signature, priKeyPath, priKeyPassword);
        data.put(BaoFooBindPayConstant.SIGNATURE, sign);

        Map<String, Object> map = new TreeMap<>();
        map.putAll(data);
        return map;
    }

}
