package BankSystem.demo.Aspect.Preformance;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.util.concurrent.TimeUnit;
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class IdempotencyAspect {

  /*                                               (here we go again.....)
        that's not an ai comment but a real human one, i promise :D
        what is idempotency?
        it's a concept in software design that ensures that an operation can be performed multiple times without changing the result beyond the initial application.
        in other words, if you send the same request multiple times, it will have the same effect as sending it once.
        why do we need it?
        in real world applications, especially in web services, clients may accidentally send the same request multiple times due to network issues,
        user errors, or retries. without idempotency, this could lead to unintended consequences, such as duplicate transactions,
        multiple resource creations, or inconsistent states.
        why redis?
        Redis is an in-memory data store that provides fast read and write operations, making it ideal for implementing idempotency. we can use Redis to store a unique key for each request,
        along with a status indicating whether the request is being processed or has been completed.
        this allows us to quickly check if a request with the same key has already been processed and take appropriate action
        and also we can set a time to live (TTL) for the key to automatically expire after a certain period, preventing stale keys from accumulating in Redis.

        note : we need to send the idempotency key in the header of the request with the name "Idempotency-Key" and it should be unique for each request that we want to be idempotent.


   */





    private final RedisTemplate<String ,Object> redisTemplate;

    private static  final String IDEMPOTENCY_HEADER = "Idempotency-Key";


    @Around("@annotation(idempotent)")
    public Object checkIdempotency(ProceedingJoinPoint joinPoint, Idempotent idempotent) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String idempotencyKey = request.getHeader(IDEMPOTENCY_HEADER);

        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            throw  new RuntimeException("Idempotency key is required! Please provide a unique key in the 'Idempotency-Key' header.");
        }

        String cacheKey = "idemp:" + idempotencyKey;

        log.info("Checking idempotency for key: {}", cacheKey);


        Boolean isFirstRequest = redisTemplate.opsForValue()
                .setIfAbsent(cacheKey, "PROCESSING", idempotent.ttl(), idempotent.timeUnit());

        if (Boolean.FALSE.equals(isFirstRequest)) {
            log.warn("Duplicate request rejected for key: {}", cacheKey);
            throw new RuntimeException("Duplicate request detected! This operation is already being processed. Please wait and try again later.");
        }


        try {
            Object result = joinPoint.proceed();


             redisTemplate.opsForValue().set(cacheKey, "COMPLETED");

            return result;
        } catch (Throwable e) {


            log.error("Error during processing. Releasing idempotency key: {}", cacheKey);
            redisTemplate.delete(cacheKey);
            throw e;
        }
    }
}
