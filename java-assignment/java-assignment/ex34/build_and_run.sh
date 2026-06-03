#!/bin/bash
# Build and run the Java modules example (ex34)
# Run from inside the ex34/ directory: bash build_and_run.sh

set -e

echo "=== Compiling com.utils ==="
mkdir -p mods/com.utils
javac -d mods/com.utils \
  com.utils/src/module-info.java \
  com.utils/src/com/utils/Greeter.java

echo "=== Compiling com.greetings ==="
mkdir -p mods/com.greetings
javac --module-path mods \
  -d mods/com.greetings \
  com.greetings/src/module-info.java \
  com.greetings/src/com/greetings/Main.java

echo "=== Running ==="
java --module-path mods -m com.greetings/com.greetings.Main
