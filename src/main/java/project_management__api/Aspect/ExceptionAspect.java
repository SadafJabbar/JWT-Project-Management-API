package project_management__api.Aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class ExceptionAspect {
    @AfterThrowing(pointcut = "execution(* project_management__api.service.*.*(..))",
    throwing = "exception")
    public void afterThrowing(JoinPoint joinPoint,Throwable exception){
        log.error("Exception in method {}: {}",joinPoint.getSignature().getName(),exception.getMessage());
    }

}
