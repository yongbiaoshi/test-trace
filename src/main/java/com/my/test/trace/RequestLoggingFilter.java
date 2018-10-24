package com.my.test.trace;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@WebFilter(urlPatterns = "/*", initParams = {@WebInitParam(name = "includeQueryString", value = "true"),
        @WebInitParam(name = "includeClientInfo", value = "true"),
        @WebInitParam(name = "includeHeaders", value = "true"), @WebInitParam(name = "includePayload", value = "true"),
        @WebInitParam(name = "maxPayloadLength", value = "1024")})
@Slf4j
public class RequestLoggingFilter extends CommonsRequestLoggingFilter {

    @Override
    protected void initFilterBean() throws ServletException {
        super.initFilterBean();
        FilterConfig filterConfig = getFilterConfig();
        if (filterConfig != null) {
            this.setIncludeQueryString(Boolean.valueOf(filterConfig.getInitParameter("includeQueryString")));
            this.setIncludeClientInfo(Boolean.valueOf(filterConfig.getInitParameter("includeClientInfo")));
            this.setIncludeHeaders(Boolean.valueOf(filterConfig.getInitParameter("includeHeaders")));
            this.setIncludePayload(Boolean.valueOf(filterConfig.getInitParameter("includePayload")));
            String maxPayloadLength = filterConfig.getInitParameter("maxPayloadLength");
            if (maxPayloadLength != null) {
                this.setMaxPayloadLength(Integer.valueOf(filterConfig.getInitParameter("maxPayloadLength")));
            }
        }
    }

    @Override
    protected boolean shouldLog(HttpServletRequest request) {
        return this.logger.isInfoEnabled();
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        MDC.put("localAddr", request.getLocalAddr());
        String requestId = request.getHeader("x-request-id");
        if (!StringUtils.isEmpty(requestId)) {
            MDC.put("requestId", requestId);
        }
        request.setAttribute("timestamp", System.currentTimeMillis());
        this.logger.info(message);
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        Object start = request.getAttribute("timestamp");
        long requestTime = System.currentTimeMillis() - Long.valueOf(Objects.toString(start, "0"));
        MDC.put("duration", requestTime + "ms");
        this.logger.info(message);
        // 清理MDC
        MDC.remove("duration");
        MDC.remove("requestId");
    }

}
