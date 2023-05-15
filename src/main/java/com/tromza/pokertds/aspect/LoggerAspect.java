package com.tromza.pokertds.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Aspect
@Component
public class LoggerAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(public * *(..))")
    private void anyPublicOperation() {
    }

    @Pointcut("within(com.tromza.pokertds.controller..*)")
    private void inController() {
    }

    @Pointcut("within(com.tromza.pokertds.service..*)")
    private void inService() {
    }

    @Before("anyPublicOperation() && inController() || inService()")
    private void getLogBefore(JoinPoint joinPoint) {
        log.info("Method" + joinPoint.getSignature() + "started!");
    }

    @After("anyPublicOperation() && inController() || inService()")
    private void getLogAfter(JoinPoint joinPoint) {
        log.info("Method" + joinPoint.getSignature() + "finished!");
    }

    @Around("@annotation(com.tromza.pokertds.annotation.GetTimeAnnotation)")
    private Object getLogAround(@NotNull ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object retVal = joinPoint.proceed();
        long end = System.currentTimeMillis();
        long time = end - start;
        log.info("Method " + joinPoint.getSignature() + " worked " + time + " millisecond");
        return retVal;
    }
}
