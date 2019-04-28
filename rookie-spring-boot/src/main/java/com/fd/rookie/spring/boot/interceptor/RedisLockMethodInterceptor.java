package com.fd.rookie.spring.boot.interceptor;

import com.fd.rookie.spring.boot.annotation.RedisCacheLock;
import com.fd.rookie.spring.boot.config.exception.BusinessException;
import com.fd.rookie.spring.boot.config.redis.RedisLockHelper;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.UUID;

@Aspect
@Configuration
public class RedisLockMethodInterceptor {
    private final RedisLockHelper redisLockHelper;

    @Autowired
    public RedisLockMethodInterceptor(RedisLockHelper redisLockHelper) {
        this.redisLockHelper = redisLockHelper;
    }

    @Around("execution(public * *(..)) && @annotation(com.fd.rookie.spring.boot.annotation.RedisCacheLock)")
    public Object interceptor(ProceedingJoinPoint pjp) throws Throwable{
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        RedisCacheLock redisCacheLock = method.getAnnotation(RedisCacheLock.class);
        if (StringUtils.isEmpty(redisCacheLock.prefix())) {
            throw new RuntimeException("lock key don't null...");
        }
        String lockKey = getKey(redisCacheLock.prefix(), pjp.getArgs());
        String value = UUID.randomUUID().toString();

        try {
            //如果返回true，说明获取到了锁；如果返回false，说明锁还未被释放，则抛出异常
            final boolean flag = redisLockHelper.lock(lockKey, value, redisCacheLock.expire(), redisCacheLock.timeUnit());
            if (!flag) {
                throw new BusinessException("重复提交!");
            }
            return pjp.proceed();
        } finally {
            // TODO 如果演示的话需要注释该代码;实际应该放开
//            redisLockHelper.unlock(lockKey, value);
        }
    }

    /**
     * key 的生成策略，目前是通过接口的参数来生成key
     * @param keyExpress
     * @param args
     * @return
     */
    private String getKey(String keyExpress, Object[] args) {
        return keyExpress.concat(args[0].toString());
    }
}
