#!/bin/bash

MODULE="core"

if [[ "$1" =~ ^(dependencies|dep|d)$ ]]; then ./${MODULE}/run.sh "$@"
else ./${MODULE}/run.sh
fi
