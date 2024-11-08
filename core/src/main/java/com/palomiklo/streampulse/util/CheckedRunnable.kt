package com.palomiklo.streampulse.util

@FunctionalInterface
fun interface CheckedRunnable {
    @Throws(Exception::class)
    fun run()
}
