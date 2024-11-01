package com.palomiklo.streampulse.connection;

import java.util.UUID;

import com.palomiklo.streampulse.config.ConfigLoader;
import com.palomiklo.streampulse.context.AsynchronousContext;

public record ConnectionInfo(UUID id, Connection connection, AsynchronousContext ctx) {

    public ConnectionInfo(UUID id, Connection connection, AsynchronousContext ctx) {
        ConfigLoader.initialize();
        this.id = id;
        this.connection = connection;
        this.ctx = ctx;
    }
}
