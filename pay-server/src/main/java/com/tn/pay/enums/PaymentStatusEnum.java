package com.tn.pay.enums;

/**
 * 支付指令结果枚举
 */
public enum PaymentStatusEnum {
    NOT_PAY_0(0, "Not Payment"),//未支付
    WAIT_PAY_1(1, "Waiting for Payment"),//付款中
    SUCCESS_2(2, "Payment success"),//支付成功
    FAILURE_3(3, "Payment failure");//支付失败

    private final Integer status;
    private final String name;

    PaymentStatusEnum(Integer status, String name) {
        this.status = status;
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }
}
