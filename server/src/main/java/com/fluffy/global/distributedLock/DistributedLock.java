package com.fluffy.global.distributedLock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    String key();

    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 락을 획득하기 위해 대기할 시간
     */
    long waitTime() default 3L;

    /**
     * 락을 유지할 시간
     */
    long leaseTime() default 3L;
}
