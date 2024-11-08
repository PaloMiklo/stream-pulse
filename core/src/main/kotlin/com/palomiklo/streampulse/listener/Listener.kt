package com.palomiklo.streampulse.listener

import com.palomiklo.streampulse.connection.ConnectionHolder
import jakarta.servlet.AsyncContext
import jakarta.servlet.AsyncEvent
import jakarta.servlet.AsyncListener
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Listener {
    private val log: Logger = LoggerFactory.getLogger(Listener::class.java)

    fun addListeners(actx: AsyncContext, chldr: ConnectionHolder) {
        actx.addListener(object : AsyncListener {
            override fun onComplete(event: AsyncEvent) {
                log.debug("Async process completed for connection ID: {}", chldr.connection.id)
                if (chldr.connection.isConnected()) {
                    chldr.connection.closeConnection()
                }
            }

            override fun onTimeout(event: AsyncEvent) {
                log.debug("Async process timed out for connection ID: {}", chldr.connection.id)
                if (chldr.connection.isConnected()) {
                    chldr.connection.closeConnection()
                }
            }

            override fun onError(event: AsyncEvent) {
                log.error(
                    "Error occurred for connection ID: {}: {}",
                    chldr.connection.id,
                    event.throwable.getLocalizedMessage()
                )
                actx.complete()
                if (chldr.connection.isConnected()) {
                    chldr.connection.closeConnection()
                }
            }

            override fun onStartAsync(event: AsyncEvent) {
                log.debug("Async process started for connection ID: {}", chldr.connection.id)
            }
        })
    }
}
