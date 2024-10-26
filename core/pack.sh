#!/bin/bash

MODULE="core"
VERSION="1.0-SNAPSHOT"
MAIN_CLASS="com.palomiklo.streampulse.App"

cd "$(dirname "$0")"

echo "⚠️  Compiling the core module..."
./mvnw clean compile 
if [ $? -ne 0 ]; then
    echo "Compilation failed. Exiting."
    exit 1
fi

echo "⚠️  Packaging the core module..."
./mvnw package
if [ $? -ne 0 ]; then
    echo "Packaging failed. Exiting."
    exit 1
fi