package com.github.x4096.tracer.dubbo.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.x4096.tracer.common.utils.TraceIdGeneratorUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.RpcContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Clock;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: tracer
 * @DateTime: 2019-11-22 00:28
 * @Description:
 */
@Aspect
@Component
@Slf4j
public class DubboLoggerAop {

    @Pointcut(value = "${aop}")
    public void pointCut() {

    }


    @Before(value = "pointCut()")
    public void doBefore(JoinPoint joinPoint) {
        String rpcTraceId = RpcContext.getContext().getAttachment("rpcTraceId");
        if (null != rpcTraceId) {
            MDC.put("traceId", rpcTraceId);
        } else {
            MDC.put("traceId", TraceIdGeneratorUtils.generate());
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Around("pointCut()")
    public Object doBasicProfiling(ProceedingJoinPoint pjp) throws Throwable {
        long start = Clock.systemUTC().millis();

        String className = pjp.getTarget().getClass().getName();
        String methodName = pjp.getSignature().getName() + "()";
        Object[] args = pjp.getArgs();// 获取请求参数，可以校验属性

        String common = "Dubbo接口, " + className + "." + methodName;

        log.info(common + ", 请求入参: " + JSON.toJSONString(args, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty));

        Object obj = pjp.proceed();

        log.info(common + ", 响应出参: " + JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty));

        long end = Clock.systemUTC().millis() - start;
        log.info(common + ", 执行耗时(毫秒): " + end);
        return obj;
    }

    @After(value = "pointCut()")
    public void doAfter() {
        MDC.clear();
    }

}
