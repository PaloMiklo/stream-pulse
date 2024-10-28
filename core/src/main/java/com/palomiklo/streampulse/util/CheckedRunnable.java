package com.palomiklo.streampulse.util;

@FunctionalInterface
public interface CheckedRunnable {
    void run() throws Exception;
}
