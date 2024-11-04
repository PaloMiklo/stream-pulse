#!/bin/bash

MODULE="spring"
VERSION="1.1.0"
MAIN_CLASS="com.palomiklo.streampulse.App"

cd "$(dirname "$0")"

echo "⚠️  Compiling the spring module..."
./mvnw clean compile 
if [ $? -ne 0 ]; then
    echo "Compilation failed. Exiting."
    exit 1
fi

echo "⚠️  Packaging the spring module..."
./mvnw package
if [ $? -ne 0 ]; then
    echo "Packaging failed. Exiting."
    exit 1
fi