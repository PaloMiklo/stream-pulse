package com.palomiklo.streampulse.blueprint;

import java.util.function.Supplier;

public interface IConnectionConfig {

    byte getConnectionTimeout();

    byte getInitialDelay();

    byte getPingInterval();

    short getConnectionCleanUpTimeout();

    Supplier<String> getPing();

    Supplier<String> getReconnectEvent();
}
