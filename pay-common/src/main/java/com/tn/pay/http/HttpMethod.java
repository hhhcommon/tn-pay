package com.tn.pay.http;

public enum HttpMethod {
    GET, POST;

    public String value() {
        return name();
    }

    public static HttpMethod fromValue(String v) {
        return valueOf(v);
    }

}
