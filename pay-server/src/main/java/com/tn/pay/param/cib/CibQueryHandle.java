package com.tn.pay.param.cib;

import com.tn.pay.module.PaymentChannel;
import com.tn.pay.module.WaitPaymentRecord;
import com.tn.pay.param.ParamHandle;
import com.tn.pay.rsa.SignatureUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 兴业查询参数组装类
 */
public class CibQueryHandle implements ParamHandle {
    @Override
    public Map<String, Object> generateParam(WaitPaymentRecord record, PaymentChannel channel) throws Exception {
        String createTime = record.getCreateTime();
        String priKeyPath = channel.getPriKeyPath();
        String priKeyPassword = channel.getPriKeyPassword();
        Map<String, Object> map = new HashMap<>();
        map.put("version", channel.getVersion());//接口版本
        map.put("mchtId", channel.getMemberNo());//商户号
        map.put("signType", "RSA");//签名类型
        map.put("serialNo", "S" + record.getCallFlow());//流水号
        map.put("transTime", LocalDateTime.parse(createTime).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));//交易时间

        //签名
        Map<String, String> param = new HashMap<>();
        map.forEach((key, value) -> param.put(key, value.toString()));
        map.put("mac", SignatureUtils.generateMAC(param, priKeyPath, priKeyPassword));
        return map;
    }
}
