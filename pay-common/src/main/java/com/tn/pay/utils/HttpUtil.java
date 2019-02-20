package com.tn.pay.utils;

import com.alibaba.fastjson.JSON;
import com.tn.pay.http.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.util.Map;

public class HttpUtil {

    //charset请求编码
    public static SimpleHttpResponse doRequest(HttpSendModel model) throws Exception {
        // 创建默认的httpClient客户端端
        SimpleHttpClient simpleHttpclient = new SimpleHttpClient();
        try {
            return doRequest(simpleHttpclient, model);
        } finally {
            simpleHttpclient.getHttpclient().close();
        }

    }

    public static SimpleHttpResponse doRequest(SimpleHttpClient httpclient, HttpSendModel model) throws Exception {

        String charset = model.getCharset();
        HttpRequestBase httpRequest = buildHttpRequest(model);

        if (model.getUrl().startsWith("https://")) {
            httpclient.enableSSL();
        }

        CloseableHttpResponse httpResponse = httpclient.getHttpclient().execute(httpRequest);
        try {
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                String result = EntityUtils.toString(httpResponse.getEntity(), charset);
                return new SimpleHttpResponse(statusCode, result, null);
            } else {
                String reason = httpResponse.getStatusLine().getReasonPhrase();
                return new SimpleHttpResponse(statusCode, null, reason);
            }
        } finally {
            EntityUtils.consume(httpResponse.getEntity());
            httpResponse.close();
            httpRequest.releaseConnection();
        }
    }

    protected static HttpRequestBase buildHttpRequest(HttpSendModel httpSendModel) throws Exception {
        HttpMimeType mimeType = httpSendModel.getMimeType();

        HttpRequestBase httpRequest;
        if (httpSendModel.getMethod() == null) {
            throw new Exception("请求方式未设定");
        }

        if (httpSendModel.getMethod() == HttpMethod.POST) {
            String url = httpSendModel.getUrl();
            String charset = httpSendModel.getCharset();
            Map<String, String> params = httpSendModel.getParams();

            HttpPost httppost = new HttpPost(url);

            HttpEntity entity = buildEntity(params, mimeType, charset);
            httppost.setEntity(entity);

            httpRequest = httppost;
        } else if (httpSendModel.getMethod() == HttpMethod.GET) {
            HttpGet httpget = new HttpGet(httpSendModel.buildGetRequestUrl());

            httpRequest = httpget;
        } else {
            throw new Exception("请求方式不支持:" + httpSendModel.getMethod());
        }

        return httpRequest;
    }

    private static HttpEntity buildEntity(Map<String, String> params, HttpMimeType mimeType, String charset) {
        if (HttpMimeType.APPLICATION_JSON.getValue().equals(mimeType.getValue())) {
            String body = JSON.toJSONString(params);
            return new StringEntity(body, ContentType.create(mimeType.getValue(), charset));
        }
        String string = UrlUtil.coverMap2String(params);
        ContentType contentType = ContentType.create(mimeType.getValue(), charset);
        return new StringEntity(string, contentType);
    }


    public static void main(String[] args) throws Exception {
        HttpSendModel sendModel = new HttpSendModel("https://www.baidu.com/s?wd=http", HttpMethod.GET);
        SimpleHttpResponse response = doRequest(sendModel);
        if (response.getStatusCode() == 200) {
            System.out.println("[" + response.getStatusCode() + "]" + response.getEntityString());
        } else {
            System.out.println("[" + response.getStatusCode() + "]" + response.getErrorMessage());
        }

    }

}
