package com.palomiklo.streampulse.connection;

import java.util.function.Supplier;

import com.palomiklo.streampulse.blueprint.IConnectionConfig;
import static com.palomiklo.streampulse.constant.Default.CONNECTION_CLEAN_UP_TIMEOUT;
import static com.palomiklo.streampulse.constant.Default.CONNECTION_TIMEOUT;
import static com.palomiklo.streampulse.constant.Default.INITIAL_DELAY;
import static com.palomiklo.streampulse.constant.Default.PING;
import static com.palomiklo.streampulse.constant.Default.PING_INTERVAL;
import static com.palomiklo.streampulse.constant.Default.RECONNECT_EVENT;

public class CustomConnectionConfig implements IConnectionConfig {

    private final byte connectionTimeout;
    private final byte initialDelay;
    private final byte pingInterval;
    private final short connectionCleanUpTimeout;
    private final Supplier<String> ping;
    private final Supplier<String> reconnectEvent;

    private CustomConnectionConfig(Builder builder) {
        this.connectionTimeout = builder.connectionTimeout;
        this.initialDelay = builder.initialDelay;
        this.pingInterval = builder.pingInterval;
        this.connectionCleanUpTimeout = builder.connectionCleanUpTimeout;
        this.ping = builder.ping;
        this.reconnectEvent = builder.reconnectEvent;
    }

    public static Builder streamPulseBuilder() {
        return new Builder();
    }

    @Override
    public byte getConnectionTimeout() {
        return connectionTimeout;
    }

    @Override
    public byte getInitialDelay() {
        return initialDelay;
    }

    @Override
    public byte getPingInterval() {
        return pingInterval;
    }

    @Override
    public short getConnectionCleanUpTimeout() {
        return connectionCleanUpTimeout;
    }

    @Override
    public Supplier<String> getPing() {
        return ping;
    }

    @Override
    public Supplier<String> getReconnectEvent() {
        return reconnectEvent;
    }

    public static final class Builder {

        private byte connectionTimeout = CONNECTION_TIMEOUT;
        private byte initialDelay = INITIAL_DELAY;
        private byte pingInterval = PING_INTERVAL;
        private short connectionCleanUpTimeout = CONNECTION_CLEAN_UP_TIMEOUT;
        private Supplier<String> ping = PING;
        private Supplier<String> reconnectEvent = RECONNECT_EVENT;

        public Builder connectionTimeout(byte connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public Builder initialDelay(byte initialDelay) {
            this.initialDelay = initialDelay;
            return this;
        }

        public Builder pingInterval(byte pingInterval) {
            this.pingInterval = pingInterval;
            return this;
        }

        public Builder connectionCleanUpTimeout(short connectionCleanUpTimeout) {
            this.connectionCleanUpTimeout = connectionCleanUpTimeout;
            return this;
        }

        public Builder ping(Supplier<String> ping) {
            this.ping = ping;
            return this;
        }

        public Builder reconnectEvent(Supplier<String> reconnectEvent) {
            this.reconnectEvent = reconnectEvent;
            return this;
        }

        public CustomConnectionConfig build() {
            return new CustomConnectionConfig(this);
        }
    }
}
