plugins {
    `java-library`
    kotlin("jvm") version "1.9.24"
}

group = "com.palomiklo.streampulse"
version = "1.1.0"
description = "spring"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("junit:junit:4.13.2")
}