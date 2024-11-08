package com.palomiklo.streampulse.header

import jakarta.servlet.http.HttpServletResponse

object Header {
    fun setHeaders(response: HttpServletResponse): HttpServletResponse {
        response.setContentType("text/event-stream")
        response.setCharacterEncoding("UTF-8")
        response.setHeader("Cache-Control", "no-cache")
        response.setHeader("Connection", "keep-alive")
        response.setHeader("Transfer-Encoding", "chunked")
        response.setHeader("X-Accel-Buffering", "no")
        return response
    }
}
