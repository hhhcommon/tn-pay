package com.tn.pay.api.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tn.pay.agent.Caller;
import com.tn.pay.api.PaymentAPI;
import com.tn.pay.dto.BalanceDTO;
import com.tn.pay.dto.ChannelModuleDTO;
import com.tn.pay.dto.PaymentOrderDTO;
import com.tn.pay.enums.PaymentCompanyEnum;
import com.tn.pay.enums.PaymentModeEnum;
import com.tn.pay.manager.baofoo.BaoFooBalanceManager;
import com.tn.pay.manager.cib.CibBalanceManager;
import com.tn.pay.module.BindCardInfo;
import com.tn.pay.module.PaymentChannel;
import com.tn.pay.module.WaitPaymentRecord;
import com.tn.pay.service.ChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 支付接口
 * 版本号 新旧版本同时注册启用情况
 * 如果出现新版本不稳定，可随时切回旧稳定版本
 * 接口 内部实现变化 尾号+1
 * 接口 参数返回值变化 中号+1
 * 接口 新加减少接口方法 头号+1
 */
@Slf4j
@Component
//@Service(interfaceClass = PaymentAPI.class, version = "2.0.0")
@Service(interfaceClass = PaymentAPI.class)
public class PaymentAPIImpl implements PaymentAPI {

    @Autowired
    private ChannelService channelService;

    @Autowired
    private BaoFooBalanceManager baoFooBalanceManager;

    @Autowired
    private CibBalanceManager cibBalanceManager;

    @Override
    @Caller(name = "保存支付指令")
    public Boolean sendPayment(PaymentOrderDTO dto) {
        log.info("保存支付指令-流水号:{}，接口入参:{}", dto.getCallFlow(), JSON.toJSONString(dto));
        try {
            //订单重复校验
            WaitPaymentRecord waitPaymentRecord = new WaitPaymentRecord();
            EntityWrapper wrapper = new EntityWrapper();
            wrapper.eq("call_flow", dto.getCallFlow());
            WaitPaymentRecord record = waitPaymentRecord.selectOne(wrapper);
            Assert.isNull(record, "支付指令已存在！");
            //验证通道
            Integer sceneId = dto.getSceneId();
            Integer fundsId = dto.getFundsId();
            Integer productId = dto.getProductId();
            Integer providerId = dto.getProviderId();
            Integer paymentMode = dto.getPaymentMode();
            ChannelModuleDTO channelModuleDTO = new ChannelModuleDTO(sceneId, fundsId, productId, providerId);
            channelModuleDTO.setPaymentMode(paymentMode);
            PaymentChannel channel = channelService.getChannel(channelModuleDTO);
            Assert.notNull(channel, "通道不存在！");

            //保存指令
            waitPaymentRecord = new WaitPaymentRecord();
            BeanUtils.copyProperties(dto, waitPaymentRecord);
            waitPaymentRecord.setTransNo("TN" + waitPaymentRecord.getCallFlow());//商户订单号
            waitPaymentRecord.setChannelId(channel.getChannelId());//通道ID

            //协议支付获取协议号
            //TODO 主动传协议号和用户id
            if (PaymentModeEnum.BIND_PAY.getCode().equals(paymentMode)) {
                wrapper = new EntityWrapper();
                wrapper.eq("channel_id", channel.getChannelId());
                wrapper.eq("bank_account", dto.getCustAccount());
                wrapper.eq("status", 2);
                BindCardInfo bindCardInfo = new BindCardInfo().selectOne(wrapper);
                Assert.notNull(bindCardInfo, "无绑卡信息！");
                log.info("保存支付指令-流水号:{}，绑卡信息:{}", dto.getCallFlow(), JSON.toJSONString(bindCardInfo));
                String protocolCode = bindCardInfo.getProtocolCode();
                String userId = bindCardInfo.getUserId();
                Assert.hasText(protocolCode, "无绑卡协议！");
                Assert.hasText(userId, "用户ID为null！");
                waitPaymentRecord.setProtocolNo(protocolCode);
                waitPaymentRecord.setUserId(userId);
            }

            log.info("保存支付指令-流水号:{}，保存待支付指令:{}", dto.getCallFlow(), JSON.toJSONString(waitPaymentRecord));

            boolean insert = waitPaymentRecord.insert();
            if (!insert) {
                return Boolean.FALSE;
            }
            log.info("保存支付指令-流水号:{}，保存待支付指令成功:{}", dto.getCallFlow(), JSON.toJSONString(waitPaymentRecord));
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("保存支付指令-异常失败-流水号:{}，异常原因:", dto.getCallFlow(), e);
            return Boolean.FALSE;
        }
    }

    @Override
    @Caller(name = "余额查询")
    public List<BalanceDTO> getBalance(ChannelModuleDTO dto) {
        log.info("余额查询，参数:{}", JSON.toJSONString(dto));
        try {
            //验证通道
            dto.setPaymentMode(0);
            PaymentChannel channel = channelService.getChannel(dto);
            Assert.notNull(channel, "通道不存在！");

            List<BalanceDTO> balance = null;
            if (PaymentCompanyEnum.BAO_FOO.getCode().equals(channel.getPayCompanyId())) {
                balance = baoFooBalanceManager.getBalance(channel);
            } else if (PaymentCompanyEnum.CIB.getCode().equals(channel.getPayCompanyId())) {
                cibBalanceManager.queryBalance(channel);
            }
            log.info("余额查询-查询成功，返回集合:{}", JSON.toJSONString(balance));
            return balance;
        } catch (Exception e) {
            log.error("余额查询-异常失败，异常原因:", e);
            return null;
        }
    }

}
