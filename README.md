
### 追踪者
当某个接口服务调用过多时,我们很难追踪一次请求链,或者说这次请求在当前系统留下了哪些日志,
为了解决这个问题使用AOP记录日志,针对每次请求产生一个唯一的标志,让这个标志在当前请求链中一直携带下去,利用slf4j的MDC(Mapped Diagnostic Contexts) 将请求唯一标志输入
这样方便我们查询一个请求链的所有日志;

### 功能
1. HTTP服务(SpringMVC)
2. RPC服务(Dubbo) 

### 使用方式
1. 在SpringBoot启动类上使用 @EnableTracer 注解,表示启用该功能;
2. 在相应的接口服务使用注解
3. 配置日志格式

#### HTTP服务(SpringMVC)
在controller层增加注解 @TracerMVC

#### RPC服务(Dubbo) 
在Dubbo接口的实现类增加注解 @TracerRPC

#### 日志配置
以下是部分日志配置,更多可参考: https://github.com/0x4096/Spring-Boot-Learning-Demo/blob/master/dubbo/dubbo-provider/src/main/resources/logback.xml
```
<pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level %logger Line:%-3L  - traceId:[%X{traceId}] - %msg%n</pattern>
```

### 想法
对于日志输出这块,控制力度可以更颗粒化,比如请求入参,响应出参,请求耗时都可以选择是否打印以及全局控制日志是否打印;


### 特别鸣谢
SOFATracer: https://github.com/sofastack/sofa-tracer
