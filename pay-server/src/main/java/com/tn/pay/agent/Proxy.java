package com.tn.pay.agent;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

public interface Proxy {

    Object doAround(ProceedingJoinPoint joinPoint, Caller caller) throws Throwable;

    void doBefore(JoinPoint joinPoint, Caller caller, Object[] args);

    void doAfter(JoinPoint joinPoint, Caller caller);

    void doAfterReturning(JoinPoint joinPoint, Caller caller, Object ret);

    void doAfterThrowing(JoinPoint joinPoint, Caller caller, Exception e);

}
