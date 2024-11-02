package com.palomiklo.streampulse.connection;

import com.palomiklo.streampulse.blueprint.IConnectionConfig;
import static com.palomiklo.streampulse.constant.Default.CONNECTION_CLEAN_UP_TIMEOUT;
import static com.palomiklo.streampulse.constant.Default.CONNECTION_TIMEOUT;
import static com.palomiklo.streampulse.constant.Default.INITIAL_DELAY;
import static com.palomiklo.streampulse.constant.Default.PING;
import static com.palomiklo.streampulse.constant.Default.PING_INTERVAL;

public class CustomConnectionConfig implements IConnectionConfig {

    private final byte connectionTimeout;
    private final byte initialDelay;
    private final byte pingInterval;
    private final short connectionCleanUpTimeout;
    private final String ping;

    private CustomConnectionConfig(Builder builder) {
        this.connectionTimeout = builder.connectionTimeout;
        this.initialDelay = builder.initialDelay;
        this.pingInterval = builder.pingInterval;
        this.connectionCleanUpTimeout = builder.connectionCleanUpTimeout;
        this.ping = builder.ping;
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
    public String getPing() {
        return ping;
    }

    public static final class Builder {

        private byte connectionTimeout = CONNECTION_TIMEOUT;
        private byte initialDelay = INITIAL_DELAY;
        private byte pingInterval = PING_INTERVAL;
        private short connectionCleanUpTimeout = CONNECTION_CLEAN_UP_TIMEOUT;
        private String ping = PING;

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

        public Builder ping(String ping) {
            this.ping = ping;
            return this;
        }

        public CustomConnectionConfig build() {
            return new CustomConnectionConfig(this);
        }
    }
}
