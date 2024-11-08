#!/bin/bash

echo "⚠️  Installing all modules into local repository..."

if [[ "$1" =~ ^(tests|test|t)$ ]]; then ./gradlew clean install -P local-install
else ./gradlew clean install -DskipTests -P local-install
fi
