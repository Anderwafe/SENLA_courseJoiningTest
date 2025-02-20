#!/bin/bash

# SRC_DIR="src/main/java"
SRC_DIR="."
# OUTPUT_DIR="out"
OUTPUT_DIR="."
MAIN_CLASS="ecosystem.EcosystemSimulator"

if [ ! -d "$OUTPUT_DIR" ]; then
    mkdir -p "$OUTPUT_DIR"
fi

echo "Compiling Java files..."
# find "$SRC_DIR" -name "*.java" -print | xargs javac -d "$OUTPUT_DIR" -sourcepath "$SRC_DIR"
find "$SRC_DIR" -name "*.java" -print | xargs javac -sourcepath "$SRC_DIR"

if [ $? -ne 0 ]; then
    echo "Compilation failed."
    exit 1
fi

echo "Running the Java program..."
java -cp "$OUTPUT_DIR" "$MAIN_CLASS"