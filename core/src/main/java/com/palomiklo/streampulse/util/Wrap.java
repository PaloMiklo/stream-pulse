package com.palomiklo.streampulse.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.palomiklo.streampulse.connection.StreamPulseConnection;

public class Wrap {
    private static final Logger logger = LoggerFactory.getLogger(StreamPulseConnection.class);

    public static void wrap(CheckedRunnable runnable, String message) {
        try {
            runnable.run();
        } catch (Exception e) {
            logger.error(message, e);
        }
    }

    public static void wrap(CheckedRunnable runnable, String message, Runnable fallback) {
        try {
            runnable.run();
        } catch (Exception e) {
            logger.error(message, e);
        }
    }

}
