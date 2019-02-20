package com.tn.pay.agent.impl;

import com.tn.pay.agent.Caller;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;

/**
 * 接口执行时间日志，自动处理接口处理时间
 */
@Slf4j
public class TimeProxy extends SimpleProxy {

    private ThreadLocal<Long> beginTime = new ThreadLocal<>();

    @Override
    public void doBefore(JoinPoint joinPoint, Caller caller, Object[] args) {
        String simpleName = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String name = joinPoint.getSignature().getName();
        log.info("call[{}] {}.{}()", caller.name(), simpleName, name);
        beginTime.set(System.currentTimeMillis());
    }

    @Override
    public void doAfter(JoinPoint joinPoint, Caller caller) {
        String simpleName = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String name = joinPoint.getSignature().getName();
        log.info("call[{}] {}.{}() time:{} ms", caller.name(), simpleName, name, System.currentTimeMillis() - beginTime.get());
    }
}
