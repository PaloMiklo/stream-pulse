package com.palomiklo.streampulse.connection

import com.palomiklo.streampulse.blueprint.IConnectionConfig
import com.palomiklo.streampulse.constant.Default
import java.util.function.Supplier

class CustomConnectionConfig private constructor(builder: Builder) :
    IConnectionConfig {
    override
    val connectionTimeout: Byte

    override
    val initialDelay: Byte

    override
    val pingInterval: Byte

    override
    val connectionCleanUpTimeout: Short
    override val ping: Supplier<String>
    override val reconnectEvent: Supplier<String>

    init {
        this.connectionTimeout = builder.connectionTimeout
        this.initialDelay = builder.initialDelay
        this.pingInterval = builder.pingInterval
        this.connectionCleanUpTimeout = builder.connectionCleanUpTimeout
        this.ping = builder.ping
        this.reconnectEvent = builder.reconnectEvent
    }

    class Builder {
        var connectionTimeout: Byte = Default.Companion.CONNECTION_TIMEOUT
        var initialDelay: Byte = Default.Companion.INITIAL_DELAY
        var pingInterval: Byte = Default.Companion.PING_INTERVAL
        var connectionCleanUpTimeout: Short = Default.Companion.CONNECTION_CLEAN_UP_TIMEOUT
        var ping: Supplier<String> = Default.Companion.PING
        var reconnectEvent: Supplier<String> = Default.Companion.RECONNECT_EVENT

        fun connectionTimeout(connectionTimeout: Byte): Builder {
            this.connectionTimeout = connectionTimeout
            return this
        }

        fun initialDelay(initialDelay: Byte): Builder {
            this.initialDelay = initialDelay
            return this
        }

        fun pingInterval(pingInterval: Byte): Builder {
            this.pingInterval = pingInterval
            return this
        }

        fun connectionCleanUpTimeout(connectionCleanUpTimeout: Short): Builder {
            this.connectionCleanUpTimeout = connectionCleanUpTimeout
            return this
        }

        fun ping(ping: Supplier<String>): Builder {
            this.ping = ping
            return this
        }

        fun reconnectEvent(reconnectEvent: Supplier<String>): Builder {
            this.reconnectEvent = reconnectEvent
            return this
        }

        fun build(): CustomConnectionConfig {
            return CustomConnectionConfig(this)
        }
    }

    companion object {
        fun streamPulseBuilder(): Builder {
            return Builder()
        }
    }
}
