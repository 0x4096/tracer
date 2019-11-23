package com.github.x4096.tracer.execption;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: tracer
 * @DateTime: 2019-11-23 09:41
 * @Description:
 */
public class AnnotationTracerRpcNotFoundException extends RuntimeException {

    public AnnotationTracerRpcNotFoundException(String msg) {
        super("当前类: " + msg + "包含@TracerRPC, 但缺失注解: org.apache.dubbo.config.annotation.Service 或 com.alibaba.dubbo.config.annotation.Service");
    }

}
