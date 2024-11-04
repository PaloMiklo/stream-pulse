# StreamPulse 📈

StreamPulse is a multi-module configurable Java-based library that enables real-time updates for your application with ease. Designed for modularity, it provides a core module that delivers essential functionality for managing long-lived server-client connections, ideal for applications needing live updates, notifications, or continuous data streams. This lightweight, framework-agnostic library simplifies the setup and management of these connections, allowing for easy integration with any Java-based application.

# Key Features
- **Seamless Connections**: Easily initialize and manage long-lived connections for real-time data streaming.
- **Heartbeat Mechanism**: Ensures stable connections with configurable heartbeats, automatically managing reconnections when needed.
- **Flexible Configuration**: Customize ping intervals, connection timeouts, and more for optimal performance.
- **Error Handling & Reconnect Logic**: Includes built-in mechanisms to handle connection errors and automated reconnections.
- **Lightweight & Fast**: Minimal overhead, designed for efficient and responsive real-time applications.
- **Modular Architecture**: Built as a multi-module project for flexibility and future extensibility.
     
# Getting Started
Add StreamPulse to your project, configure your connection settings, and let it handle the rest. StreamPulse is built to work smoothly with Java-based web servers like Tomcat and others that support HTTP streaming.

# Requirements
- Java 11+   
- Compatible with Java-based web servers (Tomcat, Jetty, etc.)   
    
# Integration
The project is hosted at [jitpack](https://jitpack.io/#PaloMiklo/stream-pulse).       
### Maven    
```xml
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>

  	<dependency>
	    <groupId>com.github.PaloMiklo</groupId>
	    <artifactId>stream-pulse</artifactId>
	    <version>1.0.0</version>
	</dependency>
```    
### Gradle
```java
	dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}
```

```java
	dependencies {
	        implementation 'com.github.PaloMiklo:stream-pulse:1.0.0'
	}
```
    
# Configure Database Connection (experimental)

In your application.yml file, add the necessary configuration properties to connect to your database.

```yaml
streampulse:
  url: jdbc:postgresql://localhost:5432/db
  username: postgres
  password: postgres
  driver-class-name: org.postgresql.Driver
```
## Example Usage
java
```java
ConnectionHolder connection = StreamPulse.builder().build();
connection.sendEvent("Welcome to StreamPulse!");
```

# DEVELOPER
## Configuration Files Overview

- **`paths.env`**: Defines key directory paths for the project, including:
  - `STREAM_PULSE_PATH`: Path to the StreamPulse library.
  - `CONSUMER_API_PATH`: Path to the API consumer.
  - `CLIENT_PATH`: Path to the client application.

- **`scripts.env`**: Contains reusable functions for managing common tasks:
  - `CLIENT_EXE`: Starts the client application.
  - `DB_EXE`: Brings up the database.
  - `DB_VALIDATION`: Checks if the database is running.     

These files are not part of the core library but are used in scripts to streamline project setup and execution.     
## IDE setup
### VS Code
`settings.json`
```json
{
    "editor.wordWrap": "wordWrapColumn",
    "editor.wordWrapColumn": 150,
    "editor.formatOnSave": true,
    "java.project.sourcePaths": [
        "src/main/java",
        "src/test/java"
    ],
    "java.configuration.updateBuildConfiguration": "automatic",
    "java.compile.nullAnalysis.mode": "automatic",
    "java.dependency.packagePresentation": "hierarchical",
    "java.jdt.ls.vmargs": "--enable-preview",
    "jdk.runConfig.vmOptions": "--enable-preview",
    "java.configuration.runtimes": [
        {
            "name": "JavaSE-21",
            "path": ".../jdk-21.0.1",
            "default": true
        },
    ],
    "jdk.runConfig.arguments": "--enable-preview"
}
```
