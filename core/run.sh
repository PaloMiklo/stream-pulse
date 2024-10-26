#!/bin/bash

MODULE="core"
VERSION="1.0-SNAPSHOT"
MAIN_CLASS="com.palomiklo.streampulse.App"

cd "$(dirname "$0")"

./pack.sh

echo "⚠️  Running the core module..."
java -cp target/${MODULE}-${VERSION}.jar ${MAIN_CLASS}
