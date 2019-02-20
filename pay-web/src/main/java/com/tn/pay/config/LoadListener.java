package com.tn.pay.config;

import com.alibaba.fastjson.JSON;
import com.tn.pay.http.HttpMethod;
import com.tn.pay.http.HttpSendModel;
import com.tn.pay.http.SimpleHttpResponse;
import com.tn.pay.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.event.*;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.util.*;

@Slf4j
public class LoadListener implements ApplicationListener<ApplicationEvent>, Ordered, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public int getOrder() {
        // 执行优先级，越高越早
        return 0;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationStartingEvent) {
            log.info("spring boot 启动开始");
        } else if (event instanceof ApplicationEnvironmentPreparedEvent) {
            log.info("spring boot Enviroment已经准备完毕，但上下文context没有创建");
            try {
                ConfigurableEnvironment environment = ((ApplicationEnvironmentPreparedEvent) event).getEnvironment();
                MutablePropertySources maps = environment.getPropertySources();
                Iterator<PropertySource<?>> its = maps.iterator();
                while (its.hasNext()) {
                    PropertySource<?> ps = its.next();
                    if (ps instanceof OriginTrackedMapPropertySource) {
                        LinkedHashMap source = (LinkedHashMap) ps.getSource();
                        if(source.containsKey("config.settings")){
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
        } else if (event instanceof ApplicationPreparedEvent) {
            log.info("spring boot 上下文context创建完成，但bean没有加载完成");
        } else if (event instanceof ContextRefreshedEvent) {
            log.info("Spring boot 容器初始化完成，bean加载完成");
        } else if (event instanceof ApplicationStartedEvent) {
            log.info("spring boot 加载完成");
        } else if (event instanceof ApplicationReadyEvent) {
            log.info("spring boot 启动完成");
        } else if (event instanceof ApplicationFailedEvent) {
            log.info("spring boot 启动异常");
            ApplicationFailedEvent applicationFailedEvent = (ApplicationFailedEvent) event;
            applicationFailedEvent.getException().printStackTrace();
        } else {
            log.info("spring boot 事件 " + event.getClass());
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
