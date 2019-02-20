package com.tn.pay;

import com.alibaba.fastjson.JSON;
import com.tn.pay.api.PaymentAPI;
import com.tn.pay.dto.ChannelModuleDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ContextConfiguration(initializers = {TestContextInitializer.class})
public class BalanceTest {

    @Autowired
    private PaymentAPI paymentAPI;

    @Test
    public void getBaofooBalance() {
        ChannelModuleDTO channelModuleDTO = new ChannelModuleDTO(1, 1, 1, 1);
        System.out.println(JSON.toJSONString(paymentAPI.getBalance(channelModuleDTO)));
    }

    @Test
    public void getCibBalance() {//TODO cib延签失败
        ChannelModuleDTO channelModuleDTO = new ChannelModuleDTO(2, 2, 2, 2);
        System.out.println(JSON.toJSONString(paymentAPI.getBalance(channelModuleDTO)));
    }
}
