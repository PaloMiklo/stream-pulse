package com.palomiklo.streampulse.config

enum class ConfigKey(val key: String) {
    JDBC_URL("streampulse.url"),
    JDBC_USERNAME("streampulse.username"),
    JDBC_PASSWORD("streampulse.password"),
    JDBC_DRIVER_CLASS_NAME("streampulse.driverClassName");

    override fun toString(): String = key
}
