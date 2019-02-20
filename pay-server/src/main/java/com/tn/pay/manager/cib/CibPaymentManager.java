package com.tn.pay.manager.cib;

import com.tn.pay.manager.PaymentManager;
import com.tn.pay.module.PaymentChannel;
import com.tn.pay.module.WaitPaymentRecord;
import com.tn.pay.http.HttpMethod;
import com.tn.pay.http.HttpSendModel;
import com.tn.pay.http.SimpleHttpResponse;
import com.tn.pay.utils.HttpUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * 兴业支付处理类
 */
@Component
@Scope("prototype")//线程运行，必须是多例
public class CibPaymentManager extends PaymentManager {

    @Override
    protected String payment(WaitPaymentRecord record, PaymentChannel channel, Map<String, Object> param) throws Exception {
        String requestUrl = channel.getRequestUrl();

        Map<String, String> params = new HashMap<>();
        param.forEach((key, value) -> params.put(key, value.toString()));
        HttpSendModel httpSendModel = new HttpSendModel(requestUrl, params, HttpMethod.POST);
        SimpleHttpResponse response = HttpUtil.doRequest(httpSendModel);

        Assert.state(response.getStatusCode() == 200, response.getErrorMessage());
        return response.getEntityString();
    }
}
