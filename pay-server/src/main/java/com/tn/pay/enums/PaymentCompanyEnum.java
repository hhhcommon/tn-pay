package com.tn.pay.enums;

/**
 * 支付公司枚举
 */
public enum PaymentCompanyEnum {
    BAO_FOO(1, "宝付"),
    CIB(2, "宝付");

    private Integer code;
    private String name;

    PaymentCompanyEnum(Integer code, String name) {
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
}
