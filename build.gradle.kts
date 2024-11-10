group = "com.palomiklo.streampulse"
version = "1.1.0"
description = "streampulse"

plugins {
    `java-library`
    kotlin("jvm") version "1.9.24"
    java
    application
    id("maven-publish")
}

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
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
        }
    }
}