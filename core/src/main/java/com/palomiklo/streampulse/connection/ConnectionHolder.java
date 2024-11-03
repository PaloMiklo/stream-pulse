package com.palomiklo.streampulse.connection;

import com.palomiklo.streampulse.config.ConfigLoader;
import com.palomiklo.streampulse.context.AsynchronousContext;
import com.palomiklo.streampulse.listener.Listener;

public record ConnectionHolder(Connection connection, AsynchronousContext ctx) {

    public ConnectionHolder  {
        ConfigLoader.initialize();
    }

    public static ConnectionHolder create(Connection connection, AsynchronousContext ctx) {
        ConnectionHolder chldr = new ConnectionHolder(connection, ctx);
        Listener.addListeners(ctx.actx(), chldr);
        return chldr;
    }
}
