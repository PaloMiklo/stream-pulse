#!/bin/bash

cd "$(dirname "$0")"

echo "⚠️  Testing the core module..."
./mvnw test