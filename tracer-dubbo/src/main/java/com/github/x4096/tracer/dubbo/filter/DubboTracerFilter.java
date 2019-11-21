package com.github.x4096.tracer.dubbo.filter;

import com.github.x4096.tracer.common.utils.TraceIdGeneratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.slf4j.MDC;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: tracer
 * @DateTime: 2019-11-21 22:16
 * @Description:
 */
@Activate(group = { CommonConstants.PROVIDER, CommonConstants.CONSUMER }, value = "dubboTracerFilter", order = 1)
public class DubboTracerFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext rpcContext = RpcContext.getContext();
        String rpcTraceId = rpcContext.getAttachment("rpcTraceId");
        if (StringUtils.isBlank(rpcTraceId)) {
            rpcTraceId = TraceIdGeneratorUtils.generate();
            rpcContext.setAttachment("rpcTraceId", rpcTraceId);
        }
        MDC.put("traceId", rpcTraceId);
        return invoker.invoke(invocation);
    }

}
