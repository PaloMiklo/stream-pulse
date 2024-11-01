package com.palomiklo.streampulse.connection;

import java.util.UUID;

import com.palomiklo.streampulse.config.ConfigLoader;

public record ConnectionInfo(UUID id, Connection connection) {

    public ConnectionInfo(UUID id, Connection connection) {
        ConfigLoader.initialize();
        this.id = id;
        this.connection = connection;
    }
}
