package com.palomiklo.streampulse.blueprint;

public interface IConnection {

    void sendEvent(String event);

    void closeConnection();

    boolean isConnected();
}
