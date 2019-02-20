package com.tn.pay.param.baofoo;

import com.alibaba.fastjson.JSONObject;
import com.tn.pay.constant.BaoFooBindPayConstant;
import com.tn.pay.module.PaymentChannel;
import com.tn.pay.module.WaitPaymentRecord;
import com.tn.pay.param.ParamHandle;
import com.tn.pay.rsa.RsaCodingUtil;
import com.tn.pay.rsa.SignatureUtils;
import com.tn.pay.utils.DateUtil;
import com.tn.pay.utils.SecurityUtil;
import com.tn.pay.utils.StringUtil;
import com.tn.pay.utils.UrlUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 宝付[协议支付]参数组装类
 */
public class BaoFooBindPayParamHandle implements ParamHandle {

    @Override
    public Map<String, Object> generateParam(WaitPaymentRecord record, PaymentChannel channel) throws Exception {
        String aesKey = StringUtil.getStringRandom(16);
        String dgtl_envlp = "01|" + aesKey;

        String version = channel.getVersion();
        String memberNo = channel.getMemberNo();
        String terminalNo = channel.getTerminalNo();
        String pubKeyPath = channel.getPubKeyPath();
        String priKeyPath = channel.getPriKeyPath();
        String priKeyPassword = channel.getPriKeyPassword();
        dgtl_envlp = RsaCodingUtil.encryptByPubCerFile(SecurityUtil.Base64Encode(dgtl_envlp), pubKeyPath);

        String protocolNo = record.getProtocolNo();
        protocolNo = SecurityUtil.AesEncrypt(SecurityUtil.Base64Encode(protocolNo), aesKey);

        Map<String, String> data = new TreeMap<>();
        data.put(BaoFooBindPayConstant.SEND_TIME, DateUtil.getSimpleDate(record.getOrderDate()));
        data.put(BaoFooBindPayConstant.MSG_ID, "TISN" + record.getCallFlow());
        data.put(BaoFooBindPayConstant.VERSION, version);
        data.put(BaoFooBindPayConstant.TERMINAL_ID, terminalNo);
        data.put(BaoFooBindPayConstant.TXN_TYPE, "08");
        data.put(BaoFooBindPayConstant.MEMBER_ID, memberNo);
        data.put(BaoFooBindPayConstant.TRANS_ID, record.getTransNo());
        data.put(BaoFooBindPayConstant.DGTL_ENVLP, dgtl_envlp);
        data.put(BaoFooBindPayConstant.USER_ID, record.getUserId());
        data.put(BaoFooBindPayConstant.PROTOCOL_NO, protocolNo);

        int txn_amt = record.getPaymentAmount().multiply(new BigDecimal(100)).intValue();
        // 交易金额 [单位：分  例：1元则提交100]，此处注意数据类型的转转，建议使用BigDecimal类弄进行转换为字串
        data.put(BaoFooBindPayConstant.TXN_AMT, txn_amt + "");
        // data.put(CARD_INFO, "");//暂时不开放信用卡

        //风控参数
        Map<String, String> riskItem = new HashMap<>();
        riskItem.put(BaoFooBindPayConstant.GOODSCATEGORY, "02");//互金消金
        riskItem.put(BaoFooBindPayConstant.CHPAYIP, "127.0.0.1");
        data.put(BaoFooBindPayConstant.RISK_ITEM, JSONObject.toJSONString(riskItem));

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
