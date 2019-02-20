package com.tn.pay.manager.baofoo;

import com.alibaba.fastjson.JSON;
import com.tn.pay.dto.BindCardDTO;
import com.tn.pay.dto.ChannelModuleDTO;
import com.tn.pay.mapper.BindCardInfoMapper;
import com.tn.pay.module.BindCardInfo;
import com.tn.pay.module.PaymentChannel;
import com.tn.pay.rsa.SignatureUtils;
import com.tn.pay.service.ChannelService;
import com.tn.pay.utils.SecurityUtil;
import com.tn.pay.http.HttpMethod;
import com.tn.pay.http.HttpSendModel;
import com.tn.pay.http.SimpleHttpResponse;
import com.tn.pay.utils.HttpUtil;
import com.tn.pay.utils.UrlUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Objects;

/**
 * 绑卡父类
 * 1 获取支付管道
 * 2 查验本地绑卡数据
 * 3 组装参数，子类实现
 * 4 请求接口
 * 5 返回延签
 * 6 处理结果，子类实现
 */
@Slf4j
public abstract class BindManager {

    /**
     * 签名默认编码
     */
    public static final String DEFAULT_CHARSET = "UTF-8";

    @Autowired
    private ChannelService channelService;

    @Autowired
    private BindCardInfoMapper bindCardInfoMapper;

    private PaymentChannel getChannel(BindCardDTO bindCardDTO) {
        //验证通道
        Integer sceneId = bindCardDTO.getSceneId();
        Integer fundsId = bindCardDTO.getFundsId();
        Integer productId = bindCardDTO.getProductId();
        Integer providerId = bindCardDTO.getProviderId();
        ChannelModuleDTO channelModuleDTO = new ChannelModuleDTO(sceneId, fundsId, productId, providerId);
        channelModuleDTO.setPaymentMode(3);
        PaymentChannel channel = channelService.getChannel(channelModuleDTO);
        Assert.notNull(channel, "通道不存在！");
        return channel;
    }

    private BindCardInfo getBindCardInfo(PaymentChannel channel, BindCardDTO bindCardDTO) {
        BindCardInfo bindCardInfo = new BindCardInfo();
        bindCardInfo.setBankAccount(bindCardDTO.getBankAccount());
        bindCardInfo.setChannelId(channel.getChannelId());
        return bindCardInfoMapper.selectOne(bindCardInfo);
    }

    private Map<String, String> doRequest(String requestUrl, Map<String, String> data) throws Exception {
        String dataForm = UrlUtil.coverMap2String(data);
        HttpSendModel httpSendModel = new HttpSendModel(requestUrl + "?" + dataForm, HttpMethod.POST);
        SimpleHttpResponse response = HttpUtil.doRequest(httpSendModel);
        Assert.state(response.getStatusCode() == 200, response.getErrorMessage());
        return UrlUtil.getParams(response.getEntityString());
    }

    private void verifySign(Map<String, String> returnData, String pubKeyPath) throws Exception {
        String sign = returnData.get("signature");
        returnData.remove("signature");
        if (StringUtils.isBlank(sign)) {
            throw new Exception("缺少验签参数！");
        }
        String signStr = UrlUtil.coverMap2String(returnData);
        String signature = SecurityUtil.sha1X16(signStr, DEFAULT_CHARSET);//签名
        if (!SignatureUtils.verifySignature(pubKeyPath, signature, sign)) {
            throw new Exception("验签失败！");
        }
    }

    @Transactional
    public void readySign(BindCardDTO bindCardDTO) throws Exception {
        log.info("预绑卡-卡号:{},绑卡参数:{}", bindCardDTO.getBankAccount(), JSON.toJSONString(bindCardDTO));
        PaymentChannel channel = getChannel(bindCardDTO);
        log.info("预绑卡-卡号:{},绑卡通道:{}", bindCardDTO.getBankAccount(), JSON.toJSONString(channel));

        BindCardInfo bindCardInfo = getBindCardInfo(channel, bindCardDTO);
        log.info("预绑卡-卡号:{},本地信息:{}", bindCardDTO.getBankAccount(), JSON.toJSONString(bindCardInfo));
        if (Objects.nonNull(bindCardInfo)) {
            Assert.state(bindCardInfo.getStatus() != 2, "已经绑卡！");
            Assert.isNull(bindCardInfo.getProtocolCode(), "已经绑卡！");
        } else {
            bindCardInfo = new BindCardInfo();
        }

        String requestUrl = channel.getRequestUrl();
        String pubKeyPath = channel.getPubKeyPath();

        Map<String, String> dataArray = readySignParam(channel, bindCardDTO);
        log.info("预绑卡-卡号:{},组装参数:{}", bindCardDTO.getBankAccount(), JSON.toJSONString(dataArray));

        Map<String, String> returnData = doRequest(requestUrl, dataArray);
        log.info("预绑卡-卡号:{},返回参数:{}", bindCardDTO.getBankAccount(), JSON.toJSONString(returnData));

        verifySign(returnData, pubKeyPath);

        String unique_code = readySignProcess(channel, returnData);
        log.info("预绑卡-卡号:{},返回唯一码:{}", bindCardDTO.getBankAccount(), unique_code);

        BeanUtils.copyProperties(bindCardDTO, bindCardInfo);
        bindCardInfo.setUniqueCode(unique_code);
        bindCardInfo.setStatus(1);
        bindCardInfo.setChannelId(channel.getChannelId());
        bindCardInfo.insertOrUpdate();
        log.info("预绑卡-卡号:{},保存成功:{}", bindCardDTO.getBankAccount(), JSON.toJSONString(bindCardInfo));
    }

    protected abstract Map<String, String> readySignParam(PaymentChannel channel, BindCardDTO bindCardDTO) throws Exception;

    protected abstract String readySignProcess(PaymentChannel channel, Map<String, String> returnData) throws Exception;

    @Transactional
    public void confirmSign(BindCardDTO bindCardDTO) throws Exception {
        log.info("确认绑卡-卡号:{},绑卡参数:{}", bindCardDTO.getBankAccount(), JSON.toJSONString(bindCardDTO));
        PaymentChannel channel = getChannel(bindCardDTO);
        log.info("确认绑卡-卡号:{},绑卡通道:{}", bindCardDTO.getBankAccount(), JSON.toJSONString(channel));

        BindCardInfo bindCardInfo = getBindCardInfo(channel, bindCardDTO);
        log.info("确认绑卡-卡号:{},本地信息:{}", bindCardDTO.getBankAccount(), JSON.toJSONString(bindCardInfo));
        Assert.notNull(bindCardInfo, "没有预绑卡！");
        Assert.state(bindCardInfo.getStatus() == 1, "没有预绑卡！");
        Assert.notNull(bindCardInfo.getUniqueCode(), "没有预绑卡唯一码！");

        String pubKeyPath = channel.getPubKeyPath();
        String requestUrl = channel.getRequestUrl();

        String sms = bindCardDTO.getSms();//短信验证码，测试环境随机6位数
        String uniqueCode = bindCardInfo.getUniqueCode() + "|" + sms;//[预签约唯一码|短信验证码]
        log.info("确认绑卡-卡号:{},短信验证码:{}", bindCardDTO.getBankAccount(), uniqueCode);

        Map<String, String> dataArray = confirmSignParam(channel, uniqueCode);
        log.info("确认绑卡-卡号:{},组装参数:{}", bindCardDTO.getBankAccount(), JSON.toJSONString(dataArray));

        Map<String, String> returnData = doRequest(requestUrl, dataArray);
        log.info("确认绑卡-卡号:{},返回参数:{}", bindCardDTO.getBankAccount(), JSON.toJSONString(returnData));

        verifySign(returnData, pubKeyPath);

        String protocol_no = confirmSignProcess(channel, returnData, bindCardInfo);
        log.info("确认绑卡-卡号:{},协议码:{}", bindCardDTO.getBankAccount(), protocol_no);

        bindCardInfo.setProtocolCode(protocol_no);
        if (StringUtils.isEmpty(protocol_no)) {
            bindCardInfo.setStatus(3);
        } else {
            bindCardInfo.setStatus(2);
        }
        bindCardInfo.updateById();
        log.info("确认绑卡-卡号:{},保存成功:{}", bindCardDTO.getBankAccount(), JSON.toJSONString(bindCardInfo));
    }

    protected abstract Map<String, String> confirmSignParam(PaymentChannel channel, String uniqueCode) throws Exception;

    protected abstract String confirmSignProcess(PaymentChannel channel, Map<String, String> returnData, BindCardInfo bindCardInfo) throws Exception;

    @Transactional
    public void queryBind(BindCardDTO bindCardDTO) throws Exception {
        log.info("绑卡查询-卡号:{},绑卡参数:{}", bindCardDTO.getBankAccount(), JSON.toJSONString(bindCardDTO));
        PaymentChannel channel = getChannel(bindCardDTO);
        log.info("绑卡查询-卡号:{},绑卡通道:{}", bindCardDTO.getBankAccount(), JSON.toJSONString(channel));

        BindCardInfo bindCardInfo = getBindCardInfo(channel, bindCardDTO);
        log.info("绑卡查询-卡号:{},本地信息:{}", bindCardDTO.getBankAccount(), JSON.toJSONString(bindCardInfo));
        if (bindCardInfo != null && StringUtils.isNotEmpty(bindCardInfo.getProtocolCode())) {
            log.info("绑卡查询-卡号:{},本地协议:{}", bindCardDTO.getBankAccount(), bindCardInfo.getProtocolCode());
            return;
        }

        String pubKeyPath = channel.getPubKeyPath();
        String requestUrl = channel.getRequestUrl();

        Map<String, String> dataArray = queryBindParam(channel, bindCardDTO);
        log.info("绑卡查询-卡号:{},组装参数:{}", bindCardDTO.getBankAccount(), JSON.toJSONString(dataArray));

        Map<String, String> returnData = doRequest(requestUrl, dataArray);
        log.info("绑卡查询-卡号:{},返回参数:{}", bindCardDTO.getBankAccount(), JSON.toJSONString(returnData));

        verifySign(returnData, pubKeyPath);

        String protocols = queryBindProcess(channel, returnData);
        log.info("绑卡查询-卡号:{},协议码:{}", bindCardDTO.getBankAccount(), protocols);
        // 签约协议号|用户ID|银行卡号|银行编码|银行名称; 签约协议号|用户ID|银行卡号|银行编码|银行名称
        String[] protocol_codes = protocols.split(";");
        for (String protocol_code : protocol_codes) {
            String[] msg = protocol_code.split("\\|", 5);
            String protocol = msg[0];
            String userId = msg[1];
            String bankAccount = msg[2];
            String bankCode = msg[3];
            String bankName = msg[4];
            bindCardInfo = new BindCardInfo();
            BeanUtils.copyProperties(bindCardDTO, bindCardInfo);
            bindCardInfo.setProtocolCode(protocol);
            bindCardInfo.setUserId(userId);
            bindCardInfo.setBankAccount(bankAccount);
            bindCardInfo.setBankCode(bankCode);
            bindCardInfo.setBankName(bankName);
            bindCardInfo.setChannelId(channel.getChannelId());
            bindCardInfo.setStatus(2);
            bindCardInfo.insertOrUpdate();
            log.info("绑卡查询-卡号:{},保存成功:{}", bindCardDTO.getBankAccount(), JSON.toJSONString(bindCardInfo));
        }
    }

    protected abstract Map<String, String> queryBindParam(PaymentChannel channel, BindCardDTO bindCardDTO) throws Exception;

    protected abstract String queryBindProcess(PaymentChannel channel, Map<String, String> returnData) throws Exception;

    @Transactional
    public void abolishBind(BindCardDTO bindCardDTO) throws Exception {
        log.info("解除绑卡-卡号:{},绑卡参数:{}", bindCardDTO.getBankAccount(), JSON.toJSONString(bindCardDTO));
        PaymentChannel channel = getChannel(bindCardDTO);
        log.info("解除绑卡-卡号:{},绑卡通道:{}", bindCardDTO.getBankAccount(), JSON.toJSONString(channel));

        BindCardInfo bindCardInfo = getBindCardInfo(channel, bindCardDTO);
        log.info("解除绑卡-卡号:{},本地信息:{}", bindCardDTO.getBankAccount(), JSON.toJSONString(bindCardInfo));
        Assert.notNull(bindCardInfo, "本地未绑卡！");

        String userId = bindCardDTO.getUserId();
        String protocolCode = bindCardInfo.getProtocolCode();
        log.info("解除绑卡-卡号:{},用户id:{},协议号:{}", bindCardDTO.getBankAccount(), userId, protocolCode);

        String pubKeyPath = channel.getPubKeyPath();
        String requestUrl = channel.getRequestUrl();

        Map<String, String> dataArray = abolishBindParam(channel, protocolCode, userId);
        log.info("解除绑卡-卡号:{},组装参数:{}", bindCardDTO.getBankAccount(), JSON.toJSONString(dataArray));

        Map<String, String> returnData = doRequest(requestUrl, dataArray);
        log.info("解除绑卡-卡号:{},返回参数:{}", bindCardDTO.getBankAccount(), JSON.toJSONString(returnData));

        verifySign(returnData, pubKeyPath);

        boolean abolish = abolishBindProcess(channel, returnData);
        log.info("解除绑卡-卡号:{},解绑结果:{}", bindCardDTO.getBankAccount(), abolish);
        if (abolish) {
            bindCardInfo.deleteById();
            log.info("解除绑卡-卡号:{},本地删除成功！", bindCardDTO.getBankAccount());
        }
    }

    protected abstract Map<String, String> abolishBindParam(PaymentChannel channel, String protocolNo, String userId) throws Exception;

    protected abstract boolean abolishBindProcess(PaymentChannel channel, Map<String, String> returnData) throws Exception;
}
