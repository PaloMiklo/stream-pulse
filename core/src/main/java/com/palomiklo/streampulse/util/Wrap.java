package com.palomiklo.streampulse.util;

import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.palomiklo.streampulse.connection.Connection;

import jakarta.annotation.Nullable;

public class Wrap {

    private static final Logger log = LoggerFactory.getLogger(Connection.class);

    public static void wrap(CheckedRunnable runnable, String message) {
        try {
            runnable.run();
        } catch (Exception e) {
            log.error(message, e);
        }
    }

    public static void wrap(CheckedRunnable runnable, String message, Runnable fallback) {
        try {
            runnable.run();
        } catch (Exception e) {
            log.error(message, e);
            fallback.run();
        }
    }

    @FunctionalInterface
    public interface CheckedSupplier<T> {

        T get() throws Exception;
    }

    @Nullable
    public static <T> T wrap(CheckedSupplier<T> supplier, String message, Runnable fallback) {
        try {
            return supplier.get();
        } catch (Exception e) {
            log.error(message, e);
            fallback.run();
            return null;
        }
    }

    @Nullable
    public static <T> T wrap(CheckedSupplier<T> supplier, String message) {
        try {
            return supplier.get();
        } catch (Exception e) {
            log.error(message, e);
            return null;
        }
    }

    public static <T> T wrap(CheckedSupplier<T> supplier, String message, Supplier<T> fallback) {
        try {
            return supplier.get();
        } catch (Exception e) {
            log.error(message, e);
            return fallback.get();
        }
    }
}
