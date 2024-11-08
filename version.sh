#!/bin/bash

if [ -z "$1" ]; then
  echo "Usage: $0 <new_version>"
  exit 1
fi

NEW_VERSION=$1

FILES=$(find . -type f \( -name "pom.xml" -o -name "pack.sh" -o -name "run.sh" -o -name "build.gradle.kts" \))

for FILE in $FILES; do
  if [[ $FILE == *.xml ]]; then
    sed -i "/<groupId>com.palomiklo<\/groupId>/{n;n;s|<version>.*</version>|<version>$NEW_VERSION</version>|}" "$FILE"
    echo "Updated version in $FILE"
  elif [[ $FILE == *.sh ]]; then
    sed -i "s|VERSION=\"[^\"]*\"|VERSION=\"$NEW_VERSION\"|g" "$FILE"
    echo "Updated version in $FILE"
  elif [[ $FILE == *build.gradle.kts ]]; then
    sed -i "s|version = \".*\"|version = \"$NEW_VERSION\"|g" "$FILE"
    echo "Updated version in $FILE"
  fi
done

echo "😈  Version updated to $NEW_VERSION in all relevant files."
