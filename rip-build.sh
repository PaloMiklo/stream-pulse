#!/bin/bash

CORE="core"
SPRING="spring"

CT="./${CORE}/build"
ST="./${SPRING}/build"

if [[ "$1" =~ ^(core|c)$ ]]; then rm -rf $CT
elif [[ "$1" =~ ^(spring|s)$ ]]; then rm -rf $ST
else rm -rf $CT && rm -rf $ST
fi
