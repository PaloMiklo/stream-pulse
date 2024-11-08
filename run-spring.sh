#!/bin/bash

MODULE="spring"
VERSION="1.1.0"
MAIN_CLASS="com.palomiklo.streampulse.App"
PROFILES=""

cd "$(dirname "$0")"

echo "⚠️  Compiling the spring module..."
./gradlew clean compileJava
if [ $? -ne 0 ]; then
    echo "Compilation failed. Exiting."
    exit 1
fi

echo "⚠️  Running the spring module..."
./gradlew :$MODULE:bootRun -Dspring-boot.run.profiles=${PROFILES} --warning-mode all

