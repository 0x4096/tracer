package com.github.x4096.tracer.interceptor;

import com.github.x4096.tracer.configuration.TracerProperties;
import org.aopalliance.intercept.MethodInterceptor;

/**
 * @author 0x4096.peng@gmail.com
 * @project tracer
 * @datetime 2020/2/7 14:36
 * @description 方法拦截抽象
 * @readme
 */
public abstract class TracingMethodInterceptor implements MethodInterceptor {

    protected final TracerProperties tracerProperties;

    public TracingMethodInterceptor(TracerProperties tracerProperties) {
        this.tracerProperties = tracerProperties;
    }

}
