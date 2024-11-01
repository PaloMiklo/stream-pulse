package com.palomiklo.streampulse.connection;

import java.io.IOException;
import java.io.PrintWriter;
import static java.util.UUID.randomUUID;
import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;
import java.util.concurrent.ScheduledExecutorService;
import static java.util.concurrent.TimeUnit.SECONDS;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.palomiklo.streampulse.blueprint.IConnectionConfig;
import static com.palomiklo.streampulse.context.AsynchronousContext.startAsyncContext;
import static com.palomiklo.streampulse.header.Header.setHeaders;
import static com.palomiklo.streampulse.thread.CustomThreadFactory.streamPulseThreadFactory;
import static com.palomiklo.streampulse.util.Wrap.wrap;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Connection {

    private final Logger log = LoggerFactory.getLogger(Connection.class);
    private final AtomicBoolean connected = new AtomicBoolean(true);
    private final Lock writeLock = new ReentrantLock();
    private final IConnectionConfig conf;
    private final HttpServletRequest req;
    private final HttpServletResponse res;
    private final ScheduledExecutorService exec = newSingleThreadScheduledExecutor(streamPulseThreadFactory);
    private PrintWriter writer;

    public static ConnectionInfo createConnection(HttpServletRequest req, HttpServletResponse res) {
        return new ConnectionInfo(randomUUID(), new Connection(req, res));
    }

    public static ConnectionInfo createConnection(IConnectionConfig conf, HttpServletRequest req, HttpServletResponse res) {
        return new ConnectionInfo(randomUUID(), new Connection(conf, req, res));
    }

    private Connection(IConnectionConfig conf, HttpServletRequest req, HttpServletResponse res) {
        this.conf = conf;
        this.res = res;
        this.req = req;
        wrap(() -> initializeConnection(), "Failed to initialize connection: ");
    }

    private Connection(HttpServletRequest req, HttpServletResponse res) {
        this.conf = new DefaultConnection();
        this.req = req;
        this.res = res;
        wrap(() -> initializeConnection(), "Failed to initialize connection: ");
    }

    private void initializeConnection() throws IOException {
        startAsyncContext(req, res);
        setHeaders(res);

        this.writer = res.getWriter();
        log.debug("Connection established!");

        startHeartbeat();
    }

    public void sendEvent(String event) {
        writeLock.lock();
        try {
            if (isConnected() && writer != null) {
                writer.write("data: " + event + "\n\n");
                writer.flush();
                log.debug("Event sent: {}", event);

                if (writer.checkError()) {
                    log.debug("Connection error detected!");
                    closeConnection();
                }
            }
        } finally {
            writeLock.unlock();
        }
    }

    private void startHeartbeat() {
        log.debug("Starting heartbeat...");

        exec.scheduleAtFixedRate(
                () -> wrap(() -> hearthbeat(), "Error during heartbeat: ", () -> closeConnection()),
                conf.getInitialDelay(), conf.getPingInterval(), SECONDS
        );

        exec.schedule(() -> stopHearthbeat(), conf.getConnectionTimeout(), SECONDS);
    }

    private void hearthbeat() {
        if (isConnected()) {
            sendEvent("ping");
        } else {
            closeConnection();
        }
    }

    private void stopHearthbeat() {
        log.debug("%s minutes has passed. Stopping heartbeat...", conf.getConnectionTimeout() / 60);
        closeConnection();
    }

    private void closeConnection() {
        connected.set(false);
        if (writer != null) {
            writer.close();
        }
        exec.shutdown();
        log.debug("Connection closed!");
    }

    private boolean isConnected() {
        return connected.get();
    }
}
