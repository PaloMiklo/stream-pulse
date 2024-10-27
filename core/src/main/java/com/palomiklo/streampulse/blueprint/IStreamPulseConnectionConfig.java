package com.palomiklo.streampulse.blueprint;

public interface IStreamPulseConnectionConfig {
    byte getConnectionTimeout();
    byte getInitialDelay();
    byte getPingInterval();
    short getConnectionCleanUpTimeout();
}