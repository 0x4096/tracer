### 主页
https://github.com/0x4096/tracer

### 追踪者
当某个接口服务调用过多时,我们很难追踪一次请求链,或者说这次请求在当前系统留下了哪些日志,
为了解决这个问题使用AOP记录日志,针对每次请求产生一个唯一的标志,让这个标志在当前请求链中一直携带下去,利用slf4j的MDC(Mapped Diagnostic Contexts) 将请求唯一标志输入
这样方便我们查询一个请求链的所有日志;

### 功能
1. HTTP服务(SpringMVC)
2. RPC服务(Dubbo) 

### 使用方式
1. 在SpringBoot启动类上使用 @EnableTracer 注解,表示启用该功能;
2. 默认拦截带有 @org.springframework.stereotype.Controller 注解的方法和带有 @org.apache.dubbo.config.annotation.Service 注解的方法, 针对MVC的请求不想拦截某个URI, 可自行配置
3. 配置日志格式

#### HTTP服务(SpringMVC)
配置详看: com.github.x4096.tracer.configuration.TracerProperties

#### RPC服务(Dubbo) 
配置详看: com.github.x4096.tracer.configuration.TracerProperties


#### 日志配置
```
<pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level %logger Line:%-3L  - traceId:[%X{traceId}] - %msg%n</pattern>
```

核心参数: %X{traceId}

### 特别鸣谢
SOFATracer: https://github.com/sofastack/sofa-tracer
