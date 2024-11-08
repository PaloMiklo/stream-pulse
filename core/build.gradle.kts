plugins {
    kotlin("jvm") version "1.9.24"
    `java-library`
    java
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

application {
    mainClass.set("com.palomiklo.streampulse.App")
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

dependencies {
    implementation(libs.slf4j)
    implementation(libs.logback.classic)
    implementation(libs.logback.core)
    testImplementation(libs.junit)
    implementation(libs.yaml)
    implementation(libs.servlet.api)
    implementation(libs.annotation.api)
    implementation(libs.jackson)
}
