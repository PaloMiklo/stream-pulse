package com.palomiklo.streampulse.connection;

import java.util.UUID;

import com.palomiklo.streampulse.config.ConfigLoader;
import com.palomiklo.streampulse.context.AsynchronousContext;
import com.palomiklo.streampulse.listener.Listener;

public record ConnectionHolder(UUID id, Connection connection, AsynchronousContext ctx) {

    public ConnectionHolder   {
        ConfigLoader.initialize();
    }

    public static ConnectionHolder create(UUID id, Connection connection, AsynchronousContext ctx) {
        ConnectionHolder chldr = new ConnectionHolder(id, connection, ctx);
        Listener.addListeners(ctx.actx(), chldr);
        return chldr;
    }
}
