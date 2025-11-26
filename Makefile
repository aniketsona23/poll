# ============================================
#   PURE JAVA MAKEFILE (WITH JUNIT + LIBS)
# ============================================
SRC_DIR := src
BIN_DIR := build
MAIN_CLASS := com.analyzer.cli.Main
JAR_FILE := codebase-analyzer.jar

LIB_DIR := lib

# ---- External JARs ----
# Adjust versions/file names to match what you actually downloaded
JUNIT_JAR      := $(LIB_DIR)/junit-platform-console-standalone-1.10.2.jar
JAVAPARSER_JAR := $(LIB_DIR)/javaparser-core-3.26.0.jar
GSON_JAR       := $(LIB_DIR)/gson-2.11.0.jar

# Classpath separator (Windows vs others)
ifeq ($(OS),Windows_NT)
  CP_SEP := ;
else
  CP_SEP := :
endif

# Combined classpath for compile & run
CLASSPATH := $(BIN_DIR)$(CP_SEP)$(JAVAPARSER_JAR)$(CP_SEP)$(GSON_JAR)
CLASSPATH_WITH_JUNIT := $(CLASSPATH)$(CP_SEP)$(JUNIT_JAR)

# Default
.DEFAULT_GOAL := help

help:
	@echo "Pure Java Codebase Analyzer"
	@echo ""
	@echo "Commands:"
	@echo "  make build              – compile all .java files (with JUnit + libs on classpath)"
	@echo "  make jar                – build runnable JAR"
	@echo "  make run                – start interactive REPL mode"
	@echo "  make exec CMD=\"...\"     – run specific analyzer command"
	@echo "  make test               – run all JUnit tests"
	@echo "  make clean              – remove build artifacts"
	@echo ""
	@echo "Examples:"
	@echo "  make run                          # Start interactive mode"
	@echo "  make exec CMD=\"analyze --path ./src\""
	@echo "  make exec CMD=\"keywords\""
	@echo "  make exec CMD=\"list classes\""
	@echo "  make test"
	@echo ""

# Compile all Java files (main + tests)
build:
	@echo "Compiling Java source..."
	@if [ ! -f "$(JAVAPARSER_JAR)" ]; then \
		echo "ERROR: $(JAVAPARSER_JAR) not found. Put javaparser-core jar in lib/"; \
		exit 1; \
	fi
	@if [ ! -f "$(GSON_JAR)" ]; then \
		echo "ERROR: $(GSON_JAR) not found. Put gson jar in lib/"; \
		exit 1; \
	fi
	@mkdir -p $(BIN_DIR)
	@find $(SRC_DIR) -name "*.java" > sources.txt
	javac -cp "$(JAVAPARSER_JAR)$(CP_SEP)$(GSON_JAR)$(CP_SEP)$(JUNIT_JAR)" -d $(BIN_DIR) @sources.txt
	@echo "✓ Build complete!"

# Create a runnable JAR (your compiled classes only; libs are loaded via -cp when running)
jar: build
	@echo "Creating JAR..."
	@echo "Main-Class: $(MAIN_CLASS)" > manifest.txt
	jar cfm $(JAR_FILE) manifest.txt -C $(BIN_DIR) .
	@echo "✓ JAR built: $(JAR_FILE)"

# Start interactive REPL mode
run: jar
	@echo "Starting Codebase Analyzer (Interactive Mode)..."
	@echo ""
	@java -cp "$(JAR_FILE)$(CP_SEP)$(JAVAPARSER_JAR)$(CP_SEP)$(GSON_JAR)" $(MAIN_CLASS) repl

# Execute a specific analyzer command
exec: jar
	@java -cp "$(JAR_FILE)$(CP_SEP)$(JAVAPARSER_JAR)$(CP_SEP)$(GSON_JAR)" $(MAIN_CLASS) $(CMD)

# Run JUnit tests using the console launcher
test: build
	@if [ ! -f "$(JUNIT_JAR)" ]; then \
		echo "ERROR: $(JUNIT_JAR) not found. Put junit-platform-console-standalone jar in lib/"; \
		exit 1; \
	fi
	@echo "Running JUnit tests..."
	java -jar "$(JUNIT_JAR)" \
	  --class-path "$(BIN_DIR)$(CP_SEP)$(JAVAPARSER_JAR)$(CP_SEP)$(GSON_JAR)" \
	  --scan-class-path
	@echo "✓ Tests finished!"

clean:
	@echo "Cleaning build artifacts..."
	@rm -rf $(BIN_DIR)
	@rm -f $(JAR_FILE)
	@rm -f -r target
	@rm -f sources.txt
	@rm -f manifest.txt
	@echo "✓ Clean complete!"
