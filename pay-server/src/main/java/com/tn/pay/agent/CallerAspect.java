package com.tn.pay.agent;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * 切面定义类，以加@Caller注解点为切点
 */
@Aspect
@Component
public class CallerAspect {

    @Pointcut("@annotation(caller)")
    public void excudeService(Caller caller) {
    }

    @Around("excudeService(caller)")
    public Object around(ProceedingJoinPoint joinPoint, Caller caller) throws Throwable {
        ProxyEnum[] proxys = caller.proxys();
        return proxys[0].getProxy().doAround(joinPoint, caller);
    }

    @Before(value = "excudeService(caller)")
    public void before(JoinPoint joinPoint, Caller caller) {
        Object[] args = joinPoint.getArgs();
        ProxyEnum[] proxys = caller.proxys();
        for (ProxyEnum proxyEnum : proxys) {
            proxyEnum.getProxy().doBefore(joinPoint, caller, args);
        }
    }

    @After("excudeService(caller)")
    public void after(JoinPoint joinPoint, Caller caller) {
        ProxyEnum[] proxys = caller.proxys();
        for (ProxyEnum proxyEnum : proxys) {
            proxyEnum.getProxy().doAfter(joinPoint, caller);
        }
    }

    @AfterReturning(value = "excudeService(caller)", returning = "ret")
    public void afterReturning(JoinPoint joinPoint, Caller caller, Object ret) {
        ProxyEnum[] proxys = caller.proxys();
        for (ProxyEnum proxyEnum : proxys) {
            proxyEnum.getProxy().doAfterReturning(joinPoint, caller, ret);
        }
    }

    @AfterThrowing(value = "excudeService(caller)", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Caller caller, Exception e) {
        ProxyEnum[] proxys = caller.proxys();
        for (ProxyEnum proxyEnum : proxys) {
            proxyEnum.getProxy().doAfterThrowing(joinPoint, caller, e);
        }
    }

}
