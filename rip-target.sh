#!/bin/bash

CORE="core"
SPRING="spring"

if [[ "$1" =~ ^(core|c)$ ]]; then rm -rf ./${CORE}/target
elif [[ "$1" =~ ^(spring|s)$ ]]; then rm -ef ./${SPRING}/target
else rm -rf ./${CORE}/target && rm -rf ./${SPRING}/target
fi