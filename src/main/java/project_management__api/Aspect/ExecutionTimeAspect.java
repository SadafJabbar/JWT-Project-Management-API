package project_management__api.Aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class ExecutionTimeAspect {
    @Around("@annotation(project_management__api.annotation.TrackExecution)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint)throws Throwable{
        long startTime=System.currentTimeMillis();
        Object result=proceedingJoinPoint.proceed();
        long endTime=System.currentTimeMillis();
        long totalTime=endTime-startTime;
        log.info("Method {} executed in {} ms",proceedingJoinPoint.getSignature().getName(),totalTime);
        return result;
    }

}
