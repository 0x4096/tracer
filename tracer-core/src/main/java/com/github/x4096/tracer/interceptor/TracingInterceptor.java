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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
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

        boolean isMVC = false;
        boolean isRPC = false;

        /* Dubbo 服务 */
        if (AnnotationUtils.isAnnotation(clazz, TracerRPC.class)) {
            if (!(AnnotationUtils.isAnnotation(clazz, Service.class)
                    || AnnotationUtils.isAnnotation(clazz, com.alibaba.dubbo.config.annotation.Service.class))) {
                throw new AnnotationTracerRpcNotFoundException(methodInvocation.getThis().getClass().getName());
            }
            RpcContext rpcContext = RpcContext.getContext();
            traceId = rpcContext.getAttachment("rpcTraceId");
            isRPC = true;
        }

        /* MVC 服务 */
        if (AnnotationUtils.isAnnotation(clazz, TracerMVC.class)) {
            if (!(AnnotationUtils.isAnnotation(clazz, Controller.class)
                    || AnnotationUtils.isAnnotation(clazz, RestController.class))) {
                throw new AnnotationTracerMvcNotFoundException(methodInvocation.getThis().getClass().getName());
            }
            traceId = MDC.get("traceId");
            isMVC = true;
        }


        if (StringUtils.isBlank(traceId)) {
            traceId = TraceIdGeneratorUtils.generate();
            MDC.put("traceId", traceId);
        }

        String common = clazz.getName() + "." + method.getName() + "()";
        long start = Clock.systemUTC().millis();

        /* 日志 */

        if (isRPC) {
            logger.info(common + ", requestParams: " + JSON.toJSONString(methodInvocation.getArguments()));
        }

        if (isMVC) {
            StringBuilder sb = new StringBuilder();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            sb.append(common);
            sb.append(", requestParams: ").append(JSON.toJSONString(methodInvocation.getArguments()));
            sb.append(", requestUri: ").append(request.getRequestURI());
            sb.append(", requestHeader: ").append(JSON.toJSONString(request.getParameterMap()));
            logger.info(sb.toString());
        }


        Object proceed = null;
        try {
            proceed = methodInvocation.proceed();
        } catch (Exception e) {
            logger.error(common + ", error", e);
        } finally {
            long end = Clock.systemUTC().millis() - start;
            logger.info(common + ", responseParams: " + JSON.toJSONString(proceed));
            logger.info(common + ", executeTime(millisecond): " + end);
            MDC.clear();
        }
        return proceed;
    }

}
