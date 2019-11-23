package com.github.x4096.tracer.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(IPUtils.class);

    /**
     * 获取主机 IP ,去除存在虚拟机情况
     *
     * @return
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

}
