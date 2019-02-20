package com.tn.pay.agent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 切面处理器
 * 默认带有，调用时间统计，和接口参数校验器
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Caller {
    String name();

    ProxyEnum[] proxys() default {ProxyEnum.TIME, ProxyEnum.VALID};
}
