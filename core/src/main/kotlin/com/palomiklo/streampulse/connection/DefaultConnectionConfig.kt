package com.palomiklo.streampulse.connection

import com.palomiklo.streampulse.blueprint.IConnectionConfig
import com.palomiklo.streampulse.constant.Default.Companion.CONNECTION_CLEAN_UP_TIMEOUT
import com.palomiklo.streampulse.constant.Default.Companion.CONNECTION_TIMEOUT
import com.palomiklo.streampulse.constant.Default.Companion.INITIAL_DELAY
import com.palomiklo.streampulse.constant.Default.Companion.PING
import com.palomiklo.streampulse.constant.Default.Companion.PING_INTERVAL
import com.palomiklo.streampulse.constant.Default.Companion.RECONNECT_EVENT

class DefaultConnectionConfig : IConnectionConfig {
    override val connectionTimeout: Byte = CONNECTION_TIMEOUT
    override val initialDelay: Byte = INITIAL_DELAY
    override val pingInterval: Byte = PING_INTERVAL
    override val connectionCleanUpTimeout: Short = CONNECTION_CLEAN_UP_TIMEOUT
    override val ping: () -> String = PING
    override val reconnectEvent: () -> String = RECONNECT_EVENT
}
