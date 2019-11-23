package com.github.x4096.tracer.configuration;

import com.github.x4096.tracer.annotations.TracerMVC;
import com.github.x4096.tracer.annotations.TracerRPC;
import com.github.x4096.tracer.interceptor.TracingInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.context.annotation.Bean;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: tracer
 * @DateTime: 2019-11-23 00:39
 * @Description:
 */
public class InterceptorAnnotationConfig {

    @Bean
    public DefaultPointcutAdvisor tracerRpc() {
        TracingInterceptor interceptor = new TracingInterceptor();
        AnnotationMatchingPointcut pointcut = new AnnotationMatchingPointcut(TracerRPC.class, true);

        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setPointcut(pointcut);
        advisor.setAdvice(interceptor);
        return advisor;
    }

    @Bean
    public DefaultPointcutAdvisor tracerMvc() {
        TracingInterceptor interceptor = new TracingInterceptor();
        AnnotationMatchingPointcut pointcut = new AnnotationMatchingPointcut(TracerMVC.class, true);

        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setPointcut(pointcut);
        advisor.setAdvice(interceptor);
        return advisor;
    }

}
