#!/bin/bash

if [ -f ./paths.env ]; then source ./paths.env
else
  echo "⚠️  Error: paths.env file not found. Exiting..."
  exit 1
fi

if [ -f ./scripts.env ]; then source ./scripts.env
else
  echo "⚠️  Error: scripts.env file not found. Exiting..."
  exit 1
fi

cd "$STREAM_PULSE_PATH" || exit
echo "⚠️  Running install-local.sh in library..."
if [[ "$1" =~ ^(tests|test|t)$ ]]; then ./install-local.sh tests
else ./install-local.sh
fi

cd "$CONSUMER_API_PATH" || exit

# Corrected the syntax for the if condition
if DB_VALIDATION; then 
  echo "⚠️  Database is already running in consumer! Skipping..."
else 
  echo "⚠️  Starting database for the consumer..."
  DB_EXE
fi

echo "⚠️  Running consumer..."
./run.sh
