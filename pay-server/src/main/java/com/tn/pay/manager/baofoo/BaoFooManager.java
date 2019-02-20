package com.tn.pay.manager.baofoo;

import com.alibaba.fastjson.JSON;
import com.tn.pay.enums.PaymentModeEnum;
import com.tn.pay.enums.PaymentStatusEnum;
import com.tn.pay.manager.PaymentManager;
import com.tn.pay.module.PaymentChannel;
import com.tn.pay.module.WaitPaymentRecord;
import com.tn.pay.rsa.RsaCodingUtil;
import com.tn.pay.utils.SecurityUtil;
import com.tn.pay.http.HttpMethod;
import com.tn.pay.http.HttpSendModel;
import com.tn.pay.http.SimpleHttpResponse;
import com.tn.pay.utils.HttpUtil;
import com.tn.pay.utils.UrlUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * 宝付支付处理类
 * 加密请求
 */
@Component
@Scope("prototype")//线程运行，必须是多例
public class BaoFooManager extends PaymentManager {

    @Override
    protected String payment(WaitPaymentRecord record, PaymentChannel channel, Map<String, Object> param) throws Exception {

        String keyStorePath = channel.getPriKeyPath();
        String keyStorePassword = channel.getPriKeyPassword();

        /**
         * 加密规则：项目编码UTF-8
         * 第一步：BASE64 加密
         * 第二步：商户私钥加密
         */
        String origData = JSON.toJSONString(param);
        origData = SecurityUtil.Base64Encode(origData);
        String encryptData = RsaCodingUtil.encryptByPriPfxFile(origData, keyStorePath, keyStorePassword);

        //代付url特性处理，url预处理
        String requestUrl = channel.getRequestUrl();
        if (PaymentModeEnum.PAYMENT.getCode().equals(channel.getPaymentMode())) {
            if (PaymentStatusEnum.WAIT_PAY_1.getStatus().equals(record.getPaymentStatus())) {
                requestUrl += "BF0040002.do";//查询接口
            } else if (PaymentStatusEnum.NOT_PAY_0.getStatus().equals(record.getPaymentStatus())) {
                requestUrl += "BF0040001.do";//支付接口
            }
        }

        String postData = "member_id=%s&terminal_id=%s&data_type=%s&version=%s&data_content=%s";
        String url = requestUrl + "?" + String.format(postData,
                channel.getMemberNo(),
                channel.getTerminalNo(),
                channel.getDataType(),
                channel.getVersion(),
                encryptData);

        //代扣url特性处理，url后处理
        if (PaymentModeEnum.REPAYMENT.getCode().equals(channel.getPaymentMode())) {
            if (PaymentStatusEnum.WAIT_PAY_1.getStatus().equals(record.getPaymentStatus())) {
                url += "&txn_type=0431&txn_sub_type=31";//查询接口
            } else if (PaymentStatusEnum.NOT_PAY_0.getStatus().equals(record.getPaymentStatus())) {
                url += "&txn_type=0431&txn_sub_type=13";//支付接口
            }
        } else if (PaymentModeEnum.BIND_PAY.getCode().equals(channel.getPaymentMode())) {
            // 协议支付不走加密数据，使用延签
            Map<String, String> map = new HashMap<>();
            param.forEach((key, value) -> {
                map.put(key, value.toString());
            });
            String dataForm = UrlUtil.coverMap2String(map);
            url = requestUrl + "?" + dataForm;
        }

        HttpSendModel httpSendModel = new HttpSendModel(url, HttpMethod.POST);
        SimpleHttpResponse response = HttpUtil.doRequest(httpSendModel);

        Assert.state(response.getStatusCode() == 200, response.getErrorMessage());

        return response.getEntityString();
    }
}
