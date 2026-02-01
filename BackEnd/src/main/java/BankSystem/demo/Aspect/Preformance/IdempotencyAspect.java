package BankSystem.demo.Aspect.Preformance;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class IdempotencyAspect {
    // todo: implement idempotency aspect with caching mechanism to avoid duplicate processing of requests
    // we will implement it using Redis cache in the future

}
