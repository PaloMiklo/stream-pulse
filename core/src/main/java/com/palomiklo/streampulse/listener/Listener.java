package com.palomiklo.streampulse.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.palomiklo.streampulse.connection.ConnectionHolder;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.AsyncEvent;
import jakarta.servlet.AsyncListener;

public class Listener {

    private static final Logger log = LoggerFactory.getLogger(Listener.class);

    public static void addListeners(AsyncContext actx, ConnectionHolder streamPulse) {
        actx.addListener(new AsyncListener() {
            @Override
            public void onComplete(AsyncEvent event) {
                log.debug("Async process completed for connection ID: {}", streamPulse.id());
                if (streamPulse.connection().isConnected()) {
                    streamPulse.connection().closeConnection();
                }
            }

            @Override
            public void onTimeout(AsyncEvent event) {
                log.debug("Async process timed out for connection ID: {}", streamPulse.id());
                streamPulse.connection().closeConnection();
            }

            @Override
            public void onError(AsyncEvent event) {
                log.error("Error occurred for connection ID: {}: {}", streamPulse.id(), event.getThrowable().getLocalizedMessage());
                streamPulse.connection().closeConnection();
            }

            @Override
            public void onStartAsync(AsyncEvent event) {
                log.debug("Async process started for connection ID: {}", streamPulse.id());
            }
        });
    }

}
