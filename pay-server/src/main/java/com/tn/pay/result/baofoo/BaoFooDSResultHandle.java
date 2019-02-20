package com.tn.pay.result.baofoo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.tn.pay.module.PaymentChannel;
import com.tn.pay.result.ResultHandle;
import com.tn.pay.rsa.RsaCodingUtil;
import com.tn.pay.utils.DateUtil;
import com.tn.pay.utils.SecurityUtil;
import com.tn.pay.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import static com.tn.pay.constant.BaoFooDSConstant.*;

/**
 * baofoo [代扣]组装结果
 */
public class BaoFooDSResultHandle implements ResultHandle {

    @Override
    public Map<String, String> generateResult(PaymentChannel channel, String result) throws Exception {
        String pubKeyPath = channel.getPubKeyPath();
        // 报文错误明文返回
        if (!result.contains(RESP_CODE)) {
            //密文返回
            //第一步：公钥解密
            //第二步：BASE64解密
            result = RsaCodingUtil.decryptByPubCerFile(result, pubKeyPath);
            result = SecurityUtil.Base64Decode(result);
        }
        JSONObject dataJson = JSON.parseObject(result);
        Map<String, String> map = Maps.newHashMap();
        map.put(RESP_CODE, dataJson.getString(RESP_CODE));
        map.put(RESP_MSG, dataJson.getString(RESP_MSG));
        map.put(TRANS_NO, dataJson.getString(TRANS_NO));
        map.put(SUCC_AMT, dataJson.getString(SUCC_AMT));
        //查询返回状态
        map.put(ORDER_STAT, dataJson.getString(ORDER_STAT));

        //请求原样返回
        map.put(MEMBER_ID, dataJson.getString(MEMBER_ID));
        map.put(TERMINAL_ID, dataJson.getString(TERMINAL_ID));
        map.put(TXN_SUB_TYPE, dataJson.getString(TXN_SUB_TYPE));
        map.put(BIZ_TYPE, dataJson.getString(BIZ_TYPE));
        map.put(TRANS_SERIAL_NO, dataJson.getString(TRANS_SERIAL_NO));
        String trade_date = dataJson.getString(TRADE_DATE);
        if (StringUtils.isNotEmpty(trade_date)) {
            trade_date = StringUtil.addZeroForNum(trade_date, 14, false);
            trade_date = DateUtil.transSimpleDate(trade_date);
        }
        map.put(TRADE_DATE, trade_date);
        map.put(ORIG_TRADE_DATE, dataJson.getString(ORIG_TRADE_DATE));
        map.put(TRANS_ID, dataJson.getString(TRANS_ID));
        map.put(ORIG_TRANS_ID, dataJson.getString(ORIG_TRANS_ID));
        map.put(SUCCESS_TIME, dataJson.getString(SUCCESS_TIME));
        return map;
    }
}
