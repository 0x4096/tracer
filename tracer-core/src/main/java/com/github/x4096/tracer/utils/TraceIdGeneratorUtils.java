package com.github.x4096.tracer.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.Clock;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: tracer
 * @DateTime: 2019-11-21 22:35
 * @Description: 参考来源: https://github.com/sofastack/sofa-tracer
 */
public class TraceIdGeneratorUtils {

    private static String IP_16 = "ffffffff";
    private static final AtomicInteger count = new AtomicInteger(1000);

    static {
        try {
            String ipAddress = IPUtils.getLocalIpAddr();
            if (ipAddress != null) {
                IP_16 = getIP_16(ipAddress);
            }
        } catch (Throwable e) {
            /*
             * empty catch block
             */
        }
    }

    /**
     * 生成 traceId
     *
     * @return 30位 traceId exp: c0a81f3e1595607314527100116667
     */
    public static String generate() {
        return getTraceId(IP_16, Clock.systemUTC().millis(), getNextId());
    }

    /**
     * 根据请求IP生成 traceId
     *
     * @param ip 10进制IP
     * @return traceId
     */
    public static String getTraceId(String ip) {
        return getTraceId(getIP_16(ip), Clock.systemUTC().millis(), getNextId());
    }

    /**
     * 根据HTTP请求生成 traceId
     *
     * @param request httpRequest
     * @return traceId
     */
    public static String getTraceId(HttpServletRequest request) {
        String netIp = IPUtils.getNetIpAddr(request);
        return getTraceId(getIP_16(null == netIp ? IP_16 : netIp), Clock.systemUTC().millis(), getNextId());
    }

    /**
     * 10进制IP转16进制
     *
     * @param ip 10进制IP
     * @return 16进制IP
     */
    public static String getIP_16(String ip) {
        String[] ips = ip.split("\\.");
        StringBuilder sb = new StringBuilder();
        for (String column : ips) {
            String hex = Integer.toHexString(Integer.parseInt(column));
            if (hex.length() == 1) {
                sb.append('0').append(hex);
            } else {
                sb.append(hex);
            }

        }
        return sb.toString();
    }

    /**
     * 从 traceId 还原请求 IP
     *
     * @param traceId traceId
     * @return 服务请求 IP
     * @apiNote 需要注意的是, traceId 前面那段地址不一定是 IP
     */
    public static String reductionIPFromTraceId(String traceId) {
        String ip16 = StringUtils.substring(traceId, 0, 8);
        int length = null == ip16 ? 0 : ip16.length();

        if (length != 8) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        String str;

        char[] chars = ip16.toCharArray();
        for (int i = 0; i < length; i++) {
            if (i % 2 == 0) {
                str = chars[i] + "" + chars[i + 1];
                sb.append(Integer.parseInt(str, 16));
            } else if (i != 7) {
                sb.append(".");
            }
        }

        return sb.toString();
    }

    /**
     * 从 traceId 还原请求时间
     *
     * @param traceId traceId 详见生成规则 {@link TraceIdGeneratorUtils#getTraceId(java.lang.String, long, int)}
     * @return 服务请求时间
     * @apiNote 需要注意的是, traceId 生成规则不一定包含时间
     */
    public static Date reductionRequestDateFromTraceId(String traceId) {
        String timestamp = StringUtils.substring(traceId, 8, 21);
        return new Date(Long.parseLong(timestamp));
    }

    private static int getNextId() {
        for (; ; ) {
            int current = count.get();
            int next = (current > 9000) ? 1000 : current + 1;
            if (count.compareAndSet(current, next)) {
                return next;
            }
        }
    }

    private static String getTraceId(String ip, long timestamp, int nextId) {
        StringBuilder appender = new StringBuilder(32);
        appender.append(ip).append(timestamp).append(nextId).append(RuntimeUtils.getPid());
        return appender.toString();
    }

}
