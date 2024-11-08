package com.palomiklo.streampulse.config

import com.palomiklo.streampulse.config.ConfigKey.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.yaml.snakeyaml.Yaml
import java.io.InputStream
import java.util.*
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.withLock

class ConfigLoader private constructor() {
    private val properties: Properties = Properties()
    private val rwLock: ReentrantReadWriteLock = ReentrantReadWriteLock()
    private val log: Logger = getLogger(ConfigLoader::class.java)

    init {
        log.debug("Config loader is initializing configuration...").also { loadUserProperties() }
    }

    private fun loadUserProperties() {
        rwLock.writeLock().withLock {
            log.debug("Loading properties...")
            val userPropertiesPath = "application.yml"
            val yaml = Yaml()

            val inputStream: InputStream? = this::class.java.classLoader.getResourceAsStream(userPropertiesPath)
            inputStream?.let { stream ->
                val conf: Map<String, Any>? = yaml.load(inputStream)
                conf?.forEach { (key, value) -> properties.setProperty(key, value.toString()) }
            } ?: log.warn("Could not load properties from $userPropertiesPath")
        }
    }

    val jdbcUrl: String = properties.getProperty(JDBC_URL.key) ?: ""
    val username: String = properties.getProperty(JDBC_USERNAME.key) ?: ""
    val password: String = properties.getProperty(JDBC_PASSWORD.key) ?: ""
    val driverClassName: String = properties.getProperty(JDBC_DRIVER_CLASS_NAME.key) ?: ""

    companion object {
        @Volatile
        private var instance: ConfigLoader? = null

        fun initializeConfig(): ConfigLoader {
            return instance ?: synchronized(this) { instance ?: ConfigLoader().also { instance = it } }
        }
    }
}
