package com.palomiklo.streampulse.connection;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import static java.util.concurrent.TimeUnit.SECONDS;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.palomiklo.streampulse.blueprint.IConnectionConfig;
import com.palomiklo.streampulse.config.DefaultConnection;
import static com.palomiklo.streampulse.context.AsynchronousContext.startAsyncContext;
import static com.palomiklo.streampulse.header.Header.setHeaders;
import static com.palomiklo.streampulse.thread.CustomThreadFactory.streamPulseThreadFactory;
import static com.palomiklo.streampulse.util.Wrap.wrap;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Connection {
    private final Logger logger = LoggerFactory.getLogger(Connection.class);
    private final AtomicBoolean connected = new AtomicBoolean(true);
    private final Lock writeLock = new ReentrantLock();
    private final IConnectionConfig config;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final ScheduledExecutorService executor = Executors
            .newSingleThreadScheduledExecutor(streamPulseThreadFactory);
    private PrintWriter writer;

    public static Connection createConnection(HttpServletRequest request, HttpServletResponse response) {
        return new Connection(request, response);
    }

    public static Connection createConnection(IConnectionConfig conf, HttpServletRequest request,
            HttpServletResponse response) {
        return new Connection(conf, request, response);
    }

    private Connection(IConnectionConfig conf, HttpServletRequest request, HttpServletResponse response) {
        this.config = conf;
        this.response = response;
        this.request = request;
        wrap(() -> initializeConnection(), "Failed to initialize connection: ");
    }

    private Connection(HttpServletRequest request, HttpServletResponse response) {
        this.config = new DefaultConnection();
        this.request = request;
        this.response = response;
        wrap(() -> initializeConnection(), "Failed to initialize connection: ");
    }

    private void initializeConnection() throws IOException {
        startAsyncContext(request, response);
        setHeaders(response);

        this.writer = response.getWriter();
        logger.debug("Connection established!");

        startHeartbeat();
    }

    public void sendEvent(String event) {
        writeLock.lock();
        try {
            if (isConnected() && writer != null) {
                writer.write("data: " + event + "\n\n");
                writer.flush();
                logger.debug("Event sent: {}", event);

                if (writer.checkError()) {
                    logger.debug("Connection error detected!");
                    closeConnection();
                }
            }
        } finally {
            writeLock.unlock();
        }
    }

    private void startHeartbeat() {
        logger.debug("Starting heartbeat...");

        executor.scheduleAtFixedRate(
                () -> wrap(() -> hearthbeat(), "Error during heartbeat: ", () -> closeConnection()),
                config.getInitialDelay(), config.getPingInterval(), SECONDS);

        executor.schedule(() -> stopHearthbeat(), config.getConnectionTimeout(), SECONDS);
    }

    private void hearthbeat() {
        if (isConnected())
            sendEvent("ping");
        else
            closeConnection();
    }

    private void stopHearthbeat() {
        logger.debug("%s minutes has passed. Stopping heartbeat...", config.getConnectionTimeout() / 60);
        closeConnection();
    }

    private void closeConnection() {
        connected.set(false);
        if (writer != null)
            writer.close();
        executor.shutdown();
        logger.debug("Connection closed!");
    }

    private boolean isConnected() {
        return connected.get();
    }
}
