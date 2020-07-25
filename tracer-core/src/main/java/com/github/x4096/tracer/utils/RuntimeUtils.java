package com.github.x4096.tracer.utils;

import org.apache.commons.lang3.StringUtils;

import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: tracer
 * @DateTime: 2019-11-23 11:59
 * @Description: 参考: https://github.com/vipshop/vjtools
 */
public class RuntimeUtils {

    private RuntimeUtils() {
    }

    private static final AtomicInteger shutdownHookThreadIndex = new AtomicInteger(0);

    /**
     * 获得当前进程的PID
     * 当失败时返回-1
     */
    public static int getPid() {
        /* format: "pid@hostname" */
        String jvmName = ManagementFactory.getRuntimeMXBean().getName();
        String[] split = jvmName.split("@");
        if (split.length != 2) {
            return -1;
        }

        try {
            return Integer.parseInt(split[0]);
        } catch (Exception e) {
            return -1;
        }
    }


    /**
     * 返回应用启动到现在的毫秒数
     */
    public static long getUpTime() {
        return ManagementFactory.getRuntimeMXBean().getUptime();
    }


    /**
     * 返回输入的JVM参数列表
     */
    public static String getVmArguments() {
        List<String> vmArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
        return StringUtils.join(vmArguments, " ");
    }


    /**
     * 获取CPU核数
     */
    public static int getCores() {
        return Runtime.getRuntime().availableProcessors();
    }


    /**
     * 注册JVM关闭时的钩子程序
     */
    public static void addShutdownHook(Runnable runnable) {
        Runtime.getRuntime().addShutdownHook(
                new Thread(runnable, "Thread-ShutDownHook-" + shutdownHookThreadIndex.incrementAndGet()));
    }


    /**
     * 通过StackTrace，获得调用者的类名.
     * 获取StackTrace有消耗，不要滥用
     */
    public static String getCallerClass() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        if (stacktrace.length >= 4) {
            StackTraceElement element = stacktrace[3];
            return element.getClassName();
        } else {
            return StringUtils.EMPTY;
        }
    }


    /**
     * 通过StackTrace，获得调用者的"类名.方法名()"
     * 获取StackTrace有消耗，不要滥用
     */
    public static String getCallerMethod() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        if (stacktrace.length >= 4) {
            StackTraceElement element = stacktrace[3];
            return element.getClassName() + '.' + element.getMethodName() + "()";
        } else {
            return StringUtils.EMPTY;
        }
    }


    /**
     * 通过StackTrace，获得当前方法的类名.
     * <p>
     * 获取StackTrace有消耗，不要滥用
     */
    public static String getCurrentClass() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        if (stacktrace.length >= 3) {
            StackTraceElement element = stacktrace[2];
            return element.getClassName();
        } else {
            return StringUtils.EMPTY;
        }
    }


    /**
     * 通过StackTrace，获得当前方法的"类名.方法名()"
     * 获取StackTrace有消耗，不要滥用
     */
    public static String getCurrentMethod() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        if (stacktrace.length >= 3) {
            StackTraceElement element = stacktrace[2];
            return element.getClassName() + '.' + element.getMethodName() + "()";
        } else {
            return StringUtils.EMPTY;
        }
    }

}
