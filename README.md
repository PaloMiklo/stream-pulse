# StreamPulse



# Integration
The project is hosted at `https://jitpack.io/#PaloMiklo/stream-pulse`       
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

# Configure Database Connection

In your application.yml file, add the necessary configuration properties to connect to your database.

```yaml
streampulse:
  url: jdbc:postgresql://localhost:5432/db
  username: postgres
  password: postgres
  driver-class-name: org.postgresql.Driver
```