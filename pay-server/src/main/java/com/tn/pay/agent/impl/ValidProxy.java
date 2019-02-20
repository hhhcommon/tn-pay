package com.tn.pay.agent.impl;

import com.tn.pay.agent.Caller;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.springframework.util.Assert;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Objects;
import java.util.Set;

/**
 * 参数检验器，配合接口中DTO的注解实现
 */
@Slf4j
public class ValidProxy extends SimpleProxy {

    private static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    @Override
    public void doBefore(JoinPoint joinPoint, Caller caller, Object[] args) {
        for (Object arg : args) {
            validate(arg);
        }
    }

    @Override
    public void doAfterReturning(JoinPoint joinPoint, Caller caller, Object ret) {
        if (Objects.nonNull(ret))
            validate(ret);
    }

    @Override
    public void doAfterThrowing(JoinPoint joinPoint, Caller caller, Exception e) {
        log.error("ex :", e);
    }

    public <T> void validate(T t) {
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(t);

        for (ConstraintViolation<T> constraintViolation : constraintViolations) {
            log.error("验参错误:{}", constraintViolation.getMessage());
            Assert.isTrue(Boolean.FALSE, constraintViolation.getMessage());
        }
    }
}
