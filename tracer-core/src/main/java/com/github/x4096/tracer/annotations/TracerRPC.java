package com.github.x4096.tracer.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: tracer
 * @DateTime: 2019-11-22 00:20
 * @Description: Dubbo 方法拦截, 过时弃用, 默认拦截带有 @org.apache.dubbo.config.annotation.Service 注解的方法
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Deprecated
public @interface TracerRPC {

}
