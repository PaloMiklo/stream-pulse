#!/bin/bash

CORE="core"
SPRING="spring"

if [[ "$1" =~ ^(core|c)$ ]]; then ./${CORE}/test.sh
elif [[ "$1" =~ ^(spring|s)$ ]]; then ./${SPRING}/test.sh
else ./${CORE}/test.sh && ./${SPRING}/test.sh
fi