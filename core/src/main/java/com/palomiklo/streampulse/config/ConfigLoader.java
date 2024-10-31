package com.palomiklo.streampulse.config;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import jakarta.annotation.Nonnull;

public class ConfigLoader {
    private static volatile ConfigLoader instance;
    private final Properties properties = new Properties();
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Logger logger = LoggerFactory.getLogger(ConfigLoader.class);
    private Map<String, String> config;

    private ConfigLoader() {
        loadUserProperties();
    }

    @Nonnull
    public static ConfigLoader initialize() {
        if (instance == null) {
            synchronized (ConfigLoader.class) {
                if (instance == null) {
                    instance = new ConfigLoader();
                }
            }
        }
        return instance;
    }

    private void loadUserProperties() {
        rwLock.writeLock().lock();
        try {
            logger.debug("Loading properties...");
            final String userPropertiesPath = "application.yml";
            final Yaml yaml = new Yaml();

            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(userPropertiesPath);
            config = yaml.load(inputStream);
            config.forEach(properties::setProperty);
            logger.debug(getJdbcUrl());
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    public final String getJdbcUrl() {
        return properties.getProperty(ConfigKey.JDBC_URL.getKey());
    }

    public final String getUsername() {
        return properties.getProperty(ConfigKey.JDBC_USERNAME.getKey());
    }

    public final String getPassword() {
        return properties.getProperty(ConfigKey.JDBC_PASSWORD.getKey());
    }

    public final String getDriverClassName() {
        return properties.getProperty(ConfigKey.JDBC_DRIVER_CLASS_NAME.getKey());
    }
}
