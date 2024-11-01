package com.palomiklo.streampulse.context;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public record AsynchronousContext(HttpServletRequest req, AsyncContext actx) {

    public static AsynchronousContext startAsyncContext(HttpServletRequest req, HttpServletResponse res) {
        req.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", true);
        AsyncContext actx = req.startAsync(req, res);
        actx.setTimeout(0);
        return new AsynchronousContext(req, actx);
    }
}
