package com.github.x4096.tracer.interceptor;

import com.github.x4096.tracer.configuration.TracerProperties;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: tracer
 * @DateTime: 2019-11-23 00:39
 * @Description: 方法追踪切入点
 */
public class TracerInterceptorLoader {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Dubbo 接口切入
     *
     * @param tracerProperties 追踪配置
     * @return 切入
     * @apiNote 注意: 不支持 alibaba 包下的 {@link com.alibaba.dubbo.config.annotation.Service}
     */
    @Bean
    @ConditionalOnClass(name = {"org.apache.dubbo.config.annotation.Service"})
    @ConditionalOnProperty(prefix = "com.github.x4096.tracer", name = {"global-log-out", "rpc-log-out"}, matchIfMissing = true)
    public DefaultPointcutAdvisor tracerRpc(TracerProperties tracerProperties) {
        logger.info("Tracer-Rpc 启用, 配置: {}", tracerProperties.toString());

        TracingMethodInterceptor interceptor = new TracingRpcMethodInterceptor(tracerProperties);
        AnnotationMatchingPointcut pointcut = new AnnotationMatchingPointcut(Service.class, true);

        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setPointcut(pointcut);
        advisor.setAdvice(interceptor);
        return advisor;
    }

    /**
     * SpringMVC 接口切入
     *
     * @param tracerProperties 追踪配置
     * @return 切入
     */
    @Bean
    @ConditionalOnClass(name = {"org.springframework.stereotype.Controller"})
    @ConditionalOnProperty(prefix = "com.github.x4096.tracer", name = {"global-log-out", "mvc-log-out"}, matchIfMissing = true)
    public DefaultPointcutAdvisor tracerMvc(TracerProperties tracerProperties) {
        logger.info("Tracer-Mvc 启用, 配置: {}", tracerProperties.toString());

        TracingMethodInterceptor interceptor = new TracingMvcMethodInterceptor(tracerProperties);
        AnnotationMatchingPointcut pointcut = new AnnotationMatchingPointcut(Controller.class, true);

        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setPointcut(pointcut);
        advisor.setAdvice(interceptor);
        return advisor;
    }

}
