package com.tn.pay.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class PageController {

    @Autowired
    private ConfigurableApplicationContext context;

    @GetMapping("/shutdown")
    public Boolean shutdown() {
        log.info("停止服务！");
        try {
            context.close();
            log.info("服务已停止！");
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("停止服务异常", e);
            return Boolean.FALSE;
        }
    }

}
