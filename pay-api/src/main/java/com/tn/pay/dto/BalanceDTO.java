package com.tn.pay.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 余额数据
 */
@Data
public class BalanceDTO implements Serializable {
    /**
     * 账号类型
     * 1:基本账户;
     * 2:未结算账户;
     * 3:冻结账户;
     * 4:保证金账户;
     * 5:资金托管账户；
     * 7:手续费账户
     * 9: 资金存管账户
     */
    @NotNull(message = "accountType 不能为空")
    private Integer accountType;
    /**
     * 余额
     */
    @NotNull(message = "balance 不能为空")
    private BigDecimal balance;
    /**
     * 币种
     */
    private String currency;

    public BalanceDTO(Integer accountType, BigDecimal balance, String currency) {
        this.accountType = accountType;
        this.balance = balance;
        this.currency = currency;
    }
}
