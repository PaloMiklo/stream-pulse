#!/bin/bash

MODULE="core"
VERSION="1.1.0"
MAIN_CLASS="com.palomiklo.streampulse.App"

cd "$(dirname "$0")"

./pack.sh

echo "⚠️  Running the core module..."

# -d to include all JAR dependencies, which is useful for development purposes when running this application in isolation
if [[ "$1" =~ ^(dependencies|dep|d)$ ]]; then ./gradlew :${MODULE}:run -PmainClass=${MAIN_CLASS}
else java -cp target/${MODULE}-${VERSION}.jar ${MAIN_CLASS}
fi
