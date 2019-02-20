package com.tn.pay.enums;

/**
 * http请求枚举
 */
public enum RequestStatusEnum {
    IN_REQUEST(1, "In the request"),//请求中
    REQUEST_RETURN(2, "Request to return");//请求返回

    private final Integer status;
    private final String msg;

    RequestStatusEnum(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }
}
