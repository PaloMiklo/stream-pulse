#!/bin/bash

if [ -f ./paths.env ]; then source ./paths.env
else
  echo "⚠️  Error: paths.env file not found. Exiting..."
  exit 1
fi

cd "$STREAM_PULSE_PATH" || exit
echo "⚠️  Running install-local.sh in library..."
if [[ "$1" =~ ^(tests|test|t)$ ]]; then ./install-local.sh tests
else ./install-local.sh
fi

cd "$CONSUMER_API_PATH" || exit

if docker-compose ps | grep -q "Up"; then echo "⚠️  docker-compose is already running in consumer! Skipping..."
else 
  echo "⚠️  Starting docker-compose up in for the consumer..."
  docker-compose up -d
fi

echo "⚠️  Running consumer..."
./run.sh
