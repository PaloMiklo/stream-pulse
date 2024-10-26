package com.palomiklo.streampulse.config;

import java.io.InputStream;
import java.util.Map;
import static java.util.Objects.requireNonNull;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

public class ConfigLoader {
    private final Properties properties = new Properties();
    Logger logger = LoggerFactory.getLogger(ConfigLoader.class);

    public ConfigLoader() {
        loadUserProperties();
        logger.debug(getJdbcUrl());
    }

    private void loadUserProperties() {
        final String userPropertiesPath = "application.yml";
        final Yaml yaml = new Yaml();

        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(userPropertiesPath);
        Map<String, String> config = yaml.load(inputStream);

        config.forEach((key, value) -> properties.setProperty(key, value));
    }

    public final String getJdbcUrl() {
        return requireNonNull(properties.getProperty(ConfigKey.JDBC_URL.getKey()), ConfigKey.JDBC_URL.getKey() + " can not be null!");
    }

    public final String getUsername() {
        return requireNonNull(properties.getProperty(ConfigKey.JDBC_USERNAME.getKey()), ConfigKey.JDBC_USERNAME.getKey() + " can not be null!");
    }

    public final String getPassword() {
        return requireNonNull(properties.getProperty(ConfigKey.JDBC_PASSWORD.getKey()), ConfigKey.JDBC_PASSWORD.getKey() + " can not be null!");
    }

    public final String getDriverClassName() {
        return requireNonNull(properties.getProperty(ConfigKey.JDBC_DRIVER_CLASS_NAME.getKey()), ConfigKey.JDBC_DRIVER_CLASS_NAME.getKey() + " can not be null!");
    }
}
