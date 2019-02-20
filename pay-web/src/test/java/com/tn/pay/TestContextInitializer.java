package com.tn.pay;

import com.alibaba.fastjson.JSON;
import com.tn.pay.http.HttpMethod;
import com.tn.pay.http.HttpSendModel;
import com.tn.pay.http.SimpleHttpResponse;
import com.tn.pay.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.web.context.support.GenericWebApplicationContext;

import java.util.*;

@Slf4j
public class TestContextInitializer implements ApplicationContextInitializer<GenericWebApplicationContext> {

    @Override
    public void initialize(GenericWebApplicationContext applicationContext) {
        try {
            MutablePropertySources propertySources = applicationContext.getEnvironment().getPropertySources();
            Iterator<PropertySource<?>> its = propertySources.iterator();
            while (its.hasNext()) {
                PropertySource<?> ps = its.next();
                if (ps instanceof OriginTrackedMapPropertySource) {
                    LinkedHashMap source = (LinkedHashMap) ps.getSource();
                    if (source.containsKey("config.settings")) {
                        String settings = getSettings(source.get("config.settings").toString());
                        List<Map> list = JSON.parseArray(settings, Map.class);
                        Map<String, String> properties = new HashMap<>();
                        list.forEach(map -> {
                            Object propertyName = map.get("propertyName");
                            Object propertyValue = map.get("propertyValue");
                            properties.put(propertyName.toString(), propertyValue.toString());
                        });
                        source.putAll(properties);
                        return;
                    }
                }
            }
        } catch (Exception e) {
            log.error("spring boot 加载参数出错", e);
        }
    }

    private String getSettings(String url) throws Exception {
        HttpSendModel sendModel = new HttpSendModel(url, HttpMethod.GET);
        SimpleHttpResponse response = HttpUtil.doRequest(sendModel);
        if (response.getStatusCode() == 200) {
            log.info("[" + response.getStatusCode() + "]" + response.getEntityString());
            return response.getEntityString();
        } else {
            log.error("[" + response.getStatusCode() + "]" + response.getErrorMessage());
            return null;
        }
    }

}
