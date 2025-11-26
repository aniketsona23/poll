#!/bin/bash
# Build and run the Codebase Analyzer

set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
TMPDIR="${TMPDIR:-/tmp}/analyzer-build"

# Ensure we have mvn (Maven). Prefer project mvnw if present.
if [ -x "$ROOT/mvnw" ]; then
  echo "Using project mvnw wrapper"
  MVN_CMD="$ROOT/mvnw"
elif command -v mvn >/dev/null 2>&1; then
  MVN_CMD="$(command -v mvn)"
  echo "Found system mvn: $($MVN_CMD -v 2>&1 | head -n1)"
else
  echo "No mvn found — will download Apache Maven (temporary)."
  mkdir -p "$TMPDIR"
  MAVEN_VERSION="3.9.6"
  MAVEN_URL="https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/${MAVEN_VERSION}/apache-maven-${MAVEN_VERSION}-bin.tar.gz"
  MAVEN_ARCHIVE="$TMPDIR/maven.tar.gz"
  
  if [ ! -d "$TMPDIR/maven" ]; then
      echo "Downloading Maven..."
      curl -L --fail -o "$MAVEN_ARCHIVE" "$MAVEN_URL"
      mkdir -p "$TMPDIR/maven"
      tar -xzf "$MAVEN_ARCHIVE" -C "$TMPDIR/maven" --strip-components=1
  fi
  
  export MAVEN_HOME="$TMPDIR/maven"
  export PATH="$MAVEN_HOME/bin:$PATH"
  MVN_CMD="$MAVEN_HOME/bin/mvn"
  echo "Downloaded Maven $MAVEN_VERSION"
fi

# Build using Maven
echo "Building project..."
"$MVN_CMD" clean package -DskipTests

# Check if build was successful
if [ $? -ne 0 ]; then
    echo "Build failed."
    exit 1
fi

# Run the analyzer
echo "Running Analyzer..."
java -jar target/containers-1.0-SNAPSHOT.jar "$@"
