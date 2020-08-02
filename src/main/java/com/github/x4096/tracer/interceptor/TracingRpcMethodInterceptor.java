package com.github.x4096.tracer.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.x4096.tracer.configuration.TracerProperties;
import com.github.x4096.tracer.utils.TracerContextUtils;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author 0x4096.peng@gmail.com
 * @project tracer
 * @datetime 2020/3/3 00:07
 * @description Dubbo 接口追踪
 * @readme
 */
public class TracingRpcMethodInterceptor extends TracingMethodInterceptor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public TracingRpcMethodInterceptor(TracerProperties tracerProperties) {
        super(tracerProperties);
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        long start = Clock.systemUTC().millis();

        Method method = methodInvocation.getMethod();

        @SuppressWarnings("rawtypes")
        Class clazz = methodInvocation.getThis().getClass();

        String common = clazz.getName() + "." + method.getName() + "()";

        /* 请求入参 */
        Object[] objects = methodInvocation.getArguments();
        List<Object> argumentList = new ArrayList<>();
        if (null != objects && objects.length > 0) {
            Collections.addAll(argumentList, objects);
        }

        if (tracerProperties.isRpcRequestLogOut()) {
            logger.info("{}, requestParams: {}", common, JSON.toJSONString(argumentList));
        }


        /* 代理执行 */
        Object proceed = null;
        try {
            proceed = methodInvocation.proceed();
        } finally {
            long end = Clock.systemUTC().millis() - start;

            String responseContent;
            if (proceed instanceof String) {
                responseContent = proceed.toString();
            } else {
                try {
                    responseContent = JSON.toJSONString(proceed, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty, SerializerFeature.DisableCircularReferenceDetect);
                } catch (Exception e) {
                    // logger.error("JSON.toJSONString Error", e);
                    responseContent = "JSON.toJSONString Error Ignore.";
                }
            }

            if (tracerProperties.isRpcResponseLogOut()) {
                logger.info("{}, responseParams: {}", common, responseContent);
            }

            /* 执行时间 */
            if (tracerProperties.isRpcResponseTimeLogOut()) {
                logger.info("{}, executeTime(millisecond): {}", common, end);
            }

            TracerContextUtils.clear();
        }

        return proceed;
    }

}
