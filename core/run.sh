#!/bin/bash

MODULE="core"
VERSION="1.0-SNAPSHOT"
MAIN_CLASS="com.palomiklo.streampulse.App"

cd "$(dirname "$0")"

./pack.sh

echo "⚠️  Running the core module..."

# -d to include all JAR dependencies, which is useful for development purposes when running this application in isolation
if [[ "$1" =~ ^(dependencies|dep|d)$ ]]; then ./mvnw exec:java -Dexec.mainClass="${MAIN_CLASS}"
else java -cp target/${MODULE}-${VERSION}.jar ${MAIN_CLASS}
fi
