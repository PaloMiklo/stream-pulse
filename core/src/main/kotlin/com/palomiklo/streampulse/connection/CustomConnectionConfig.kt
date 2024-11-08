package com.palomiklo.streampulse.connection

import com.palomiklo.streampulse.blueprint.IConnectionConfig
import com.palomiklo.streampulse.constant.Default.Companion.CONNECTION_CLEAN_UP_TIMEOUT
import com.palomiklo.streampulse.constant.Default.Companion.CONNECTION_TIMEOUT
import com.palomiklo.streampulse.constant.Default.Companion.INITIAL_DELAY
import com.palomiklo.streampulse.constant.Default.Companion.PING
import com.palomiklo.streampulse.constant.Default.Companion.PING_INTERVAL
import com.palomiklo.streampulse.constant.Default.Companion.RECONNECT_EVENT

class CustomConnectionConfig private constructor(builder: Builder) : IConnectionConfig {
    override val connectionTimeout: Byte
    override val initialDelay: Byte
    override val pingInterval: Byte
    override val connectionCleanUpTimeout: Short
    override val ping: () -> String
    override val reconnectEvent: () -> String

    init {
        this.connectionTimeout = builder.connectionTimeout
        this.initialDelay = builder.initialDelay
        this.pingInterval = builder.pingInterval
        this.connectionCleanUpTimeout = builder.connectionCleanUpTimeout
        this.ping = builder.ping
        this.reconnectEvent = builder.reconnectEvent
    }

    class Builder {
        var connectionTimeout: Byte = CONNECTION_TIMEOUT
        var initialDelay: Byte = INITIAL_DELAY
        var pingInterval: Byte = PING_INTERVAL
        var connectionCleanUpTimeout: Short = CONNECTION_CLEAN_UP_TIMEOUT
        var ping: () -> String = PING
        var reconnectEvent: () -> String = RECONNECT_EVENT

        fun connectionTimeout(connectionTimeout: Byte): Builder = apply { this.connectionTimeout = connectionTimeout }
        fun initialDelay(initialDelay: Byte): Builder = apply { this.initialDelay = initialDelay }
        fun pingInterval(pingInterval: Byte): Builder = apply { this.pingInterval = pingInterval }
        fun connectionCleanUpTimeout(connectionCleanUpTimeout: Short): Builder = apply { this.connectionCleanUpTimeout = connectionCleanUpTimeout }
        fun ping(ping: () -> String): Builder = apply { this.ping = ping }
        fun reconnectEvent(reconnectEvent: () -> String): Builder = apply { this.reconnectEvent = reconnectEvent }
        fun build(): CustomConnectionConfig = CustomConnectionConfig(this)
    }

    companion object {
        fun streamPulseBuilder(): Builder = Builder()
    }
}
