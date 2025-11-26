# Codebase Analyzer

A Java-based CLI tool to analyze Java codebases using **Functional Object-Oriented Programming** principles. Built with custom container implementations demonstrating advanced data structures and algorithms.

## Table of Contents
- [Overview](#overview)
- [Functional-OO Approach](#functional-oo-approach)
- [Architecture & Design](#architecture--design)
- [Container Implementations](#container-implementations)
- [Algorithms Used](#algorithms-used)
- [File Structure](#file-structure)
- [Features](#features)
- [Usage](#usage)
- [Development](#development)
- [Assignment Deliverables](#assignment-deliverables)

## Overview

This project demonstrates functional object-oriented programming by analyzing Java codebases. It leverages custom-built data structures (containers) that support both imperative operations and functional programming paradigms (map, filter, reduce). The analyzer reads, searches, sorts, and aggregates information from Java source files.

## Project Documentation

For a comprehensive explanation of the project's architecture, functional-OO design, and container usage, please refer to the main submission document:

👉 **[POPL Assignment Submission (popl_submission.md)](popl_submission.md)**

This document covers:
-   **Functional-OO Approach**: How we blended paradigms.
-   **Container Implementations**: Detailed usage of `GenericList`, `Stack`, `Queue`, etc.
-   **Design Philosophy**: The "What" vs "How" separation.
-   **Full Command List**: Summary of implemented features.

## Architecture & Design

### Core Components

```
┌─────────────────────────────────────────────────┐
│           CLI Layer (Commands)                  │
│  AnalyzeCommand, ListCommand, GrepCommand, etc. │
└─────────────────┬───────────────────────────────┘
                  │
┌─────────────────▼───────────────────────────────┐
│         Core Analysis Engine                    │
│    Scanner → Parser → Index                     │
└─────────────────┬───────────────────────────────┘
                  │
┌─────────────────▼───────────────────────────────┐
│      Custom Container Layer                     │
│  GenericList, Queue, Stack, Deque, PriorityQueue│
└─────────────────────────────────────────────────┘
```

For details on the container implementations and algorithms, see `popl_submission.md`.

## File Structure

### Source Files (`src/main/java/com/`)

#### analyzer/cli/
- **Main.java**: Entry point, command routing, help system
- **Command.java**: Interface for all command implementations
- ... (See `popl_submission.md` for full list)

#### analyzer/core/
- **Scanner.java**: BFS directory traversal
- **Parser.java**: AST parsing
- **Index.java**: In-memory index

#### containers/
- **ListContainer.java**: Abstract base
- **GenericList.java**: Functional list
- **Queue.java**, **Stack.java**, **Deque.java**, **PriorityQueueCustom.java**

## Features

See **[COMMANDS.md](COMMANDS.md)** for the full feature list and usage examples.

## Usage

For detailed instructions on how to build and run the project, please refer to the **[Run Guide](docs/Run_Guide.md)**.

For a complete reference of all available commands, examples, and options, see **[COMMANDS.md](COMMANDS.md)**.

### Quick Command Summary

- **analyze**: Scan and index a directory.
- **list**: List classes, methods, or fields.
- **grep**: Search for patterns in the codebase.
- **keywords**: Show top frequent keywords.
- **sort-by-keyword**: Sort files by keyword frequency.
- **sort-by-class-count**: Sort packages by number of classes.
- **aggregate**: Show overall codebase statistics.
- **metrics**: Show detailed metrics for a specific file.
- **inspect**: Deep dive into a class or file.
- **top**: Show top N classes by metric (methods/fields).
- **export**: Save results to JSON or CSV.
- **repl**: Start interactive mode.

## Development

### Building
```bash
# Using Make
make build

# Using Maven directly
mvn clean package

# Manual compilation (if needed)
javac -cp "lib/*" -d build src/main/java/com/**/*.java
```

### Testing
```bash
# Run all tests
make test
mvn test

# Run specific test class
make test TEST=StackTest
mvn -Dtest=StackTest test

# Run specific test method
mvn -Dtest=GenericListTest#testStableSort test
```

### Cleaning
```bash
make clean    # Removes build/, JAR, and temporary files
```

### Project Structure Metrics

- **Modules**: 6 container classes + 12 command classes + 3 core classes + 3 model classes
- **Total Classes**: 24 primary classes
- **Test Classes**: 5 comprehensive test suites
- **Lines of Code**: ~3000+ lines
- **Design Pattern**: Abstract Factory (ListContainer), Command Pattern (CLI), Strategy Pattern (Comparators)

## Assignment Deliverables

This project fulfills the PoPL Assignment requirements (CS F301, Sem-I 2025-26):

### ✅ Required Deliverables

1. **Chronological Development Log**: See `DEVLOG.md`
   - Timestamped design and development actions
   - Interpretation of specifications
   - GenAI assistance documentation
   - Implementation insights and decisions

2. **Demonstration-Ready Bundle**:
   - Single command to build and run: `make run`
   - Complete source code in `src/`
   - All dependencies in `lib/`
   - Build scripts: `Makefile`, `pom.xml`, `run.sh`, `run.bat`

3. **Documentation**:
   - This README with comprehensive architecture explanation
   - `COMMANDS.md` with usage examples
   - `docs/` folder with assignment specifications
   - Inline code documentation

4. **Video Demonstration**: (To be added)
   - Demo of all features
   - Command-line usage
   - REPL mode interaction

### Assignment Requirements Met

✅ **High-level interface**: CLI with menu-driven commands and REPL mode
✅ **List structures**: Custom implementations (GenericList, Stack, Queue, Deque, PriorityQueue)
✅ **Reading files**: Scanner with BFS traversal
✅ **Searching**: Grep command with regex support
✅ **Sorting**: Multiple sort operations (keyword count, class count)
✅ **Aggregation methods**: Keywords frequency, codebase statistics
✅ **Functional operations**: map, filter, reduce on GenericList
✅ **Use case**: Keyword frequency analysis from multiple files

### Example Use Case (From Assignment Spec)

**Task**: Analyze a directory of Java files and list keywords in decreasing order of frequency.

**Solution**:
```bash
# Analyze the directory
make run
> analyze --path ./src

# Get keyword statistics
> keywords --top 10

# Export results
> export --format json --output keywords.json
```

**Output**: Keywords sorted by frequency with counts, using PriorityQueueCustom for efficient top-N extraction.

### Functional-OO Implementation

**What (Declarative)**:
- Commands specify *what* analysis to perform
- Functional operations describe *what* transformations to apply
- Container APIs define *what* behaviors are available

**How (Implementation)**:
- Core engine handles *how* files are scanned and parsed
- Containers implement *how* data is stored and accessed
- Algorithms handle *how* sorting and aggregation occur

**Abstraction Layers**:
```
High-level: Commands (what user wants)
    ↓
Mid-level: Core analysis (what to extract)
    ↓
Low-level: Containers + Algorithms (how to store/process)
```

### Team Contributions

**Aniket Sonawane (2022B3A70031G)**:
- **Role**: Team Lead & Core Architect
- **Contributions**: 
  - Designed `ListContainer` hierarchy and `GenericList`.
  - Implemented core functional operations (`map`, `filter`, `reduce`).
  - Built the `Index` and `Scanner` logic.

**Vanshaj Bhudolia (2022B3A70972G)**:
- **Role**: Container Specialist
- **Contributions**:
  - Implemented `Stack`, `Queue`, and `Deque` containers.
  - Developed the REPL (Read-Eval-Print Loop) with command history.
  - Wrote unit tests for container classes.

**Vedant Kamath (2022B3A7xxxxG)**:
- **Role**: Analysis Engine Developer
- **Contributions**:
  - Implemented `Parser` using JavaParser.
  - Built `GrepCommand` and regex integration.
  - Developed `InspectCommand` for deep-diving into classes.

**Mayukh Saha (2022B3A7xxxxG)**:
- **Role**: CLI & Algorithms Developer
- **Contributions**:
  - Implemented `PriorityQueueCustom` and Heap algorithms.
  - Built `SortCommand`, `KeywordsCommand`, and `AggregateCommand`.
  - Created documentation and build scripts (`Makefile`, `run.sh`).

## External Dependencies

### Libraries (in `lib/`)

1. **JavaParser (3.26.0)**:
   - Purpose: Parse Java source files
   - Usage: Extract AST (Abstract Syntax Tree)
   - API: `StaticJavaParser.parse(File)`

2. **Gson (2.11.0)**:
   - Purpose: JSON serialization/deserialization
   - Usage: Export command results to JSON
   - API: `Gson.toJson()`, `Gson.fromJson()`

3. **JUnit Platform Console Standalone (1.10.2)**:
   - Purpose: Unit testing framework
   - Usage: Automated test execution
   - API: JUnit 5 annotations and assertions

### Why Local Dependencies?

- **Reproducibility**: Exact versions guaranteed
- **Offline capability**: No internet required for build
- **Simplicity**: Single command build without Maven Central
- **Assignment compliance**: Self-contained deliverable

## Complexity Analysis

### Time Complexities

| Operation | Container | Complexity |
|-----------|-----------|------------|
| Add | Stack | O(1) amortized |
| Add | Queue | O(1) amortized |
| Add | GenericList (end) | O(1) amortized |
| Add | PriorityQueue | O(log n) |
| Remove | Stack | O(1) |
| Remove | Queue | O(1) |
| Remove | GenericList (end) | O(1) |
| Remove | PriorityQueue | O(log n) |
| Get by index | GenericList | O(1) |
| Sort | GenericList | O(n log n) |
| Search | All | O(n) |
| Peek | All | O(1) |

### Space Complexities

| Container | Space | Notes |
|-----------|-------|-------|
| GenericList | O(n) | Array-backed |
| Stack | O(n) | Array-backed |
| Queue | O(n) | LinkedList-backed |
| Deque | O(n) | LinkedList-backed |
| PriorityQueue | O(n) | Array-backed heap |

## Modularity & Design Quality

### Metrics (from Assignment Evaluation)

**Component A** (One module per type): 96/96
- Each container in separate file
- One class per module

**Component B** (Single header per type): 24/24
- Clean class definitions
- Clear interfaces

**Component C** (No imperative code in headers): 0/12
- Implementation note: Java doesn't separate headers/implementation
- All logic in class files (standard Java practice)

**Component D** (Minimal change if storage changes): 4/4
- Abstract ListContainer allows backing storage flexibility
- Subclasses can override storage choice

**Total Score**: 124/136

### Design Patterns Used

1. **Abstract Factory**: ListContainer provides common interface
2. **Command Pattern**: Each CLI command is a separate Command implementation
3. **Strategy Pattern**: Comparators for sorting/priority
4. **Template Method**: ListContainer defines algorithm skeleton
5. **Iterator Pattern**: For traversing containers (planned)

## Troubleshooting

### Common Issues

**Issue**: `Scanner closed` exception in REPL
**Fix**: Scanner now properly closed after loop exits (fixed in ReplCommand.java)

**Issue**: Unknown command in REPL
**Fix**: Type `help` to see all available commands

**Issue**: Build fails with missing dependencies
**Fix**: Ensure `lib/` directory contains all JARs

**Issue**: Tests fail
**Fix**: Run `make clean` then `make test`

## Future Enhancements

- [ ] Iterator implementation for all containers
- [ ] Parallel processing for large codebases
- [ ] Web UI for visualization
- [ ] Git integration for version analysis
- [ ] Code complexity metrics (cyclomatic complexity)
- [ ] Dependency graph generation
- [ ] Performance benchmarking tools

## License

Academic project for CS F301 (PoPL) - BITS Pilani

## References

- Assignment specification: `docs/Newassignment.md`
- Container design: `docs/oldAssignment_doc.md`
- JavaParser documentation: https://javaparser.org/
- Functional programming in Java: https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html

---

**For detailed command usage, see `COMMANDS.md`**
