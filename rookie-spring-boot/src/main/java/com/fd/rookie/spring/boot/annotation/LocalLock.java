package com.fd.rookie.spring.boot.annotation;

import java.lang.annotation.*;

/**
 * 锁的注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface LocalLock {
    String key() default "";
}
