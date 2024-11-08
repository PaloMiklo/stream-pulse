package com.palomiklo.streampulse.blueprint

import java.util.function.Supplier

interface IConnectionConfig {
    val connectionTimeout: Byte

    val initialDelay: Byte

    val pingInterval: Byte

    val connectionCleanUpTimeout: Short

    val ping: Supplier<String>

    val reconnectEvent: Supplier<String>
}
