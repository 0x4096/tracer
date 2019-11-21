package com.github.x4096.tracer.dubbo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: tracer
 * @DateTime: 2019-11-22 00:20
 * @Description:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Tracer {

    /**
     * operateName
     *
     * @return
     */
    String operateName() default "";

}
