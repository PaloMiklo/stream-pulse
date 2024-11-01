package com.palomiklo.streampulse.context;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public record AsynchronousContext(HttpServletRequest request, AsyncContext asyncContext) {
    public static AsynchronousContext startAsyncContext(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", true);
        AsyncContext asyncContext = request.startAsync(request, response);
        asyncContext.setTimeout(0);
        return new AsynchronousContext(request, asyncContext);
    }
}
