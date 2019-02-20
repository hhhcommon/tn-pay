package com.tn.pay.http;

public class SimpleHttpResponse {

    private int statusCode;
    private String entityString;
    private String errorMessage;

    public int getStatusCode() {
        return statusCode;
    }

    public String getEntityString() {
        return entityString;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public SimpleHttpResponse(int statusCode, String entityString, String errorMessage) {
        this.statusCode = statusCode;
        this.entityString = entityString;
        this.errorMessage = errorMessage;
    }

}
