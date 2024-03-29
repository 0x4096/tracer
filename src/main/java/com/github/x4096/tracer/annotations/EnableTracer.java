package com.github.x4096.tracer.annotations;

import com.github.x4096.tracer.configuration.TracerProperties;
import com.github.x4096.tracer.interceptor.TracerInterceptorLoader;
import com.github.x4096.tracer.interceptor.mybatis.MybatisSqlTracerInterceptorLoader;
import org.springframework.boot.web.servlet.ServletComponentScan;
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
@Import({TracerInterceptorLoader.class, MybatisSqlTracerInterceptorLoader.class, TracerProperties.class})
@ServletComponentScan(basePackages = "com.github.x4096.tracer")
public @interface EnableTracer {

}
