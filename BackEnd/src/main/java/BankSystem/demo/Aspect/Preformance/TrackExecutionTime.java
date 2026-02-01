package BankSystem.demo.Aspect.Preformance;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j // this is a logger for the class to log the execution time of the method
public class TrackExecutionTime {

    @Around("@annotation(performanceAspect)")
    public Object trackTime(ProceedingJoinPoint joinPoint, PerformanceAspect performanceAspect) throws Throwable {
        long startTime = System.currentTimeMillis();
        // joinPoint point hold the method that will be executed and its parameters
        Object result = joinPoint.proceed(); // this is the method that will be executed
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        if (duration > 2000) {
            log.warn("Method {} took {}ms", joinPoint.getSignature().getName(), duration);
        }      
        return result;
    }
}
