package com.github.x4096.tracer.filter;

import com.github.x4096.tracer.utils.TraceIdGeneratorUtils;
import com.github.x4096.tracer.utils.TracerContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.slf4j.MDC;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: tracer
 * @DateTime: 2019-11-23 09:57
 * @Description: dubbo spi filter
 */
@Activate(group = {CommonConstants.PROVIDER, CommonConstants.CONSUMER}, value = "dubboTracerFilter", order = 1)
public class DubboTracerFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext rpcContext = RpcContext.getContext();
        String rpcTraceId = rpcContext.getAttachment("rpcTraceId");
        /* 获取是提供者还是消费者 */
        String side = rpcContext.getUrl().getParameter("side");
        if (StringUtils.isBlank(rpcTraceId)) {
            /* 只处理消费者, 若是提供者线程可能复用, 那么 MDC.get("traceId") 获取的数据非空, 导致 traceId 重复  */
            if ("consumer".equals(side)) {
                rpcTraceId = MDC.get("traceId");
            }

            if (StringUtils.isBlank(rpcTraceId)) {
                String remoteHost = rpcContext.getRemoteHost();
                if (null != remoteHost) {
                    rpcTraceId = TraceIdGeneratorUtils.getTraceId(remoteHost);
                } else {
                    rpcTraceId = TraceIdGeneratorUtils.generate();
                }
            }
            rpcContext.setAttachment("rpcTraceId", rpcTraceId);
        }

        if ("provider".equals(side)) {
            TracerContextUtils.setTraceId(rpcTraceId);
        }

        return invoker.invoke(invocation);
    }

}
