#!/bin/bash

CORE="core"
SPRING="spring"

CT="./gradlew :${CORE}:test"
ST="./gradlew :${SPRING}:test"

if [[ "$1" =~ ^(core|c)$ ]]; then $CT
elif [[ "$1" =~ ^(spring|s)$ ]]; then $ST
else $CT && $ST
fi