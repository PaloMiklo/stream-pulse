package com.palomiklo.streampulse.blueprint

interface IConnectionConfig {
    val connectionTimeout: Byte
    val initialDelay: Byte
    val pingInterval: Byte
    val connectionCleanUpTimeout: Short
    val ping: () -> String
    val reconnectEvent: () -> String
}
