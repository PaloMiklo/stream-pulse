package com.palomiklo.streampulse.blueprint;

public interface IConnectionConfig {

    byte getConnectionTimeout();

    byte getInitialDelay();

    byte getPingInterval();

    short getConnectionCleanUpTimeout();

    String getPing();
}
