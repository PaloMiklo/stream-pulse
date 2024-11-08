package com.palomiklo.streampulse.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Wrap {
    private val log: Logger = LoggerFactory.getLogger(Wrap::class.java)

    fun wrap(runnable: () -> Unit, message: String?) {
        try {
            runnable()
        } catch (e: Exception) {
            log.error(message, e)
        }
    }

    fun <T> wrap(supplier: () -> T, message: String?): Result<T> {
        return try {
            Result.success(supplier())
        } catch (e: Exception) {
            log.error(message, e)
            Result.failure(e)
        }
    }
}
