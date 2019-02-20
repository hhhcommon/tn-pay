package com.tn.pay.manager.cib;

import com.alibaba.fastjson.JSON;
import com.tn.pay.constant.CibConstant;
import com.tn.pay.module.PaymentChannel;
import com.tn.pay.rsa.SignatureUtils;
import com.tn.pay.utils.StringUtil;
import com.tn.pay.http.HttpMethod;
import com.tn.pay.http.HttpSendModel;
import com.tn.pay.http.SimpleHttpResponse;
import com.tn.pay.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * 余额查询处理类
 */
@Slf4j
@Component
public class CibBalanceManager {

    public void queryBalance(PaymentChannel channel) throws Exception {
        log.info("余额查询，通道:{}", channel);
        String memberNo = channel.getMemberNo();
        String requestUrl = channel.getRequestUrl();
        String pubKeyPath = channel.getPubKeyPath();
        String priKeyPath = channel.getPriKeyPath();
        String priKeyPassword = channel.getPriKeyPassword();
        Map<String, String> params = new HashMap<>();
        params.put("version", channel.getVersion());//接口版本
        params.put("mchtId", memberNo);//商户号
        params.put("signType", "RSA");//签名类型
        params.put("serialNo", StringUtil.getUUID());//流水号
//        params.put("transTime", "");//交易时间
        //签名
        params.put("mac", SignatureUtils.generateMAC(params, priKeyPath, priKeyPassword));

        HttpSendModel httpSendModel = new HttpSendModel(requestUrl, params, HttpMethod.POST);
        SimpleHttpResponse response = HttpUtil.doRequest(httpSendModel);

        Assert.state(response.getStatusCode() == 200, response.getErrorMessage());
        String result = response.getEntityString();

        log.info("余额查询，返回:{}", result);
        Map resultMap = JSON.parseObject(result, Map.class);
        //验签
        if (!SignatureUtils.verifyMAC(resultMap, pubKeyPath)) {
            throw new Exception("验签失败！");
        }

        String respCode = String.valueOf(resultMap.get("respCode"));
        String respMsg = String.valueOf(resultMap.get("respMsg"));
        if (CibConstant.SUCCESS.equals(respCode)) {
            //成功
            Object data = resultMap.get("data");
            log.info("余额查询-查询成功，data:{}", JSON.toJSONString(data));
        } else {
            //失败
            throw new Exception("余额查询-查询失败:" + respCode + "，返回信息:" + respMsg);
        }
    }
}
