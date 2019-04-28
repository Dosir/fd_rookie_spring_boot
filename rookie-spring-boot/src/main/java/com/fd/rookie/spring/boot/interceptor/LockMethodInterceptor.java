package com.fd.rookie.spring.boot.interceptor;

import com.fd.rookie.spring.boot.annotation.LocalLock;
import com.fd.rookie.spring.boot.config.exception.BusinessException;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * Lock拦截器
 *
 * 首先通过 CacheBuilder.newBuilder() 构建出缓存对象，设置好过期时间【其目的就是为了防止因程序崩溃锁得不到释放】
 * 在具体的 interceptor() 方法上采用的是 Around（环绕增强） ，所有带 LocalLock 注解的都将被切面处理；
 */
@Aspect
@Configuration
public class LockMethodInterceptor {
    private static final Cache<String, Object> LocalCaches = CacheBuilder.newBuilder()
            //最大缓存1000个
            .maximumSize(1000)
            //设置写缓存5s后过期
            .expireAfterWrite(5, TimeUnit.SECONDS)
            .build();

    @Around("execution(public * *(..)) && @annotation(com.fd.rookie.spring.boot.annotation.LocalLock)")
    public Object interceptor(ProceedingJoinPoint pjp) throws Throwable{
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        LocalLock localLock = method.getAnnotation(LocalLock.class);
        String key = getKey(localLock.key(), pjp.getArgs());
        if (StringUtils.isNotEmpty(key)) {
            if (LocalCaches.getIfPresent(key) != null) {
                throw new BusinessException("请勿重复请求!");
            }
            // 如果是第一次请求,就将 key 当前对象压入缓存中
            LocalCaches.put(key, key);
        }
        return pjp.proceed();
    }

    /**
     * key 的生成策略，目前是通过接口的参数来生成key
     * @param keyExpress
     * @param args
     * @return
     */
    private String getKey(String keyExpress, Object[] args) {
        for(int i = 0; i < args.length; i++) {
            keyExpress = keyExpress.replace("arg[" + i + "]", args[i].toString());
        }
        return keyExpress;
    }


















}
