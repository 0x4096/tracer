package com.github.x4096.tracer.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: tracer
 * @DateTime: 2019-11-23 09:34
 * @Description: SpringMVC 拦截
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface TracerMVC {

}