package com.palomiklo.streampulse.connection;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;
import static java.util.UUID.randomUUID;
import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import static java.util.concurrent.TimeUnit.SECONDS;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.palomiklo.streampulse.blueprint.IConnection;
import com.palomiklo.streampulse.blueprint.IConnectionConfig;
import static com.palomiklo.streampulse.context.AsynchronousContext.startAsyncContext;
import static com.palomiklo.streampulse.header.Header.setHeaders;
import static com.palomiklo.streampulse.thread.CustomThreadFactory.streamPulseThreadFactory;
import static com.palomiklo.streampulse.util.Wrap.wrap;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Connection implements IConnection {

    private final Logger log = LoggerFactory.getLogger(Connection.class);
    private final AtomicBoolean connected = new AtomicBoolean(true);
    private final Lock writeLock = new ReentrantLock();
    private final IConnectionConfig conf;
    private final HttpServletResponse res;
    private final ScheduledExecutorService exec = newSingleThreadScheduledExecutor(streamPulseThreadFactory);
    private PrintWriter writer;
    private ScheduledFuture<?> stopHeartbeatTask;
    public final UUID id;

    public static ConnectionHolder createConnection(HttpServletRequest req, HttpServletResponse res) {
        ConnectionHolder chldr = ConnectionHolder.create(new Connection(res), startAsyncContext(req, res));
        return chldr;
    }

    public static ConnectionHolder createConnection(IConnectionConfig conf, HttpServletRequest req, HttpServletResponse res) {
        ConnectionHolder chldr = ConnectionHolder.create(new Connection(conf, res), startAsyncContext(req, res));
        return chldr;
    }

    private Connection(IConnectionConfig conf, HttpServletResponse res) {
        this.id = randomUUID();
        this.conf = conf;
        this.res = res;
        wrap(() -> initializeConnection(), "Failed to initialize connection for ID: {}: " + id);
    }

    private Connection(HttpServletResponse res) {
        this.id = randomUUID();
        this.conf = new DefaultConnectionConfig();
        this.res = res;
        wrap(() -> initializeConnection(), "Failed to initialize connection for ID: {}: " + id);
    }

    private void initializeConnection() throws IOException {
        setHeaders(res);

        this.writer = res.getWriter();
        log.debug("Connection established for ID: {}!", id);

        startHeartbeat();
    }

    private void startHeartbeat() {
        log.debug("Starting heartbeat for connection ID: {}...", id);

        exec.scheduleAtFixedRate(
                () -> wrap(() -> hearthbeat(), "Error during heartbeat for connection ID: " + id),
                conf.getInitialDelay(), conf.getPingInterval(), SECONDS
        );

        stopHeartbeatTask = exec.schedule(() -> {
            log.debug("{} minutes has passed. Stopping heartbeat for connection ID: {}...", conf.getConnectionTimeout() / 60, id);
            closeConnection();
        }, conf.getConnectionTimeout(), SECONDS);

    }

    private void hearthbeat() {
        if (isConnected()) {
            sendEvent(String.format("Connection ID %s; event data: %s", id, conf.getPing().get()));
        }
    }

    @Override
    public void sendEvent(String event) {
        writeLock.lock();
        try {
            if (isConnected() && writer != null) {
                writer.write("data: " + event + "\n\n");
                writer.flush();
                log.debug("CID: {} -> EVENT: {}", id, event);

                if (writer.checkError()) {
                    log.debug("Writer detected a connection error for ID: {}!", id);
                }
            } else {
                log.error("Error occured for connection ID: {}!", id);
            }
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void closeConnection() {
        writeLock.lock();
        try {
            if (connected.compareAndSet(true, false)) {
                if (stopHeartbeatTask != null) {
                    stopHeartbeatTask.cancel(true);
                }
                exec.shutdownNow();
                if (writer != null) {
                    writer.close();
                }
                log.debug("Connection with ID: {} closed!", id);
            }
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean isConnected() {
        return connected.get();
    }
}
