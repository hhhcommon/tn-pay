package com.tn.pay.manager.cib;

import com.alibaba.fastjson.JSON;
import com.tn.pay.constant.CibConstant;
import com.tn.pay.dto.ChannelModuleDTO;
import com.tn.pay.dto.PaymentOrderDTO;
import com.tn.pay.module.PaymentChannel;
import com.tn.pay.rsa.RsaCodingUtil;
import com.tn.pay.rsa.SignatureUtils;
import com.tn.pay.service.ChannelService;
import com.tn.pay.utils.DateUtil;
import com.tn.pay.http.HttpMethod;
import com.tn.pay.http.HttpSendModel;
import com.tn.pay.http.SimpleHttpResponse;
import com.tn.pay.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 卡号认证父类
 * 1 获取支付管道
 * 2 组装参数，子类实现
 * 3 请求接口
 * 4 处理结果，子类实现
 */
@Slf4j
@Component
public class AuthManager {

    @Autowired
    private ChannelService channelService;

    public boolean acctAuth(PaymentOrderDTO dto) throws Exception {
        log.info("卡认证-卡号:{},绑卡参数:{}", dto.getCustAccount(), JSON.toJSONString(dto));

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
        log.info("卡认证-卡号:{},绑卡通道:{}", dto.getCustAccount(), JSON.toJSONString(channel));

        String requestUrl = channel.getRequestUrl();
        Map<String, String> params = getParams(dto, channel);
        log.info("卡认证-卡号:{},参数:{}", dto.getCustAccount(), JSON.toJSONString(params));

        HttpSendModel httpSendModel = new HttpSendModel(requestUrl, params, HttpMethod.POST);
        SimpleHttpResponse response = HttpUtil.doRequest(httpSendModel);

        Assert.state(response.getStatusCode() == 200, response.getErrorMessage());
        String result = response.getEntityString();
        log.info("卡认证-卡号:{},返回:{}", dto.getCustAccount(), result);

        boolean process = process(channel, result);
        if (process) {
            //保存认证信息
            return true;
        }
        return false;
    }

    private Map<String, String> getParams(PaymentOrderDTO dto, PaymentChannel channel) throws Exception {
        String createTime = DateUtil.getNowTime();
        String pubKeyPath = channel.getPubKeyPath();
        String priKeyPath = channel.getPriKeyPath();
        String priKeyPassword = channel.getPriKeyPassword();
        //组装公共参数 除mac签名字段外
        Map<String, String> request = new HashMap<>();
        request.put("signType", "RSA");//签名类型
        request.put("version", channel.getVersion());//接口版本
        request.put("mchtId", channel.getMemberNo());//商户号
        request.put("serialNo", dto.getCallFlow());//流水号
        request.put("transTime", LocalDateTime.parse(createTime).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));//交易时间
        //收款人特性字段
        request.put("acctNo", RsaCodingUtil.encryptByCFCA(dto.getCustAccount(), pubKeyPath));//收款人账号
        request.put("acctName", RsaCodingUtil.encryptByCFCA(dto.getAccountName(), pubKeyPath));
        request.put("bankName", RsaCodingUtil.encryptByCFCA(dto.getBankName(), pubKeyPath));
        request.put("certType", "0");//证件类型
        request.put("certNo", RsaCodingUtil.encryptByCFCA(dto.getCertNo(), pubKeyPath));//证件号码
        request.put("rsrvPhoneNo", RsaCodingUtil.encryptByCFCA(dto.getMobile(), pubKeyPath));//银行卡预留手机号
        request.put("sendSms", "false");//是否发送短信

        //签名
        request.put("mac", SignatureUtils.generateMAC(request, priKeyPath, priKeyPassword));
        return request;
    }

    private boolean process(PaymentChannel channel, String result) throws Exception {
        String pubKeyPath = channel.getPubKeyPath();
        //进行验签
        Map resultMap = JSON.parseObject(result, Map.class);
        if (!SignatureUtils.verifyMAC(resultMap, pubKeyPath)) {
            throw new Exception("验签失败！");
        }

        String respCode = String.valueOf(resultMap.get("respCode"));
        String retMsg = String.valueOf(resultMap.get("respMsg"));
        String serialNo = String.valueOf(resultMap.get("serialNo"));
        log.info("卡认证-流水:{},返回状态:{},信息:{}", serialNo, respCode, retMsg);
        return CibConstant.SUCCESS.equals(respCode);
    }

}
