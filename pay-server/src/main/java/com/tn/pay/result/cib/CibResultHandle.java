package com.tn.pay.result.cib;

import com.alibaba.fastjson.JSON;
import com.tn.pay.module.PaymentChannel;
import com.tn.pay.result.ResultHandle;
import com.tn.pay.rsa.SignatureUtils;
import com.tn.pay.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * cib 组装结果
 */
public class CibResultHandle implements ResultHandle {
    @Override
    public Map<String, String> generateResult(PaymentChannel channel, String result) throws Exception {
        //进行验签
        Map resultMap = JSON.parseObject(result, Map.class);
        if (!SignatureUtils.verifyMAC(resultMap, channel.getPubKeyPath())) {
            throw new Exception("验签失败！");
        }

        Map<String, String> retMap = new HashMap<>();
        String respCode = resultMap.get("respCode") != null ? (String) resultMap.get("respCode") : null;
        String retMsg = resultMap.get("retMsg") != null ? (String) resultMap.get("retMsg") : null;
        String serialNo = resultMap.get("serialNo") != null ? (String) resultMap.get("serialNo") : null;
        String traceNo = resultMap.get("traceNo") != null ? (String) resultMap.get("traceNo") : null;
        String stateTime = resultMap.get("stateTime") != null ? (String) resultMap.get("stateTime") : null;
        String tranStatus = resultMap.get("tranStatus") != null ? (String) resultMap.get("tranStatus") : null;

        if (StringUtils.isNotEmpty(stateTime)) {
            stateTime = DateUtil.transSimpleDate(stateTime);
        }

        retMap.put("retCode", respCode);//兴业交易返回码
        retMap.put("retMsg", retMsg);//兴业响应信息
        retMap.put("serialNo", serialNo);//商户交易流水号
        retMap.put("traceNo", traceNo);//兴业交易回执
        retMap.put("stateTime", stateTime);//交易完成时间
        retMap.put("tranStatus", tranStatus);//交易状态(查询类) 0:失败，1：成功，2：处理中
        return retMap;
    }
}
