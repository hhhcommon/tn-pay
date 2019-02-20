package com.tn.pay;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import com.tn.pay.config.LoadListener;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableDubboConfiguration
@ComponentScan("com.tn.pay")
@MapperScan("com.tn.pay.mapper")
public class Application {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.addListeners(new LoadListener());
        app.run(args);
    }

}
