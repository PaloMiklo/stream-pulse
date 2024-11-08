plugins {
    `java-library`
    kotlin("jvm") version "1.9.24"
}

group = "com.palomiklo.streampulse"
version = "1.1.0"
description = "core"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    api("ch.qos.logback:logback-core:1.4.14")
    api("ch.qos.logback:logback-classic:1.4.12")
    api("org.yaml:snakeyaml:2.3")
    compileOnly("jakarta.servlet:jakarta.servlet-api:5.0.0")
    api("jakarta.annotation:jakarta.annotation-api:2.1.1")
}
