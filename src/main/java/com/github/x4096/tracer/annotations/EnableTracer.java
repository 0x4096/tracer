package com.github.x4096.tracer.annotations;

import com.github.x4096.tracer.interceptor.TracerInterceptorLoader;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: tracer
 * @DateTime: 2019-11-23 11:44
 * @Description: 启用 tracer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import({TracerInterceptorLoader.class})
public @interface EnableTracer {

}
