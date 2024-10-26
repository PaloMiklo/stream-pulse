package com.palomiklo.streampulse.config;

public enum ConfigKey {
    JDBC_URL("streampulse.url"),
    JDBC_USERNAME("streampulse.username"),
    JDBC_PASSWORD("streampulse.password"),
    JDBC_DRIVER_CLASS_NAME("streampulse.driverClassName");

    private final String key;

    ConfigKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return key;
    }
}
