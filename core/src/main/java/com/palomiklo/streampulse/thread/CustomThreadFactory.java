package com.palomiklo.streampulse.thread;

import java.util.concurrent.ThreadFactory;

import jakarta.annotation.Nullable;

public class CustomThreadFactory {
    @Nullable
    public static final ThreadFactory streamPulseThreadFactory = new ThreadFactory() {
        private final String namePattern = "stream-pulse-thread-%d";
        private int count = 0;

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = Thread.ofVirtual().unstarted(r);
            thread.setName(String.format(namePattern, count++));
            return thread;
        }
    };
}