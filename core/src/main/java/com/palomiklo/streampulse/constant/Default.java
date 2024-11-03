package com.palomiklo.streampulse.constant;

import java.util.function.Supplier;

public interface Default {

    byte CONNECTION_TIMEOUT = 1 * 60;
    byte INITIAL_DELAY = 0;
    byte PING_INTERVAL = 5;
    short CONNECTION_CLEAN_UP_TIMEOUT = 5 * 60;
    Supplier<String> PING = () -> "ping";
}
