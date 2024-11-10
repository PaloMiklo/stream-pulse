#!/bin/bash

echo "⚠️  Installing all modules into local Maven repository..."

if [[ "$1" =~ ^(tests|test|t)$ ]]; then ./gradlew clean publishToMavenLocal -P localInstall
else ./gradlew clean publishToMavenLocal -DskipTests -P localInstall
fi
