package com.payflow.paymentapi.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final String START_TIME = "start-time";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        long start = System.currentTimeMillis();
        request.setAttribute(START_TIME, start);

        String correlationId = request.getHeader("X-Correlation-ID");
        request.setAttribute("CID", correlationId);


        log.info("[REQ] {} {} cid={} contentType={} remote={}",
                request.getMethod(),
                request.getRequestURI(),
                correlationId,
                request.getContentType(),
                request.getRemoteAddr()
        );

        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex
    ) {
        long start = (Long) request.getAttribute(START_TIME);
        long totalMs = System.currentTimeMillis() - start;

        log.info("[RES] {} {} status={} time={}ms cid={}",
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus(),
                totalMs,
                request.getHeader("X-Correlation-ID")
        );
    }
}
