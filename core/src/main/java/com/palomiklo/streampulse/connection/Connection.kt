package com.palomiklo.streampulse.connection

import com.palomiklo.streampulse.blueprint.Event
import com.palomiklo.streampulse.blueprint.IConnection
import com.palomiklo.streampulse.blueprint.IConnectionConfig
import com.palomiklo.streampulse.context.AsynchronousContext
import com.palomiklo.streampulse.header.Header
import com.palomiklo.streampulse.thread.CustomThreadFactory
import com.palomiklo.streampulse.util.Wrap
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import java.io.IOException
import java.io.PrintWriter
import java.util.*
import java.util.UUID.randomUUID
import java.util.concurrent.Executors.newSingleThreadScheduledExecutor
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

class Connection : IConnection {
    private val log: org.slf4j.Logger = LoggerFactory.getLogger(Connection::class.java)
    private val connected: AtomicBoolean = AtomicBoolean(true)
    private val writeLock: Lock = ReentrantLock()
    private val conf: IConnectionConfig
    private val res: HttpServletResponse
    private val exec: ScheduledExecutorService = newSingleThreadScheduledExecutor(CustomThreadFactory.streamPulseThreadFactory)
    private var writer: PrintWriter? = null
    private var stopHeartbeatTask: ScheduledFuture<*>? = null
    val id: UUID

    private constructor(conf: IConnectionConfig, res: HttpServletResponse) {
        this.id = randomUUID()
        this.conf = conf
        this.res = res
        Wrap.wrap({ initializeConnection() }, "Failed to initialize connection for ID: {}: $id")
    }

    private constructor(res: HttpServletResponse) {
        this.id = randomUUID()
        this.conf = DefaultConnectionConfig()
        this.res = res
        Wrap.wrap({ initializeConnection() }, "Failed to initialize connection for ID: {}: $id")
    }

    @Throws(IOException::class)
    private fun initializeConnection() {
        Header.setHeaders(res)

        this.writer = res.writer
        log.debug("Connection established for ID: {}!", id)

        startHeartbeat()
    }

    private fun startHeartbeat() {
        log.debug("Starting heartbeat for connection ID: {}...", id)

        exec.scheduleAtFixedRate(
            { Wrap.wrap({ heartbeat() }, "Error during heartbeat for connection ID: $id") },
            conf.initialDelay.toLong(), conf.pingInterval.toLong(), TimeUnit.SECONDS
        )

        stopHeartbeatTask = exec.schedule({
            log.debug(
                "{} minutes has passed. Stopping heartbeat for connection ID: {}...",
                conf.connectionTimeout / 60,
                id
            )
            sendEvent(
                Event.serialize(
                    Event(
                        id,
                        conf.reconnectEvent.get()
                    )
                )
            )
            closeConnection()
        }, conf.connectionTimeout.toLong(), TimeUnit.SECONDS)
    }

    private fun heartbeat() {
        if (isConnected()) {
            val event: String = Event.serialize(
                Event(
                    id,
                    conf.ping.get()
                )
            )
            sendEvent(event)
        }
    }

    override fun sendEvent(event: String) {
        writeLock.lock()
        try {
            if (isConnected() && writer != null) {
                writer!!.write(event)
                writer!!.flush()
                log.debug("EVENT: {}", event)

                if (writer!!.checkError()) {
                    log.debug("Writer detected a connection error for event: {}!", event)
                }
            } else {
                log.error("Error occured for connection ID: {}!", id)
            }
        } finally {
            writeLock.unlock()
        }
    }

    override fun closeConnection() {
        writeLock.lock()
        try {
            if (connected.compareAndSet(true, false)) {
                if (stopHeartbeatTask != null) {
                    stopHeartbeatTask!!.cancel(true)
                }
                exec.shutdownNow()
                if (writer != null) {
                    writer!!.close()
                }
                log.debug("Connection with ID: {} closed!", id)
            }
        } finally {
            writeLock.unlock()
        }
    }

    override fun isConnected(): Boolean {
        return connected.get()
    }

    companion object {
        fun createConnection(req: HttpServletRequest, res: HttpServletResponse): ConnectionHolder {
            val chldr: ConnectionHolder = ConnectionHolder.Companion.create(
                Connection(res),
                AsynchronousContext.Companion.startAsyncContext(req, res)
            )
            return chldr
        }

        fun createConnection(
            conf: IConnectionConfig,
            req: HttpServletRequest,
            res: HttpServletResponse
        ): ConnectionHolder {
            val chldr: ConnectionHolder = ConnectionHolder.Companion.create(
                Connection(conf, res),
                AsynchronousContext.Companion.startAsyncContext(req, res)
            )
            return chldr
        }
    }
}
