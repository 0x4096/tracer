package com.github.x4096.tracer.common.utils;


/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: tracer
 * @DateTime: 2019-11-23 11:55
 * @Description:
 */
public class AnnotationUtils {

    /**
     * 判断某个类是否是指定注解
     *
     * @param clazz
     * @param annotationClass
     * @return
     */
    public static boolean isAnnotation(Class clazz, Class annotationClass) {
        return clazz.getAnnotation(annotationClass) != null;
    }

}
