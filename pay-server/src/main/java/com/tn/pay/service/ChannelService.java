package com.tn.pay.service;

import com.alibaba.fastjson.JSON;
import com.tn.pay.dto.ChannelModuleDTO;
import com.tn.pay.mapper.PaymentChannelMapper;
import com.tn.pay.module.PaymentChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 支付通道获取类
 */
@Slf4j
@Component
public class ChannelService {

    @Value("${tn.pay.channel.status}")
    private Integer channelStatus;

    @Autowired
    private PaymentChannelMapper paymentChannelMapper;

    public PaymentChannel getChannel(ChannelModuleDTO module) {
        log.info("获取通道-通道类型:{}，获取支付通道入参:{}", module.getPaymentMode(), JSON.toJSONString(module));
        PaymentChannel channel = new PaymentChannel();
        channel.setStatus(channelStatus);
        channel.setSceneId(module.getSceneId());
        channel.setFundsId(module.getFundsId());
        channel.setProductId(module.getProductId());
        channel.setProviderId(module.getProviderId());
        channel.setPaymentMode(module.getPaymentMode());
        log.info("获取通道-通道类型:{}，获取支付通道mapper参数:{}", module.getPaymentMode(), JSON.toJSONString(channel));
        channel = paymentChannelMapper.selectOne(channel);
        log.info("获取通道-通道类型:{}，获得支付通道:{}", module.getPaymentMode(), JSON.toJSONString(channel));
        return channel;
    }
}
