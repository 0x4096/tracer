package com.github.x4096.tracer.utils;

import org.slf4j.MDC;

/**
 * Tracer 上下文工具类
 *
 * @author 0x4096.peng@gmail.com
 * @date 2020/7/24
 * @readme 注意: 若自定义使用了该工具类, 记得及时清除 {@link com.github.x4096.tracer.utils.TracerContextUtils#clear()} 避免OOM
 */
public class TracerContextUtils {

    private TracerContextUtils() {
    }

    private static final ThreadLocal<String> TRACE_ID_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 设置 TracerId
     */
    public static void setTraceId(String traceId) {
        MDC.put("traceId", traceId);
        TRACE_ID_THREAD_LOCAL.set(traceId);
    }

    /**
     * 获取当前 TracerId
     */
    public static String getTracerId() {
        return TRACE_ID_THREAD_LOCAL.get();
    }

    /**
     * 清除数据
     */
    public static void clear() {
        MDC.clear();
        TRACE_ID_THREAD_LOCAL.remove();
    }

}
