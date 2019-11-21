package com.github.x4096.tracer.common.utils;

import com.github.x4096.common.utils.env.system.XRuntimeUtils;
import com.github.x4096.common.utils.network.IPUtils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: tracer
 * @DateTime: 2019-11-21 22:35
 * @Description:
 */
public class TraceIdGeneratorUtils {

    private static String IP_16 = "ffffffff";
    private static AtomicInteger count = new AtomicInteger(1000);

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

    private static String getTraceId(String ip, long timestamp, int nextId) {
        StringBuilder appender = new StringBuilder(30);
        appender.append(ip).append(timestamp).append(nextId).append(XRuntimeUtils.getPid());
        return appender.toString();
    }

    public static String generate() {
        return getTraceId(IP_16, System.currentTimeMillis(), getNextId());
    }

    private static String getIP_16(String ip) {
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

    private static int getNextId() {
        for (; ; ) {
            int current = count.get();
            int next = (current > 9000) ? 1000 : current + 1;
            if (count.compareAndSet(current, next)) {
                return next;
            }
        }
    }

}
