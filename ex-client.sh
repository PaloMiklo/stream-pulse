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

echo "⚠️  Running client..."
cd "$CLIENT_PATH" && CLIENT_EXE; exec bash