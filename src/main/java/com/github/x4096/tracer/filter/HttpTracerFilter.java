package com.github.x4096.tracer.filter;

import com.github.x4096.tracer.utils.TraceIdGeneratorUtils;
import com.github.x4096.tracer.utils.TracerContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Http 服务追踪
 *
 * @author 0x4096.peng@gmail.com
 * @date 2020/7/25
 */
@WebFilter(urlPatterns = "/**")
@Order(Ordered.HIGHEST_PRECEDENCE + 1024)
@ConditionalOnProperty(prefix = "com.github.x4096.tracer", name = {"global-log-out", "mvc-log-out"}, matchIfMissing = true)
public class HttpTracerFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        /* do nothing */
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        String requestId = servletRequest.getHeader("RequestId");
        if (StringUtils.isBlank(requestId)) {
            requestId = TraceIdGeneratorUtils.getTraceId(servletRequest);
        }

        TracerContextUtils.setTraceId(requestId);

        try {
            chain.doFilter(request, response);
        } finally {
            TracerContextUtils.clear();
        }
    }

    @Override
    public void destroy() {
        /* do nothing */
    }

}
