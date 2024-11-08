package com.palomiklo.streampulse.connection

import com.palomiklo.streampulse.config.ConfigLoader
import com.palomiklo.streampulse.context.AsynchronousContext
import com.palomiklo.streampulse.listener.Listener

@JvmRecord
data class ConnectionHolder(val connection: Connection, val ctx: AsynchronousContext) {
    init {
        ConfigLoader.initialize()
    }

    companion object {
        fun create(connection: Connection, ctx: AsynchronousContext): ConnectionHolder {
            val chldr = ConnectionHolder(connection, ctx)
            Listener.addListeners(ctx.actx, chldr)
            return chldr
        }
    }
}
