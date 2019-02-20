package com.tn.pay.param.cib;

import com.tn.pay.module.PaymentChannel;
import com.tn.pay.module.WaitPaymentRecord;
import com.tn.pay.param.ParamHandle;
import com.tn.pay.rsa.RsaCodingUtil;
import com.tn.pay.rsa.SignatureUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 兴业代付代收参数组装类
 */
public class CibParamHandle implements ParamHandle {

    @Override
    public Map<String, Object> generateParam(WaitPaymentRecord record, PaymentChannel channel) throws Exception {
        String createTime = record.getCreateTime();
        String pubKeyPath = channel.getPubKeyPath();
        String priKeyPath = channel.getPriKeyPath();
        String priKeyPassword = channel.getPriKeyPassword();
        Map<String, Object> map = new HashMap<>();
        map.put("version", channel.getVersion());//接口版本
        map.put("mchtId", channel.getMemberNo());//商户号
        map.put("signType", "RSA");//签名类型
        map.put("serialNo", "S" + record.getCallFlow());//流水号
        map.put("transTime", LocalDateTime.parse(createTime).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));//交易时间

        //组装接口个性化参数
        map.put("acctName", RsaCodingUtil.encryptByCFCA(record.getAccountName(), pubKeyPath));//收款人账户姓名
        map.put("acctNo", RsaCodingUtil.encryptByCFCA(record.getCustAccount(), pubKeyPath));//收款人账号
        map.put("amount", record.getPaymentAmount().setScale(2, BigDecimal.ROUND_HALF_DOWN).toString());//交易金额(保留两位小数)
        map.put("currency", "156");//156-⼈民币
        //此处需转换参数--账务推送支付 1:对私,2:对公;推送银行时 0:对公,1:对私
        Integer pubFlag = 2;
        if (pubFlag.equals(record.getPubOrpriFlag())) {
            map.put("pubOrPriFlag", "0");//支付方式为对公付款时,参数需增加账户类型和银行名称
            map.put("acctType", "3");//银行账户类型 1 借记卡 2贷记卡 3：对公 5：存折
            map.put("bankName", record.getBankName());//银行名称
        } else {
            map.put("pubOrPriFlag", "1");
        }
        //签名
        Map<String, String> param = new HashMap<>();
        map.forEach((key, value) -> param.put(key, value.toString()));
        map.put("mac", SignatureUtils.generateMAC(param, priKeyPath, priKeyPassword));
        return map;
    }
}
