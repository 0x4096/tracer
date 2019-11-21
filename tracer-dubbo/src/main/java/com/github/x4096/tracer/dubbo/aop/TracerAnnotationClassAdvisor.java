package com.github.x4096.tracer.dubbo.aop;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: tracer
 * @DateTime: 2019-11-22 00:19
 * @Description:
 */
public class TracerAnnotationClassAdvisor extends AbstractPointcutAdvisor {

    private Advice advice;

    private Pointcut pointcut;

    public TracerAnnotationClassAdvisor(MethodInterceptor interceptor) {
        this.advice = interceptor;
        this.pointcut = new TracerAnnotationClassPointcut();
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }



}
