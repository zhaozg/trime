#!/usr/bin/env bash

DIGEST_ALGORITHM=sha256sum
MAGIC=magic.txt
echo auto generated contents >> $MAGIC

# fetch jni relative elements
grep ndkVersion app/build.gradle.kts >> $MAGIC

elements=("buildTypes" "externalNativeBuild" "splits")
for element in ${elements[@]}; do
  awk "/$element/,/\}/" app/build.gradle.kts >> $MAGIC
done

hash=$(git submodule status)
hash=$hash$($DIGEST_ALGORITHM)
hash=$(echo $hash | $DIGEST_ALGORITHM | cut -c-64)

echo "hash=$hash" >> $GITHUB_OUTPUT
