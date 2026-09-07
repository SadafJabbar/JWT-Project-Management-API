package project_management__api.Aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Slf4j
@Component
public class LoggingAspect {

    @Before("execution(* project_management__api.service.*.*(..))")
    public void before(JoinPoint joinPoint){
        Object[] args= joinPoint.getArgs();
        Object target=joinPoint.getTarget();
        log.info("Method started {}",joinPoint.getSignature().getName());
        log.info("Arguments: {}", Arrays.toString(args));
        log.info("Signature: {}",joinPoint.getSignature());
        log.info("Target: {}",target.getClass().getSimpleName());
    }

    @After("execution(* project_management__api.service.*.*(..))")
    public void after(JoinPoint joinPoint){
        log.info("Method Finished {}",joinPoint.getSignature().getName());
    }}
