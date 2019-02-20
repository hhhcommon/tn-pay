package com.tn.pay.agent.impl;

import com.tn.pay.agent.Caller;
import com.tn.pay.agent.Proxy;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 基础代理处理类
 */
public class SimpleProxy implements Proxy {

    @Override
    public Object doAround(ProceedingJoinPoint joinPoint, Caller caller) throws Throwable {
        return joinPoint.proceed();
    }

    @Override
    public void doBefore(JoinPoint joinPoint, Caller caller, Object[] args) {
    }

    @Override
    public void doAfter(JoinPoint joinPoint, Caller caller) {
    }

    @Override
    public void doAfterReturning(JoinPoint joinPoint, Caller caller, Object ret) {
    }

    @Override
    public void doAfterThrowing(JoinPoint joinPoint, Caller caller, Exception e) {
    }

}
