#!/bin/bash

MODULE="spring"
VERSION="1.0-SNAPSHOT"
MAIN_CLASS="com.palomiklo.streampulse.App"
PROFILES=""

cd "$(dirname "$0")"

echo "⚠️  Compiling the spring module..."
./mvnw clean compile 
if [ $? -ne 0 ]; then
    echo "Compilation failed. Exiting."
    exit 1
fi

echo "⚠️  Running the spring module..."
./mvnw spring-boot:run -Dspring-boot.run.profiles=${PROFILES}
