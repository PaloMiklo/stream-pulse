package com.palomiklo.streampulse.connection;

import jakarta.servlet.http.HttpServletResponse;

public class Header {
    public static HttpServletResponse setHeaders(HttpServletResponse response){
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Connection", "keep-alive");
        response.setHeader("Transfer-Encoding", "chunked");
        response.setHeader("X-Accel-Buffering", "no");
        return response;
    }
}
