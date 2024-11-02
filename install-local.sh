#!/bin/bash

echo "⚠️  Installing all modules into local repository..."

if [[ "$1" =~ ^(tests|test|t)$ ]]; then ./mvnw clean install -P local-install
else ./mvnw clean install -DskipTests -P local-install
fi
