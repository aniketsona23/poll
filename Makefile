.PHONY: clean run help compile analyzer test-all

# Default target
.DEFAULT_GOAL := help

# Maven command
MVN := mvn

# Java commands
JAVAC := javac
JAVA := java

# Directories
SRC_DIR := src/main/java
BIN_DIR := bin
CONTAINERS := com/containers
INSIGHTS := com/myorg/gitinsights

# Test class to run (can be overridden: make run TEST=StackTest)
TEST ?=

help:
	@echo "╔════════════════════════════════════════════════════════╗"
	@echo "║  PoPL Quiz - Makefile                                  ║"
	@echo "╚════════════════════════════════════════════════════════╝"
	@echo ""
	@echo "Available targets:"
	@echo "  make compile        - Compile Git Commit Log Analyzer"
	@echo "  make analyzer       - Run Git Commit Log Analyzer (CLI)"
	@echo "  make test-all       - Run all Maven tests"
	@echo "  make run            - Run specific test or all tests"
	@echo "  make clean          - Clean all build artifacts"
	@echo "  make help           - Show this help message"
	@echo ""
	@echo "Git Analyzer Examples:"
	@echo "  make compile        # Compile the analyzer"
	@echo "  make analyzer       # Start interactive CLI"
	@echo ""
	@echo "Maven Test Examples:"
	@echo "  make test-all                      # Run all tests"
	@echo "  make run TEST=StackTest            # Run StackTest class"
	@echo "  make run TEST=StackTest#testEmpty  # Run specific test method"
	@echo ""

compile:
	@echo "Compiling Git Commit Log Analyzer..."
	@mkdir -p $(BIN_DIR)
	$(JAVAC) -d $(BIN_DIR) $(SRC_DIR)/$(CONTAINERS)/*.java $(SRC_DIR)/$(INSIGHTS)/*.java
	@echo "✓ Compilation successful!"
	@echo ""
	@echo "To run the analyzer, use: make analyzer"

analyzer: compile
	@echo "Starting Git Commit Log Analyzer..."
	@echo ""
	$(JAVA) -cp $(BIN_DIR) com.myorg.gitinsights.Main

test-all:
	@echo "Running all Maven tests..."
	$(MVN) clean test

clean:
	@echo "Cleaning build artifacts..."
	@rm -rf $(BIN_DIR)
	$(MVN) clean
	@echo "✓ Clean complete!"

run:
ifeq ($(TEST),)
	@echo "Running all Maven tests..."
	$(MVN) clean test
else
	@echo "Running test: $(TEST)"
	$(MVN) -Dtest=$(TEST) test
endif

