package com.palomiklo.streampulse.connection;

import com.palomiklo.streampulse.blueprint.IStreamPulseConnectionConfig;
import static com.palomiklo.streampulse.constant.Default.CONNECTION_CLEAN_UP_TIMEOUT;
import static com.palomiklo.streampulse.constant.Default.CONNECTION_TIMEOUT;
import static com.palomiklo.streampulse.constant.Default.INITIAL_DELAY;
import static com.palomiklo.streampulse.constant.Default.PING_INTERVAL;

public class DefaultStreamPulseConnectionConfig implements IStreamPulseConnectionConfig {

    @Override
    public byte getConnectionTimeout() {
        return CONNECTION_TIMEOUT;
    }

    @Override
    public byte getInitialDelay() {
        return INITIAL_DELAY;
    }

    @Override
    public byte getPingInterval() {
        return PING_INTERVAL;
    }

    @Override
    public short getConnectionCleanUpTimeout() {
        return CONNECTION_CLEAN_UP_TIMEOUT;
    }
}
