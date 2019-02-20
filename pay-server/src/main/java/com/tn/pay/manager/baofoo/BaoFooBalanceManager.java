package com.tn.pay.manager.baofoo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tn.pay.dto.BalanceDTO;
import com.tn.pay.module.PaymentChannel;
import com.tn.pay.utils.SecurityUtil;
import com.tn.pay.http.HttpMethod;
import com.tn.pay.http.HttpSendModel;
import com.tn.pay.http.SimpleHttpResponse;
import com.tn.pay.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 余额查询处理类
 */
@Slf4j
@Component
public class BaoFooBalanceManager {

    public List<BalanceDTO> getBalance(PaymentChannel channel) throws Exception {
        log.info("余额查询，通道:{}", channel);
        List<BalanceDTO> balances = new ArrayList<>();

        String postData = "member_id=%s&terminal_id=%s&return_type=%s&trans_code=BF0001&version=%s&account_type=0";
        postData = String.format(postData,
                channel.getMemberNo(),
                channel.getTerminalNo(),
                channel.getDataType(),
                channel.getVersion());

        String sign = SecurityUtil.MD5(postData + "&key=" + channel.getMemberKey()).toUpperCase();//必须为大写
        String url = channel.getRequestUrl() + "?" + postData + "&sign=" + sign;

        HttpSendModel httpSendModel = new HttpSendModel(url, HttpMethod.POST);
        SimpleHttpResponse response = HttpUtil.doRequest(httpSendModel);

        Assert.state(response.getStatusCode() == 200, response.getErrorMessage());

        String result = response.getEntityString();
        log.info("余额查询，返回:{}", result);
        JSONObject object = JSON.parseObject(result);
        JSONObject trans_content = object.getJSONObject("trans_content");
        JSONObject trans_head = trans_content.getJSONObject("trans_head");
        JSONObject trans_reqDatas = trans_content.getJSONObject("trans_reqDatas");
        String return_code = trans_head.getString("return_code");
        String return_msg = trans_head.getString("return_msg");
        if ("0000".equals(return_code)) {
            JSONArray trans_reqData = trans_reqDatas.getJSONArray("trans_reqData");
            log.info("余额查询-查询成功，data:{}", JSON.toJSONString(trans_reqData));
            trans_reqData.forEach(data -> {
                Integer account_type = ((JSONObject) data).getInteger("account_type");
                String currency = ((JSONObject) data).getString("currency");
                BigDecimal balance = ((JSONObject) data).getBigDecimal("balance");
                BalanceDTO balanceDTO = new BalanceDTO(account_type, balance, currency);
                balances.add(balanceDTO);
            });
        } else {
            throw new Exception("余额查询-查询失败:" + return_code + "，返回信息:" + return_msg);
        }
        return balances;
    }
}
