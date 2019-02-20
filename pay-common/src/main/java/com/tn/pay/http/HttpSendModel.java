package com.tn.pay.http;

import com.tn.pay.utils.UrlUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class HttpSendModel {

    /**
     * url构建默认编码
     */
    private static final String DEFAULT_CHARSET = "UTF-8";

    private String url;
    private HttpMethod method;
    private String charset = DEFAULT_CHARSET;
    private Map<String, String> params = new HashMap<>();
    private HttpMimeType mimeType;

    public String getUrl() {
        return url;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getCharset() {
        return charset;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public HttpMimeType getMimeType() {
        return mimeType;
    }

    /**
     * 默认application/x-www-form-urlencoded
     */
    public HttpSendModel(String url, HttpMethod method) throws Exception {
        this(url, HttpMimeType.APPLICATION_FORM_URLENCODED, method);
    }

    private HttpSendModel(String url, HttpMimeType mimeType, HttpMethod method) throws Exception {
        int index = url.indexOf("?");
        if (index != -1) {
            this.url = url.substring(0, index);
            String paramsString = url.substring(index + 1);
            //构建参数表
            buildParams(paramsString);
        } else {
            this.url = url;
        }
        this.method = method;
        this.mimeType = mimeType;
    }

    /**
     * 默认application/json
     */
    public HttpSendModel(String url, Map<String, String> params, HttpMethod method) throws Exception {
        this(url, params, HttpMimeType.APPLICATION_JSON, method);
    }

    private HttpSendModel(String url, Map<String, String> params, HttpMimeType mimeType, HttpMethod method) throws Exception {
        int index = url.indexOf("?");
        if (index != -1) {
            this.url = url.substring(0, index);
            String paramsString = url.substring(index + 1);
            //构建参数表
            buildParams(paramsString);
        } else {
            this.url = url;
        }
        this.params.putAll(params);
        this.method = method;
        this.mimeType = mimeType;
    }

    private void buildParams(String paramsString) throws Exception {
        if (StringUtils.isBlank(paramsString)) {
            return;
        }
        this.params.putAll(UrlUtil.getParams(paramsString));
    }

    /**
     * 构建get请求url
     */
    public String buildGetRequestUrl() {
        return UrlUtil.generateUrl(this.url, this.params);
    }

}
