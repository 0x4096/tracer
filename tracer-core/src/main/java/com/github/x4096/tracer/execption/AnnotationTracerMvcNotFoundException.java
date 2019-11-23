package com.github.x4096.tracer.execption;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: tracer
 * @DateTime: 2019-11-23 09:43
 * @Description:
 */
public class AnnotationTracerMvcNotFoundException extends RuntimeException {

    public AnnotationTracerMvcNotFoundException(String msg) {
        super("当前类: " + msg + ", 包含@TracerMVC, 但缺失注解: @Controller 或 @RestController");
    }

}
