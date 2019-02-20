package com.tn.pay.service;

import com.alibaba.fastjson.JSON;
import com.tn.pay.enums.RequestStatusEnum;
import com.tn.pay.mapper.HttpLogMapper;
import com.tn.pay.module.HttpLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * 支付日志处理类
 */
@Slf4j
@Component
public class HttpLogService {

    @Autowired
    private HttpLogMapper httpLogMapper;

    public HttpLog createLog(Map<String, Object> param, Integer channelId, Integer paymentId, Integer payAction) {
        String reqData = JSON.toJSONString(param);
        HttpLog httpLog = new HttpLog();
        httpLog.setChannelId(channelId);
        httpLog.setPaymentId(paymentId);
        httpLog.setPayAction(payAction);
        httpLog = httpLogMapper.selectOne(httpLog);
        if (Objects.isNull(httpLog)) {
            httpLog = new HttpLog();
            httpLog.setChannelId(channelId);
            httpLog.setPaymentId(paymentId);
            httpLog.setPayAction(payAction);
            httpLog.setRequestMessage(reqData);
            httpLogMapper.insert(httpLog);
        } else {
            httpLog.setRequestMessage(reqData);
            httpLogMapper.updateById(httpLog);
        }
        return httpLog;
    }

    public HttpLog updateLog(HttpLog httpLog, Map<String, String> param) {
        String resData = JSON.toJSONString(param);
        httpLog.setRequestStatus(RequestStatusEnum.REQUEST_RETURN.getStatus());
        httpLog.setResponseMessage(resData);
        httpLogMapper.updateById(httpLog);
        return httpLog;
    }
}
