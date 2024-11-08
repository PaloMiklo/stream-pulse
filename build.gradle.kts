plugins {
    `java-library`
    kotlin("jvm") version "1.9.24"
}

group = "com.palomiklo.streampulse"
version = "1.1.0"
description = "core"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}
