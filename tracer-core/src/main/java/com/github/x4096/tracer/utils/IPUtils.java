package com.github.x4096.tracer.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: tracer
 * @DateTime: 2019-11-23 11:58
 * @Description:
 */
public class IPUtils {

    private IPUtils() {
    }

    private static final String LOCAL_HOST_V4 = "127.0.0.1";

    private static final String LOCAL_HOST_V6 = "0:0:0:0:0:0:0:1";

    private static final int IPV4_MAX_LENGTH = 15;

    private static final String SEPARATOR = ",";

    private static final String UNKNOWN = "unknown";

    private static final Logger LOGGER = LoggerFactory.getLogger(IPUtils.class);

    /**
     * 获取主机 IP ,去除存在虚拟机情况
     */
    public static String getLocalIpAddr() {
        InetAddress candidateAddress = null;
        try {
            /* 遍历所有的网络接口 */
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                /* 在所有的接口下再遍历IP */
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    /* 排除loopback类型地址 */
                    if (!inetAddr.isLoopbackAddress()) {
                        if (inetAddr.isSiteLocalAddress()) {
                            /* 如果是site-local地址，就是它了 */
                            return inetAddr.getHostAddress();
                        } else if (candidateAddress == null) {
                            /* site-local类型的地址未被发现，先记录候选地址 */
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }

            if (candidateAddress != null) {
                return candidateAddress.getHostAddress();
            }

            /* 如果没有发现 non-loopback地址.只能用最次选的方案 */
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
            }
            return jdkSuppliedAddress.getHostAddress();
        } catch (Exception e) {
            LOGGER.error("", e);
        }

        return null;
    }


    /**
     * 获取网络请求IP地址
     */
    public static String getNetIpAddr(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");

        if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }

        if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (LOCAL_HOST_V4.equals(ipAddress) || LOCAL_HOST_V6.equals(ipAddress)) {
                /* 根据网卡取本机配置的IP */
                InetAddress inet;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    LOGGER.error("", e);
                    return null;
                }
                ipAddress = inet.getHostAddress();
            }
        }

        /* 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割 */
        if (ipAddress != null && ipAddress.length() > IPV4_MAX_LENGTH) {
            if (ipAddress.indexOf(SEPARATOR) > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(SEPARATOR));
            }
        }

        return ipAddress;
    }

}
