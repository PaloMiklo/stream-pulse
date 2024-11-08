package com.palomiklo.streampulse.header

import jakarta.servlet.http.HttpServletResponse

object Header {
    fun setHeaders(res: HttpServletResponse): HttpServletResponse = res.apply {
        contentType = "text/event-stream"
        characterEncoding = "UTF-8"
        setHeader("Cache-Control", "no-cache")
        setHeader("Connection", "keep-alive")
        setHeader("Transfer-Encoding", "chunked")
        setHeader("X-Accel-Buffering", "no")
    }
}
