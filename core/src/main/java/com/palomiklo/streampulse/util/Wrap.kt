package com.palomiklo.streampulse.util

import com.sun.org.slf4j.internal.Logger
import com.sun.org.slf4j.internal.LoggerFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.function.Supplier

object Wrap {
    private val log: Logger = LoggerFactory.getLogger(Wrap::class.java)

    fun wrap(runnable: CheckedRunnable, message: String?) {
        try {
            runnable.run()
        } catch (e: Exception) {
            log.error(message, e)
        }
    }

    fun wrap(runnable: CheckedRunnable, message: String?, fallback: Runnable) {
        try {
            runnable.run()
        } catch (e: Exception) {
            log.error(message, e)
            fallback.run()
        }
    }

    fun <T> wrap(supplier: CheckedSupplier<T>, message: String?, fallback: Runnable): T? {
        try {
            return supplier.get()
        } catch (e: Exception) {
            log.error(message, e)
            fallback.run()
            return null
        }
    }

    fun <T> wrap(supplier: CheckedSupplier<T>, message: String?): T? {
        try {
            return supplier.get()
        } catch (e: Exception) {
            log.error(message, e)
            return null
        }
    }

    fun <T> wrap(supplier: CheckedSupplier<T>, message: String?, fallback: Supplier<T>): T {
        try {
            return supplier.get()
        } catch (e: Exception) {
            log.error(message, e)
            return fallback.get()
        }
    }

    fun interface CheckedSupplier<T> {
        fun get(): T
    }
}
