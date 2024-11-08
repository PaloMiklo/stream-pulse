package com.palomiklo.streampulse.context

import jakarta.servlet.AsyncContext
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

@JvmRecord
data class AsynchronousContext(val req: HttpServletRequest, val actx: AsyncContext) {
    companion object {
        fun startAsyncContext(req: HttpServletRequest, res: HttpServletResponse?) =
            AsynchronousContext(req, req.startAsync(req, res).apply {
                req.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", true)
                timeout = 0
            })
    }
}