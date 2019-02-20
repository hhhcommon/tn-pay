package com.tn.pay.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.TreeMap;

public class UrlUtil {

    public static String generateUrl(String url, Map<String, String> params) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        String requestUrl = url;

        if (params.size() != 0) {
            String appender = coverMap2String(params);
            // 用于url不存在参数的情况
            if (!url.contains("?")) {
                requestUrl += "?" + appender;
            }
            // 用于url已存在参数的情况
            else {
                requestUrl += "&" + appender;
            }
        }
        return requestUrl;
    }

    /**
     * map集合转String
     *
     * @data map
     */
    public static String coverMap2String(Map<String, String> data) {
        StringBuilder sf = new StringBuilder();
        for (String key : data.keySet()) {
            if (!StringUtils.isBlank(data.get(key))) {
                sf.append(key).append("=").append(data.get(key).trim()).append("&");
            }
        }
        return sf.substring(0, sf.length() - 1);
    }

    /**
     * 返回参数处理
     *
     * @str url参数
     */
    public static Map<String, String> getParams(String str) throws Exception {
        Map<String, String> dataArray = new TreeMap<>();
        String[] list = str.split("&");
        for (String temp : list) {
            if (temp.matches("(.+?)=(.+?)")) {
                String[] tempList = temp.split("=");
                dataArray.put(tempList[0], tempList[1]);
            } else if (temp.matches("(.+?)=")) {
                String[] tempList = temp.split("=");
                dataArray.put(tempList[0], "");
            } else {
                throw new Exception("参数无法分解！");
            }
        }
        return dataArray;
    }
}
