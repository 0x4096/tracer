package com.github.x4096.tracer.interceptor;

import com.alibaba.fastjson.JSON;
import com.github.x4096.tracer.annotations.TracerMVC;
import com.github.x4096.tracer.annotations.TracerRPC;
import com.github.x4096.tracer.common.utils.AnnotationUtils;
import com.github.x4096.tracer.common.utils.TraceIdGeneratorUtils;
import com.github.x4096.tracer.execption.AnnotationTracerMvcNotFoundException;
import com.github.x4096.tracer.execption.AnnotationTracerRpcNotFoundException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.apache.dubbo.rpc.RpcContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.time.Clock;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: tracer
 * @DateTime: 2019-11-23 00:27
 * @Description:
 */
public class TracingInterceptor implements MethodInterceptor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Method method = methodInvocation.getMethod();
        Class clazz = methodInvocation.getThis().getClass();

        String traceId = null;

        /* Dubbo 服务 */
        if (AnnotationUtils.isAnnotation(clazz, TracerRPC.class)) {
            if (!(AnnotationUtils.isAnnotation(clazz, Service.class)
                    || AnnotationUtils.isAnnotation(clazz, com.alibaba.dubbo.config.annotation.Service .class))) {
                throw new AnnotationTracerRpcNotFoundException(methodInvocation.getThis().getClass().getName());
            }
            RpcContext rpcContext = RpcContext.getContext();
            traceId = rpcContext.getAttachment("rpcTraceId");
        }

        /* MVC 服务 */
        if (AnnotationUtils.isAnnotation(clazz, TracerMVC.class)) {
            if (!(AnnotationUtils.isAnnotation(clazz, Controller.class)
                    || AnnotationUtils.isAnnotation(clazz, RestController.class))) {
                throw new AnnotationTracerMvcNotFoundException(methodInvocation.getThis().getClass().getName());
            }
            traceId = MDC.get("traceId");
        }


        if (StringUtils.isBlank(traceId)) {
            traceId = TraceIdGeneratorUtils.generate();
            MDC.put("traceId", traceId);
        }

        String commom = clazz.getName() + "." + method.getName() + "()";
        long start = Clock.systemUTC().millis();
        /* 日志 */
        logger.info(commom + ", 请求入参: " + JSON.toJSONString(methodInvocation.getArguments()));
        Object proceed = null;
        try {
            proceed = methodInvocation.proceed();
        } finally {
            long end = Clock.systemUTC().millis() - start;
            logger.info(commom + ", 响应出参: " + JSON.toJSONString(proceed));
            logger.info(commom + ", 执行耗时(毫秒): " + end);
            MDC.clear();
        }
        return proceed;
    }

}
