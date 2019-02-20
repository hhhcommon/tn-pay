package com.tn.pay.enums;

/**
 * 支付模式枚举
 */
public enum PaymentModeEnum {
    QUERY_BALANCE(0, "余额查询"),
    PAYMENT(1, "代付"),
    REPAYMENT(2, "代收"),
    BIND_PAY(3, "绑卡,协议支付");

    private Integer code;
    private String name;

    PaymentModeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getName(Integer code) {
        for (PaymentModeEnum pm : PaymentModeEnum.values()) {
            if (pm.getCode() == code) {
                return pm.name;
            }
        }
        return null;
    }
}
