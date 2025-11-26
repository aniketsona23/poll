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

## Functional-OO Approach

### Why Functional-OO?

This project follows a **Functional Object-Oriented** design philosophy:

1. **Object-Oriented Foundation**: 
   - Clear class hierarchies with `ListContainer<T>` as abstract base
   - Encapsulation of data structures and behaviors
   - Polymorphism through abstract methods (`add`, `remove`, `peek`)
   - Inheritance for code reuse across container types

2. **Functional Programming Elements**:
   - **Higher-order functions**: `GenericList` supports `map`, `filter`, `reduce`
   - **Immutability focus**: Operations create new views rather than mutating state where possible
   - **Declarative approach**: Commands focus on *what* to do, not *how* to do it
   - **Function composition**: Complex operations built from simpler functional primitives

3. **Separation of Concerns**:
   - **What vs How**: Command classes specify *what* analysis to perform
   - Core algorithms (parsing, scanning) handle *how* it's executed
   - Container abstractions separate storage from usage semantics

### Design Rationale

**Why this approach was chosen:**
- **Extensibility**: Easy to add new commands and analysis types
- **Testability**: Pure functional operations are easier to test
- **Maintainability**: Clear separation between data structures and business logic
- **Performance**: Custom containers optimized for specific access patterns
- **Learning**: Demonstrates both OO design patterns and functional programming concepts

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

### Container Usage in Analyzer

1. **GenericList<T>**: Primary storage for indexed data
   - Stores lists of `ClassInfo`, `MethodInfo`, `FieldInfo`
   - Used for maintaining ordered collections with random access
   - Supports functional operations for filtering and transforming results

2. **Queue<T>**: Breadth-First Search operations
   - Directory traversal during file scanning
   - Storing and processing grep match results
   - FIFO processing of analysis tasks

3. **Stack<T>**: Command history management
   - REPL mode maintains command history for recall
   - LIFO access pattern for undo/history features

4. **Deque<T>**: Double-ended operations
   - Flexible insert/remove at both ends
   - Used in certain parsing scenarios requiring bidirectional access

5. **PriorityQueueCustom<T>**: Ordered aggregation
   - Sorting files by keyword count (top-N queries)
   - Sorting packages by class count
   - Heap-based priority ordering for efficient aggregation

## Container Implementations

### ListContainer<T> (Abstract Base Class)

**Purpose**: Common foundation for all containers

**Protected Fields**:
- `List<T> elements` - Backing storage
- `int size` - Number of elements

**Common APIs**:
```java
boolean isEmpty()           // O(1) - Check if empty
int size()                  // O(1) - Get size
void clear()                // O(n) - Remove all elements
boolean update(int, T)      // O(1)/O(n) - Update at index
boolean contains(T)         // O(n) - Check containment
String toString()           // O(n) - String representation
abstract void add(T)        // Defined by subclass
abstract T remove()         // Defined by subclass
abstract T peek()           // Defined by subclass
```

### GenericList<T>

**Purpose**: Index-based list with functional operations

**Key APIs**:
```java
void addLast(T)                          // O(1) amortized
void addFirst(T)                         // O(n)
void addAt(int index, T)                 // O(n)
T removeAt(int index)                    // O(n)
T get(int index)                         // O(1)
void sort(Comparator<T>)                 // O(n log n) - Merge Sort
GenericList<R> map(Function<T,R>)        // O(n) - Transform elements
GenericList<T> filter(Predicate<T>)      // O(n) - Filter elements
R reduce(R identity, BinaryOperator<R>)  // O(n) - Aggregate
```

**Algorithm**: Stable Merge Sort for ordering

### Stack<T>

**Purpose**: LIFO (Last In, First Out) operations

**Key APIs**:
```java
void push(T)        // O(1) amortized
T pop()             // O(1)
T peek()            // O(1)
```

**Usage**: REPL command history

### Queue<T>

**Purpose**: FIFO (First In, First Out) operations

**Key APIs**:
```java
void enqueue(T)     // O(1) amortized
T dequeue()         // O(1)
T peek()            // O(1)
```

**Usage**: BFS directory scanning, grep result processing

### Deque<T>

**Purpose**: Double-ended queue operations

**Key APIs**:
```java
void addFirst(T)    // O(1)
void addLast(T)     // O(1)
T removeFirst()     // O(1)
T removeLast()      // O(1)
T peekFirst()       // O(1)
T peekLast()        // O(1)
```

### PriorityQueueCustom<T>

**Purpose**: Heap-based priority ordering

**Key APIs**:
```java
void add(T)                             // O(log n)
T remove()                              // O(log n)
T peek()                                // O(1)
PriorityQueueCustom(Comparator<T>)      // Constructor with custom ordering
```

**Algorithm**: Binary Heap with bubble-up and bubble-down operations

## Algorithms Used

### 1. Merge Sort (GenericList)
- **Complexity**: O(n log n) time, O(n) space
- **Properties**: Stable sorting algorithm
- **Usage**: Sorting files, packages, keywords by count/name
- **Implementation**: Recursive divide-and-conquer with merge operation

### 2. Binary Heap Operations (PriorityQueueCustom)
- **Complexity**: O(log n) insert/remove, O(1) peek
- **Properties**: Min-heap or max-heap based on comparator
- **Usage**: Top-N keyword extraction, priority-based aggregation
- **Operations**: bubbleUp (heapify-up), bubbleDown (heapify-down)

### 3. Breadth-First Search (Scanner)
- **Complexity**: O(V + E) where V = directories, E = files
- **Usage**: Directory traversal for finding Java files
- **Implementation**: Queue-based iterative BFS

### 4. Abstract Syntax Tree Parsing (Parser)
- **Library**: JavaParser
- **Complexity**: O(n) where n = file size
- **Usage**: Extract classes, methods, fields from Java source
- **Output**: Structured `ClassInfo` objects with metadata

### 5. Functional Operations (GenericList)
- **map**: O(n) - Transform each element
- **filter**: O(n) - Select elements matching predicate
- **reduce**: O(n) - Aggregate to single value

## File Structure

### Source Files (`src/main/java/com/`)

#### analyzer/cli/
- **Main.java**: Entry point, command routing, help system
- **Command.java**: Interface for all command implementations
- **AnalyzeCommand.java**: Scans and indexes directory
- **ListCommand.java**: Lists classes/methods/variables with file locations
- **GrepCommand.java**: Pattern search across codebase
- **KeywordsCommand.java**: Keyword frequency analysis
- **SortCommand.java**: Sort by keyword count or class count
- **AggregateCommand.java**: Statistical aggregation of codebase
- **MetricsCommand.java**: Detailed metrics for individual files
- **ExportCommand.java**: Export results to JSON/CSV
- **InspectCommand.java**: Deep inspection of specific class/file
- **TopCommand.java**: Top-N classes by methods/fields using PriorityQueue
- **ReplCommand.java**: Interactive REPL mode with history

#### analyzer/core/
- **Scanner.java**: BFS directory traversal, finds Java files
  - Uses `Queue<File>` for breadth-first traversal
  - Returns `GenericList<File>` of discovered files

- **Parser.java**: Parses Java files using JavaParser
  - Converts files to `ClassInfo` objects
  - Extracts methods, fields, imports
  - Returns `GenericList<ClassInfo>`

- **Index.java**: In-memory index of codebase
  - Stores `GenericList<ClassInfo>`
  - Provides search and lookup capabilities
  - Uses `PriorityQueueCustom` for top-N operations

#### analyzer/model/
- **ClassInfo.java**: Represents a Java class
  - Contains methods, fields, package info
  - Uses `GenericList` for method/field storage

- **MethodInfo.java**: Represents a method
  - Name, return type, parameters, modifiers

- **FieldInfo.java**: Represents a field
  - Name, type, modifiers

#### containers/
- **ListContainer.java**: Abstract base for all containers
- **GenericList.java**: Full-featured list with functional ops
- **Queue.java**: FIFO queue
- **Stack.java**: LIFO stack
- **Deque.java**: Double-ended queue
- **PriorityQueueCustom.java**: Binary heap priority queue

### Test Files (`src/test/java/com/`)

- **analyzer/ContainerUsageTest.java**: Integration tests
- **containers/GenericListTest.java**: GenericList unit tests
- **containers/QueueTest.java**: Queue unit tests
- **containers/StackTest.java**: Stack unit tests
- **containers/DequeTest.java**: Deque unit tests
- **containers/PriorityQueueCustomTest.java**: Priority queue tests

### Build & Configuration

- **pom.xml**: Maven configuration (Java 11, JUnit, JavaParser, Gson)
- **Makefile**: Build automation and convenience commands
- **run.sh / run.bat**: Cross-platform execution scripts
- **lib/**: Local JAR dependencies (gson, javaparser, junit)

## Features

- **Analyze**: Scans and indexes Java files in a directory
- **List**: Lists classes (with file locations), methods (with signatures), and variables/fields from indexed code
- **Find/Grep**: Search for identifiers or regex patterns
- **Keywords**: Count and list top-N keywords with frequency
- **Sort**: Sort files by keyword count or packages by class count
- **Aggregate**: Show codebase statistics (classes, methods, LOC)
- **Metrics**: Detailed metrics for individual files
- **Inspect**: Deep dive into specific class or file with complete details
- **Top**: Find top-N classes by methods, fields, or other metrics using PriorityQueue
- **Export**: Export results to JSON or CSV formats
- **REPL**: Interactive mode with command history using Stack

## Usage

### Prerequisites
- Java 11+
- Maven (wrapper included)

### Building and Running

**Using Make (Linux/Mac/Windows with Make):**
```bash
# Build the project
make build

# Run in interactive mode
make repl

# Or use specific commands
make analyze PATH=./testdata
make list
make grep PATTERN="ArrayList"
make keywords
make aggregate
```

**Using Scripts:**

**Linux/Mac:**
```bash
./run.sh <command> [options]
```

**Windows:**
```bat
.\run.bat <command> [options]
```

### Commands

- **Analyze**:
  ```bash
  ./run.sh analyze --path <directory>
  # or
  make analyze PATH=<directory>
  ```

- **List Classes** (with file locations):
  ```bash
  ./run.sh list classes
  # or
  make list
  ```

- **List Methods** (with signatures and LOC):
  ```bash
  ./run.sh list methods
  ```

- **List Variables/Fields**:
  ```bash
  ./run.sh list variables
  # or
  ./run.sh list fields
  ```

- **Grep**:
  ```bash
  ./run.sh grep "pattern"
  # or
  make grep PATTERN="pattern"
  ```

- **Inspect Class/File** (detailed information):
  ```bash
  ./run.sh inspect Main
  # or
  ./run.sh inspect Main.java
  ```

- **Top N Classes** (by methods or fields):
  ```bash
  ./run.sh top 5 methods
  # or
  ./run.sh top 10 fields
  ```

- **Keywords**:
  ```bash
  ./run.sh keywords
  # or
  make keywords
  ```

- **Sort by Keyword**:
  ```bash
  ./run.sh sort-by-keyword "TODO"
  # or
  make sort
  ```

- **Aggregate Stats**:
  ```bash
  ./run.sh aggregate
  # or
  make aggregate
  ```

- **Metrics for File**:
  ```bash
  ./run.sh metrics Main.java
  # or
  make metrics FILE=Main.java
  ```

- **Export Results**:
  ```bash
  ./run.sh export
  # or
  make export
  ```

- **Interactive Mode**:
  ```bash
  ./run.sh repl
  # or
  make run
  ```

**Inside REPL, try:**
```bash
> help
> list classes
> list methods
> inspect Main
> top 5 methods
> top 10 fields
> exit
```

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
- Project architecture and design
- Container implementations (ListContainer, GenericList, PriorityQueueCustom)
- Core analysis engine (Scanner, Parser, Index)
- CLI framework and command implementations
- Functional programming features (map, filter, reduce)
- Build system and documentation

**Vanshaj Bhudolia (2022B3A70972G)**:
- Additional container implementations (Stack, Queue, Deque)
- Test suite development
- REPL mode implementation
- Command history functionality
- Integration testing

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
