package com.github.x4096.tracer.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.x4096.tracer.configuration.TracerProperties;
import com.github.x4096.tracer.configuration.TracerSpringMvcProperties;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.time.Clock;
import java.util.*;

/**
 * @author 0x4096.peng@gmail.com
 * @project tracer
 * @datetime 2020/3/3 00:06
 * @description Mvc 方法拦截
 * @readme
 */
public class TracingMvcMethodInterceptor extends TracingMethodInterceptor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 包含日志 URL
     */
    private final Set<String> includeUrlSet = new HashSet<>();

    /**
     * 排除日志 URL
     */
    private final Set<String> excludeUrlSet = new HashSet<>();

    public TracingMvcMethodInterceptor(TracerProperties tracerProperties) {
        super(tracerProperties);

        TracerSpringMvcProperties springMvcProperties = tracerProperties.getSpringMvcProperties();
        if (null == springMvcProperties) {
            return;
        }

        String includeUrls = springMvcProperties.getIncludeUrls();
        if (Objects.nonNull(includeUrls)) {
            includeUrlSet.addAll(Arrays.asList(includeUrls.split(",")));
        }

        String excludeUrls = springMvcProperties.getExcludeUrls();
        if (Objects.nonNull(excludeUrls)) {
            String[] excludeUrlArray = excludeUrls.split(",");
            for (String excludeUrl : excludeUrlArray) {
                if ("".equals(excludeUrl)) {
                    continue;
                }

                if (includeUrlSet.contains(excludeUrl)) {
                    throw new IllegalArgumentException(String.format("includeUrl 中已包含 %s uri, ", excludeUrl));
                } else {
                    excludeUrlSet.add(excludeUrl);
                }
            }
        }
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        long start = Clock.systemUTC().millis();

        Method method = methodInvocation.getMethod();

        @SuppressWarnings("rawtypes")
        Class clazz = methodInvocation.getThis().getClass();

        if (!isRequest(method)) {
            return methodInvocation.proceed();
        }

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = null;
        if (null != servletRequestAttributes) {
            request = servletRequestAttributes.getRequest();
        }

        String common = clazz.getName() + "." + method.getName() + "()";

        /* 日志 */
        boolean urlLogOut = false;

        /* 针对  HttpServletRequest 和 HttpServletResponse 在使用JSON.toJSONString 时会报错处理 */
        Object[] objects = methodInvocation.getArguments();
        List<Object> argumentList = new ArrayList<>();
        if (null != objects && objects.length > 0) {
            for (Object object : objects) {
                if (!(object instanceof HttpServletRequest || object instanceof HttpServletResponse)) {
                    argumentList.add(object);
                }
            }
        }

        if (null != request) {
            String uri = request.getRequestURI();
            urlLogOut = (includeUrlSet.size() == 0 || includeUrlSet.contains(uri)) && !excludeUrlSet.contains(uri);

            if (tracerProperties.isMvcRequestLogOut() && urlLogOut) {
                logger.info("{}, requestParams: {}, requestUri: {}, requestHeaders: {}",
                        common, JSON.toJSONString(argumentList), request.getRequestURI(), JSON.toJSONString(request.getParameterMap()));
            }
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

            /* 响应出参 */
            if (urlLogOut && tracerProperties.isMvcResponseLogOut()) {
                HttpServletResponse response = servletRequestAttributes.getResponse();
                if (null != response) {
                    Collection<String> headers = response.getHeaderNames();
                    Map<String, String> headersMap = new HashMap<>(headers.size());
                    headers.forEach(headerKey -> headersMap.put(headerKey, response.getHeader(headerKey)));

                    logger.info("{}, responseParams: {}, responseHeaders: {}", common, responseContent, JSON.toJSONString(headersMap));
                }

            }

            /* 执行时间 */
            if (urlLogOut && tracerProperties.isMvcResponseTimeLogOut()) {
                logger.info("{}, executeTime(millisecond): {}", common, end);
            }

        }

        return proceed;
    }


    /**
     * 方式是否为 HTTP 请求
     *
     * @param method 执行的方法
     * @return 是否为 HTTP 请求
     */
    private boolean isRequest(Method method) {
        return (method.isAnnotationPresent(RequestMapping.class)
                || method.isAnnotationPresent(PostMapping.class)
                || method.isAnnotationPresent(GetMapping.class)
                || method.isAnnotationPresent(DeleteMapping.class));
    }

}
