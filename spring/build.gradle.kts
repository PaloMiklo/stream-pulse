group = "com.palomiklo.streampulse"
version = "1.1.0"
description = "spring"

plugins {
    `java-library`
    kotlin("jvm") version "1.9.24"
    java
    application
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.0"
}
application {
    mainClass.set("com.palomiklo.streampulse.App")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.boot)
    testImplementation(libs.junit)
}