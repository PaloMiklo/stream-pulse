package com.palomiklo.streampulse.connection

import com.palomiklo.streampulse.blueprint.IConnectionConfig
import com.palomiklo.streampulse.constant.Default
import java.util.function.Supplier

class DefaultConnectionConfig : IConnectionConfig {
    override
    val connectionTimeout: Byte
        get() = Default.CONNECTION_TIMEOUT

    override
    val initialDelay: Byte
        get() = Default.INITIAL_DELAY

    override
    val pingInterval: Byte
        get() = Default.PING_INTERVAL

    override
    val connectionCleanUpTimeout: Short
        get() = Default.CONNECTION_CLEAN_UP_TIMEOUT

    override
    val ping: Supplier<String>
        get() = Default.PING

    override
    val reconnectEvent: Supplier<String>
        get() = Default.RECONNECT_EVENT
}
