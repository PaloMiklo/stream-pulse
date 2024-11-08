package com.palomiklo.streampulse.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.yaml.snakeyaml.Yaml
import java.io.InputStream
import java.util.*
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.withLock

class ConfigLoader private constructor() {
    private val properties: Properties = Properties()
    private val rwLock: ReentrantReadWriteLock = ReentrantReadWriteLock()
    private val log: Logger = LoggerFactory.getLogger(ConfigLoader::class.java)
    private var conf: Map<String, Any>? = null

    init {
        log.debug("Config loader is initializing configuration...")
        loadUserProperties()
    }

    private fun loadUserProperties() {
        rwLock.writeLock().withLock {
            log.debug("Loading properties...")
            val userPropertiesPath = "application.yml"
            val yaml = Yaml()

            val inputStream: InputStream? = this::class.java.classLoader.getResourceAsStream(userPropertiesPath)
            if (inputStream != null) {
                conf = yaml.load(inputStream)
                conf?.forEach { (key, value) ->
                    properties.setProperty(key, value.toString())
                }
                log.debug("JDBC URL: {}", jdbcUrl)  // Use {} to avoid String interpolation within SLF4J logging
            } else {
                log.warn("Could not load properties from $userPropertiesPath")
            }
        }
    }

    val jdbcUrl: String
        get() = properties.getProperty(ConfigKey.JDBC_URL.key) ?: ""

    val username: String
        get() = properties.getProperty(ConfigKey.JDBC_USERNAME.key) ?: ""

    val password: String
        get() = properties.getProperty(ConfigKey.JDBC_PASSWORD.key) ?: ""

    val driverClassName: String
        get() = properties.getProperty(ConfigKey.JDBC_DRIVER_CLASS_NAME.key) ?: ""

    companion object {
        @Volatile
        private var instance: ConfigLoader? = null

        fun initialize(): ConfigLoader {
            return instance ?: synchronized(this) {
                instance ?: ConfigLoader().also { instance = it }
            }
        }
    }
}
