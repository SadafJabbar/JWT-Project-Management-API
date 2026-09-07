package project_management__api.Aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class ResultAspect {

    @AfterReturning(pointcut = "execution(* project_management__api.service.*.*(..))",
    returning = "result")
    public void afterReturning(JoinPoint joinPoint,Object result){
        log.info("Method {}  Returned {}",joinPoint.getSignature().getName(),result);
    }

}
