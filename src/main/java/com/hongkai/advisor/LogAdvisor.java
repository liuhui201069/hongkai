package com.hongkai.advisor;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author chengyu.hcy
 * @since 2016/10/12
 */
@Aspect
@Component
@Slf4j
public class LogAdvisor {

    // APO TIME
    private static Logger apiLogger = LoggerFactory.getLogger("CONSOLE");
    
    @Pointcut("(execution (* com.hongkai.controller.*.*(..)))")
    public void log() {}

    @Around(value = "log()", argNames = "joinPoint")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        String params = org.apache.commons.lang3.StringUtils.join(joinPoint.getArgs(), ",");
        long start = System.currentTimeMillis();
        Object result = null;
        try{
            result = joinPoint.proceed();
        }catch (Exception e){
            log.error("@LogAdvisor@ process exception.", e);
        }
        apiLogger.info("Method:{} completed, params = {}, result = {}, timeUsedMillis: {}",
            methodName,
            params,
            result,
            System.currentTimeMillis() - start);
        return result;
    }
}
