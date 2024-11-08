package com.palomiklo.streampulse.thread

import java.util.concurrent.ThreadFactory

object CustomThreadFactory {
    val streamPulseThreadFactory: ThreadFactory = object : ThreadFactory {
        private val PREFIX = "stream-pulse-thread"
        private var count = 0
        override fun newThread(r: Runnable): Thread = Thread.ofVirtual().unstarted(r).apply { setName("$PREFIX-$count++") }
    }
}