package com.fd.rookie.spring.boot.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * redis锁的注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RedisCacheLock {
    /**
     * redis锁 key的前缀
     * @return
     */
    String prefix() default "";

    /**
     * 过期时间
     * @return
     */
    int expire() default 5;

    /**
     * 过期时间的单位
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * key的分隔符，默认是:
     * @return
     */
    String delimiter() default ":";
}
