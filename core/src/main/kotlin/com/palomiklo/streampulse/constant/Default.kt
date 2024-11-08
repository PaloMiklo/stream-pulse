package com.palomiklo.streampulse.constant

import java.util.function.Supplier

interface Default {
    companion object {
        const val CONNECTION_TIMEOUT: Byte = (1 * 60).toByte()
        const val INITIAL_DELAY: Byte = 0
        const val PING_INTERVAL: Byte = 5
        const val CONNECTION_CLEAN_UP_TIMEOUT: Short = (5 * 60).toShort()
        val PING: Supplier<String> = Supplier<String> { "ping" }
        val RECONNECT_EVENT: Supplier<String> = Supplier<String> { "reconnect" }
    }
}
