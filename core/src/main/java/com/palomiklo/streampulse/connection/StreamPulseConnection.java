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

import com.palomiklo.streampulse.blueprint.IStreamPulseConnectionConfig;
import static com.palomiklo.streampulse.connection.StreamPulseThreadFactory.streamPulseThreadFactory;
import static com.palomiklo.streampulse.util.Wrap.wrap;

import jakarta.servlet.http.HttpServletResponse;

public class StreamPulseConnection {
    private final Logger logger = LoggerFactory.getLogger(StreamPulseConnection.class);
    private final AtomicBoolean connected = new AtomicBoolean(true);
    private final ScheduledExecutorService executor = Executors
            .newSingleThreadScheduledExecutor(streamPulseThreadFactory);
    private final Lock writeLock = new ReentrantLock();
    private final IStreamPulseConnectionConfig config;
    private final HttpServletResponse response;
    private PrintWriter writer;

    private StreamPulseConnection(IStreamPulseConnectionConfig configuration, HttpServletResponse response) {
        this.config = configuration;
        this.response = response;
        wrap(() -> initializeConnection(), "Failed to initialize connection: ");
    }

    private StreamPulseConnection(HttpServletResponse response) {
        this.config = new DefaultStreamPulseConnectionConfig();
        this.response = response;
        wrap(() -> initializeConnection(), "Failed to initialize connection: ");
    }

    public static StreamPulseConnection createConnection(HttpServletResponse response) {
        return new StreamPulseConnection(response);
    }

    public static StreamPulseConnection createConnection(
            IStreamPulseConnectionConfig configuration, HttpServletResponse response) {
        return new StreamPulseConnection(configuration, response);
    }

    private void initializeConnection() throws IOException {
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Connection", "keep-alive");
        response.setHeader("Transfer-Encoding", "chunked");

        this.writer = response.getWriter();
        logger.debug("Connection established!");

        startHeartbeat();
        while (isConnected()) {
        }
    }

    private void sendEvent(String event) {
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
