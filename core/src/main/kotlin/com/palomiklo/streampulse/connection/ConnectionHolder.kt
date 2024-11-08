package com.palomiklo.streampulse.connection

import com.palomiklo.streampulse.config.ConfigLoader.Companion.initializeConfig
import com.palomiklo.streampulse.context.AsynchronousContext
import com.palomiklo.streampulse.listener.Listener

@JvmRecord
data class ConnectionHolder(val connection: Connection, val ctx: AsynchronousContext) {
    init {
        initializeConfig()
    }

    companion object {
        fun create(connection: Connection, ctx: AsynchronousContext): ConnectionHolder =
            ConnectionHolder(connection, ctx).also { chldr -> Listener.addListeners(ctx.actx, chldr) }
    }
}
