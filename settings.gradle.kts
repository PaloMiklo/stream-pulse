rootProject.name = "streampulse"
include("core")
include("spring")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }

    versionCatalogs {
        create("libs") {
            library("slf4j", "org.slf4j:slf4j-api:1.7.32")
            library("logback-classic", "ch.qos.logback:logback-classic:1.2.10")
            library("logback-core", "ch.qos.logback:logback-core:1.4.14")
            library("snakeyaml", "org.yaml:snakeyaml:2.3")
            library("servlet-api", "jakarta.servlet:jakarta.servlet-api:5.0.0")
            library("annotation-api", "jakarta.annotation:jakarta.annotation-api:2.1.1")
            library("junit", "junit:junit:4.13.2")
            library("yaml","org.yaml:snakeyaml:2.3")
            library("servlet","jakarta.servlet:jakarta.servlet-api:5.0.0")
            library("jackson","com.fasterxml.jackson.core:jackson-databind:2.18.1")
            library("boot","org.springframework.boot:spring-boot-starter:3.3.0")
        }
    }
}
