package com.tn.pay.api;

import com.tn.pay.dto.BalanceDTO;
import com.tn.pay.dto.ChannelModuleDTO;
import com.tn.pay.dto.PaymentOrderDTO;

import java.util.List;

public interface PaymentAPI {

    /**
     * 推送待付款(回款)指令
     *
     * @param dto 账务放款(回款)指令
     */
    Boolean sendPayment(PaymentOrderDTO dto);

    /**
     * 获取余额数据
     *
     * @dto 通道索引
     */
    List<BalanceDTO> getBalance(ChannelModuleDTO dto);
}
