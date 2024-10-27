package com.palomiklo.streampulse.config;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

public class ConfigLoader {
    private static ConfigLoader instance;
    private final Properties properties = new Properties();
    private final Logger logger = LoggerFactory.getLogger(ConfigLoader.class);

    private ConfigLoader() {
        loadUserProperties();
    }

    public static final ConfigLoader initialize() {
        if (instance == null) {
            instance = new ConfigLoader();
        }
        return instance;
    }

    private void loadUserProperties() {
        logger.debug("Loading properties...");
        final String userPropertiesPath = "application.yml";
        final Yaml yaml = new Yaml();

        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(userPropertiesPath);
        Map<String, String> config = yaml.load(inputStream);

        config.forEach((key, value) -> properties.setProperty(key, value));
        logger.debug(getJdbcUrl());
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
