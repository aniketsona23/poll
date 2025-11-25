#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
TMPDIR="$(mktemp -d "${TMPDIR:-/tmp}/run-tests.XXXX")"

cleanup() {
  rc=$?
  # Remove temporary directory (keep if you want to inspect on failure)
  if [ -d "$TMPDIR" ]; then
    rm -rf "$TMPDIR"
  fi
  exit $rc
}
trap cleanup EXIT

echo "Project root: $ROOT"
echo "Temp dir: $TMPDIR"

# 1) Ensure we have Java
if command -v java >/dev/null 2>&1; then
  echo "Found system java: $(java -version 2>&1 | head -n1)"
  JAVA_CMD="java"
  MVN_CMD="$(command -v mvn || true)"
else
  echo "No system java found — will download a Temurin JDK (temporary)."
  # Adjust platform if needed: linux|windows|mac
  PLATFORM="linux"
  ARCH="x64"
  # Adoptium API pattern (redirecting download)
  API_URL="https://api.adoptium.net/v3/binary/latest/17/ga/${PLATFORM}/${ARCH}/jdk/hotspot/normal/eclipse"
  JDK_ARCHIVE="$TMPDIR/jdk.tar.gz"
  echo "Downloading JDK from Adoptium..."
  curl -L --fail -o "$JDK_ARCHIVE" "$API_URL"
  mkdir -p "$TMPDIR/jdk"
  tar -xzf "$JDK_ARCHIVE" -C "$TMPDIR/jdk" --strip-components=1
  export JAVA_HOME="$TMPDIR/jdk"
  export PATH="$JAVA_HOME/bin:$PATH"
  JAVA_CMD="$JAVA_HOME/bin/java"
  echo "Temurin JDK ready: $($JAVA_CMD -version 2>&1 | head -n1)"
  MVN_CMD=""
fi

# 2) Ensure we have mvn (Maven). Prefer project mvnw if present.
if [ -x "$ROOT/mvnw" ]; then
  echo "Using project mvnw wrapper"
  MVN_CMD="$ROOT/mvnw"
elif command -v mvn >/dev/null 2>&1; then
  MVN_CMD="$(command -v mvn)"
  echo "Found system mvn: $($MVN_CMD -v 2>&1 | head -n1)"
else
  echo "No mvn found — will download Apache Maven (temporary)."
  MAVEN_VERSION="3.9.6"
  MAVEN_URL="https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/${MAVEN_VERSION}/apache-maven-${MAVEN_VERSION}-bin.tar.gz"
  MAVEN_ARCHIVE="$TMPDIR/maven.tar.gz"
  curl -L --fail -o "$MAVEN_ARCHIVE" "$MAVEN_URL"
  mkdir -p "$TMPDIR/maven"
  tar -xzf "$MAVEN_ARCHIVE" -C "$TMPDIR/maven" --strip-components=1
  export MAVEN_HOME="$TMPDIR/maven"
  export PATH="$MAVEN_HOME/bin:$PATH"
  MVN_CMD="$MAVEN_HOME/bin/mvn"
  echo "Downloaded Maven $MAVEN_VERSION"
fi

# 3) Run tests (non-interactive)
echo "Running tests with: $MVN_CMD"
# run from project root
cd "$ROOT"
# Use -B batch mode and fail fast on test failure
"$MVN_CMD" -B clean test
TEST_EXIT=$?

if [ $TEST_EXIT -eq 0 ]; then
  echo "Tests passed."
else
  echo "Some tests failed (exit code $TEST_EXIT). See Maven output above."
fi

exit $TEST_EXIT
