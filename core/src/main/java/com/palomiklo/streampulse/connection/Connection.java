package com.palomiklo.streampulse.connection;

import java.io.IOException;
import java.io.PrintWriter;
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

    public static ConnectionHolder createConnection(HttpServletRequest req, HttpServletResponse res) {
        ConnectionHolder chldr = ConnectionHolder.create(randomUUID(), new Connection(res), startAsyncContext(req, res));
        return chldr;
    }

    public static ConnectionHolder createConnection(IConnectionConfig conf, HttpServletRequest req, HttpServletResponse res) {
        ConnectionHolder chldr = ConnectionHolder.create(randomUUID(), new Connection(conf, res), startAsyncContext(req, res));
        return chldr;
    }

    private Connection(IConnectionConfig conf, HttpServletResponse res) {
        this.conf = conf;
        this.res = res;
        wrap(() -> initializeConnection(), "Failed to initialize connection: ");
    }

    private Connection(HttpServletResponse res) {
        this.conf = new DefaultConnectionConfig();
        this.res = res;
        wrap(() -> initializeConnection(), "Failed to initialize connection: ");
    }

    private void initializeConnection() throws IOException {
        setHeaders(res);

        this.writer = res.getWriter();
        log.debug("Connection established!");

        startHeartbeat();
    }

    private void startHeartbeat() {
        log.debug("Starting heartbeat...");

        exec.scheduleAtFixedRate(
                () -> wrap(() -> hearthbeat(), "Error during heartbeat: "),
                conf.getInitialDelay(), conf.getPingInterval(), SECONDS
        );

        stopHeartbeatTask = exec.schedule(() -> log.debug("{} minutes has passed. Stopping heartbeat...", conf.getConnectionTimeout() / 60), conf.getConnectionTimeout(), SECONDS);
    }

    private void hearthbeat() {
        if (isConnected()) {
            sendEvent("ping");
        }
    }

    @Override
    public void sendEvent(String event) {
        writeLock.lock();
        try {
            if (isConnected() && writer != null) {
                writer.write("data: " + event + "\n\n");
                writer.flush();
                log.debug("Event sent: {}", event);

                if (writer.checkError()) {
                    log.debug("Writer detected a connection error!");
                }
            } else {
                log.error("Connectin error occured!");
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
                log.debug("Connection closed!");
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
