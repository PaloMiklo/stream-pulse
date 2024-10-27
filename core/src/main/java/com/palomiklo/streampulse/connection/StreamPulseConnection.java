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

import jakarta.servlet.http.HttpServletResponse;

public class StreamPulseConnection {
    private final Logger logger = LoggerFactory.getLogger(StreamPulseConnection.class);
    private final AtomicBoolean connected = new AtomicBoolean(true);
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private final Lock writeLock = new ReentrantLock();
    private final IStreamPulseConnectionConfig config;
    private PrintWriter writer;
    private final HttpServletResponse response;

    private StreamPulseConnection(IStreamPulseConnectionConfig configuration, HttpServletResponse response) {
        this.config = configuration;
        this.response = response;
        try {
            initializeConnection();
        } catch (IOException e) {
            logger.error("Failed to initialize connection: ", e);
        }
    }

    private StreamPulseConnection(HttpServletResponse response) {
        this.config = new DefaultStreamPulseConnectionConfig();
        this.response = response;
        try {
            initializeConnection();
        } catch (IOException e) {
            logger.error("Failed to initialize connection: ", e);
        }
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

    private void sendEvent(String event) throws IOException {
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

    private ScheduledExecutorService startHeartbeat() {
        logger.debug("Starting heartbeat...");

        executor.scheduleAtFixedRate(() -> {
            try {
                if (isConnected()) {
                    logger.debug("Sending heartbeat...");
                    sendEvent("ping");
                } else {
                    closeConnection();
                }
            } catch (IOException e) {
                logger.error("Error during heartbeat: ", e);
                closeConnection();
            }
        }, config.getInitialDelay(), config.getPingInterval(), SECONDS);

        executor.schedule(() -> {
            logger.debug("%s minutes has passed. Stopping heartbeat...", config.getConnectionTimeout() / 60);
            closeConnection();
        }, config.getConnectionTimeout(), SECONDS);

        return executor;
    }

    private void closeConnection() {
        connected.set(false);
        if (writer != null) {
            writer.close();
        }
        executor.shutdown();
        logger.debug("Connection closed!");
    }

    private boolean isConnected() {
        return connected.get();
    }
}
