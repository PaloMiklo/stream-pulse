package com.palomiklo.streampulse.connection

import com.palomiklo.streampulse.blueprint.Event
import com.palomiklo.streampulse.blueprint.Event.Companion.serialize
import com.palomiklo.streampulse.blueprint.IConnection
import com.palomiklo.streampulse.blueprint.IConnectionConfig
import com.palomiklo.streampulse.connection.ConnectionHolder.Companion.create
import com.palomiklo.streampulse.context.AsynchronousContext.Companion.startAsyncContext
import com.palomiklo.streampulse.header.Header.setHeaders
import com.palomiklo.streampulse.thread.CustomThreadFactory
import com.palomiklo.streampulse.util.Wrap.wrap
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import java.io.PrintWriter
import java.util.*
import java.util.UUID.randomUUID
import java.util.concurrent.Executors.newSingleThreadScheduledExecutor
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit.SECONDS
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

class Connection(private val res: HttpServletResponse, private val conf: IConnectionConfig = DefaultConnectionConfig()) : IConnection {
    private val log: Logger = getLogger(Connection::class.java)
    private val connected: AtomicBoolean = AtomicBoolean(true)
    private val writeLock: Lock = ReentrantLock()
    private val exec: ScheduledExecutorService = newSingleThreadScheduledExecutor(CustomThreadFactory.streamPulseThreadFactory)
    private var writer: PrintWriter? = null
    private var stopHeartbeatTask: ScheduledFuture<*>? = null
    val id: UUID = randomUUID()

    init {
        wrap(::initializeConnection, "Failed to initialize connection for ID: $id")
    }

    private fun initializeConnection() {
        setHeaders(res).also { writer = res.writer }.also { log.debug("Connection established for ID: {}!", id) }.also { startHeartbeat() }
    }

    private fun startHeartbeat() {
        log.debug("Starting heartbeat for connection ID: {}...", id)

        exec.scheduleAtFixedRate(
            { wrap(::heartbeat, "Error during heartbeat for connection ID: $id") },
            conf.initialDelay.toLong(),
            conf.pingInterval.toLong(),
            SECONDS
        )

        stopHeartbeatTask = exec.schedule({
            log.debug(
                "{} minutes has passed. Stopping heartbeat for connection ID: {}...",
                conf.connectionTimeout / 60,
                id
            )
            sendEvent(serialize(Event(id, conf.reconnectEvent())).getOrThrow())
            closeConnection()
        }, conf.connectionTimeout.toLong(), SECONDS)
    }


    private fun heartbeat() {
        if (isConnected()) {
            val event = serialize(Event(id, conf.ping())).getOrThrow()
            sendEvent(event)
        }
    }


    override fun sendEvent(event: String) {
        writeLock.lock()
        try {
            writer?.takeIf { isConnected() }?.apply {
                write(event)
                flush()
                log.debug("EVENT: {}", event)
                if (checkError()) log.debug("Writer detected a connection error for event: {}!", event)
            } ?: log.error("Error occurred for connection ID: {}!", id)
        } finally {
            writeLock.unlock()
        }
    }

    override fun closeConnection() {
        writeLock.lock()
        try {
            if (connected.compareAndSet(true, false)) {
                stopHeartbeatTask?.cancel(true)
                    .also { exec.shutdownNow() }.also { writer?.close() }.also { log.debug("Connection with ID: {} closed!", id) }
            }
        } finally {
            writeLock.unlock()
        }
    }

    override fun isConnected(): Boolean = connected.get()

    companion object {
        fun createConnection(req: HttpServletRequest, res: HttpServletResponse): ConnectionHolder =
            create(Connection(res), startAsyncContext(req, res))

        fun createConnection(
            conf: IConnectionConfig, req: HttpServletRequest, res: HttpServletResponse
        ): ConnectionHolder = create(Connection(res, conf), startAsyncContext(req, res))
    }
}
