#!/bin/bash

CORE="core"
SPRING="spring"

MODULE="core"
VERSION="1.1.0"
MAIN_CLASS="com.palomiklo.streampulse.App"

# Change directory to the script location
cd "$(dirname "$0")"

echo "⚠️  Building the ${MODULE} module..."
./gradlew :${MODULE}:clean :${MODULE}:build
if [ $? -ne 0 ]; then
    echo "Build failed. Exiting."
    exit 1
fi

echo "⚠️  ${MODULE} module has been built and packaged successfully."

MODULE="spring"
VERSION="1.1.0"
MAIN_CLASS="com.palomiklo.streampulse.App"
cd "$(dirname "$0")"

echo "⚠️  Building the ${MODULE} module..."
./gradlew :${MODULE}:clean :${MODULE}:build
if [ $? -ne 0 ]; then
    echo "Build failed. Exiting."
    exit 1
fi

echo "⚠️  ${MODULE} module has been built and packaged successfully."
