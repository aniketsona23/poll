# Codebase Analyzer - Command Reference

Complete guide to using the Codebase Analyzer CLI tool.

## Table of Contents
- [Getting Started](#getting-started)
- [Build Commands](#build-commands)
- [Running the Analyzer](#running-the-analyzer)
- [Analysis Commands](#analysis-commands)
- [Inspection Commands](#inspection-commands)
- [REPL Mode](#repl-mode)
- [Examples & Use Cases](#examples--use-cases)
- [Command Line Options](#command-line-options)

## Getting Started

### Quick Start

```bash
# 1. Build the project
make build

# 2. Start interactive mode
make run

# 3. Inside REPL, type commands:
> help
> analyze --path ./testdata
> list classes
> keywords
> exit
```

## Build Commands

### Using Make (Recommended)

```bash
# Build the project (compile + package JAR)
make build

# Clean build artifacts
make clean

# Run tests
make test

# Run specific test
make test TEST=StackTest

# Run specific test method
make test TEST=GenericListTest#testStableSort
```

### Using Maven Directly

```bash
# Build
mvn clean package

# Run tests
mvn test

# Run specific test
mvn -Dtest=StackTest test

# Clean
mvn clean
```

### Manual Compilation (Advanced)

```bash
# Compile
javac -cp "lib/*" -d build src/main/java/com/**/*.java

# Create JAR
jar cfm codebase-analyzer.jar manifest.txt -C build .

# Run
java -cp "codebase-analyzer.jar:lib/*" com.analyzer.cli.Main <command>
```

## Running the Analyzer

### Three Ways to Run

#### 1. Interactive REPL Mode (Recommended)

```bash
make run
# or
./run.sh repl
# or
java -jar codebase-analyzer.jar repl
```

**Inside REPL**:
```
> help                          # Show available commands
> analyze --path ./src          # Analyze source directory
> list classes                  # List all classes
> grep "ArrayList"              # Search pattern
> keywords --top 10             # Top 10 keywords
> history                       # Show command history
> exit                          # Exit REPL
```

#### 2. Single Command Execution

```bash
# Using Make
make exec CMD="analyze --path ./src"
make exec CMD="list classes"
make exec CMD="keywords"

# Using run scripts
./run.sh analyze --path ./testdata
./run.sh grep "public static"
./run.sh aggregate

# Windows
.\run.bat analyze --path .\testdata
.\run.bat list classes
```

#### 3. Direct JAR Execution

```bash
java -cp "codebase-analyzer.jar:lib/javaparser-core-3.26.0.jar:lib/gson-2.11.0.jar" \
  com.analyzer.cli.Main analyze --path ./src
```

## Analysis Commands

### 1. analyze - Scan and Index Directory

**Purpose**: Scans a directory for Java files and builds an in-memory index.

**Syntax**:
```bash
analyze [--path <directory>]
```

**Options**:
- `--path <directory>`: Directory to analyze (default: current directory `.`)

**Examples**:
```bash
# Analyze current directory
analyze

# Analyze specific directory
analyze --path ./src

# Analyze testdata
analyze --path ./testdata

# Analyze nested structure
analyze --path ./src/main/java
```

**What it does**:
1. Uses `Queue<File>` for BFS traversal of directory tree
2. Finds all `.java` files
3. Parses each file using JavaParser
4. Extracts classes, methods, fields
5. Builds index in memory using `GenericList<ClassInfo>`

**Output**:
```
Scanning and indexing...
Found 50 Java files
Index built. Found 45 classes.
  - 12 classes in com.analyzer.cli
  - 6 classes in com.analyzer.core
  - 3 classes in com.analyzer.model
  - 6 classes in com.containers
  - Total methods: 287
  - Total fields: 94
```

---

### 2. list - List Program Elements

**Purpose**: Lists classes, methods, or variables from the indexed codebase.

**Syntax**:
```bash
list <type>
```

**Types**:
- `classes`: List all classes with package info
- `methods`: List all methods with signatures
- `variables`: List all fields/variables

**Examples**:
```bash
# List all classes
list classes

# List all methods
list methods

# List all variables/fields
list variables
```

**Sample Output**:

```bash
> list classes
Classes found: 19
1. com.analyzer.cli.Main
2. com.analyzer.cli.AnalyzeCommand
3. com.analyzer.cli.ListCommand
4. com.analyzer.core.Scanner
5. com.containers.GenericList
...

> list methods
Methods found: 287
com.analyzer.cli.Main.main(String[])
com.analyzer.cli.Main.printHelp()
com.containers.GenericList.add(T)
com.containers.GenericList.sort(Comparator<T>)
com.containers.Stack.push(T)
...

> list variables
Fields found: 94
com.analyzer.cli.Main.commands : Map<String, Command>
com.containers.ListContainer.elements : List<T>
com.containers.ListContainer.size : int
...
```

---

### 3. grep - Search for Patterns

**Purpose**: Search for identifiers or regex patterns across the codebase.

**Syntax**:
```bash
grep "<pattern>"
```

**Pattern Types**:
- Simple string: `"ArrayList"`
- Regex: `"public\s+static\s+void"`
- Java keywords: `"synchronized"`, `"volatile"`

**Examples**:
```bash
# Find all uses of ArrayList
grep "ArrayList"

# Find public static methods
grep "public static"

# Find specific method calls
grep "\.parse\("

# Find imports
grep "^import"

# Find class declarations
grep "class\s+\w+\s+extends"
```

**Output**:
```
Matches found: 23
File: src/main/java/com/analyzer/core/Scanner.java
  Line 15: import java.util.ArrayList;
  Line 42: List<File> files = new ArrayList<>();

File: src/main/java/com/containers/GenericList.java
  Line 8: import java.util.ArrayList;
  Line 23: this.elements = new ArrayList<>();
...
```

**Implementation Notes**:
- Uses `Queue<Match>` to store and process results
- Performs line-by-line regex matching
- Returns file location, line number, and matched line

---

### 4. keywords - Keyword Frequency Analysis

**Purpose**: Count and display keywords by frequency across the codebase.

**Syntax**:
```bash
keywords [--top <N>]
```

**Options**:
- `--top <N>`: Show only top N keywords (default: all)

**Examples**:
```bash
# Show all keywords with frequencies
keywords

# Show top 10 most frequent keywords
keywords --top 10

# Show top 5
keywords --top 5
```

**Output**:
```
Keyword Frequency Analysis
==========================
Total keywords found: 1247

Top 10 Keywords:
1. public     : 287 occurrences
2. private    : 156 occurrences
3. void       : 143 occurrences
4. return     : 128 occurrences
5. static     : 89 occurrences
6. final      : 67 occurrences
7. class      : 45 occurrences
8. new        : 234 occurrences
9. if         : 198 occurrences
10. for       : 87 occurrences
```

**How it works**:
1. Scans all indexed files
2. Tokenizes and counts Java keywords
3. Uses `PriorityQueueCustom` with max-heap for top-N extraction
4. Returns sorted list by frequency

**Use Case** (from assignment): Analyze keyword distribution to understand coding patterns.

---

### 5. sort-by-keyword - Sort Files by Keyword Count

**Purpose**: Sort source files by the occurrence count of a specific keyword.

**Syntax**:
```bash
sort-by-keyword "<keyword>"
```

**Examples**:
```bash
# Find files with most TODO comments
sort-by-keyword "TODO"

# Find files with most synchronized blocks
sort-by-keyword "synchronized"

# Find most complex files (by if count)
sort-by-keyword "if"

# Find files with most classes
sort-by-keyword "class"
```

**Output**:
```
Files sorted by keyword "TODO":
1. Main.java                    : 15 occurrences
2. Scanner.java                 : 8 occurrences
3. Parser.java                  : 6 occurrences
4. GenericList.java             : 4 occurrences
5. AnalyzeCommand.java          : 2 occurrences
```

**Implementation**:
- Uses `PriorityQueueCustom` with custom comparator
- Counts keyword per file
- Sorts in descending order

---

### 6. sort-by-class-count - Sort Packages by Class Count

**Purpose**: Sort packages by the number of classes they contain.

**Syntax**:
```bash
sort-by-class-count
```

**Examples**:
```bash
sort-by-class-count
```

**Output**:
```
Packages sorted by class count:
1. com.analyzer.cli             : 10 classes
2. com.containers               : 6 classes
3. com.analyzer.core            : 3 classes
4. com.analyzer.model           : 3 classes
```

**Use Case**: Identify which packages are largest/most complex.

---

### 7. aggregate - Codebase Statistics

**Purpose**: Display aggregated statistics about the entire codebase.

**Syntax**:
```bash
aggregate
```

**Examples**:
```bash
aggregate
```

**Output**:
```
╔════════════════════════════════════════╗
║     Codebase Statistics                ║
╚════════════════════════════════════════╝

Files Analyzed    : 50
Total Classes     : 45
Total Methods     : 287
Total Fields      : 94
Total Lines       : 12,456

Packages          : 4
  - com.analyzer.cli     : 10 classes
  - com.containers       : 6 classes
  - com.analyzer.core    : 3 classes
  - com.analyzer.model   : 3 classes

Average Methods per Class : 6.4
Average Fields per Class  : 2.1

Top 5 Largest Classes:
1. GenericList          : 45 methods
2. Main                 : 12 methods
3. Parser               : 18 methods
4. Scanner              : 8 methods
5. PriorityQueueCustom  : 15 methods
```

**Implementation**:
- Aggregates data from `Index`
- Uses `reduce` operation on `GenericList` for summation
- Uses `PriorityQueueCustom` for top-N extraction

---

### 8. metrics - Detailed File Metrics

**Purpose**: Show detailed metrics for a specific file.

**Syntax**:
```bash
metrics <filename>
```

**Examples**:
```bash
# Get metrics for Main.java
metrics Main.java

# Get metrics for GenericList
metrics GenericList.java

# Full path also works
metrics src/main/java/com/analyzer/cli/Main.java
```

**Output**:
```
╔════════════════════════════════════════╗
║  Metrics for Main.java                 ║
╚════════════════════════════════════════╝

File: Main.java
Package: com.analyzer.cli
Size: 3.2 KB

Classes: 1
  - Main

Methods: 12
  - public static void main(String[])
  - private static void printHelp()
  - ... (10 more)

Fields: 3
  - private static final Map<String, Command> commands
  - ... (2 more)

Lines of Code: 145
Comment Lines: 23
Blank Lines: 18

Complexity Metrics:
  - Cyclomatic Complexity: 15
  - Method Count: 12
  - Max Method Length: 45 lines
  - Average Method Length: 12 lines

Keywords (top 5):
  1. public  : 12
  2. private : 8
  3. static  : 5
  4. void    : 10
  5. return  : 15
```

---

### 9. export - Export Results

**Purpose**: Export analysis results to JSON or CSV format.

**Syntax**:
```bash
export [--format <json|csv>] [--output <filename>]
```

**Options**:
- `--format <json|csv>`: Output format (default: json)
- `--output <filename>`: Output file (default: analysis-results.json)

**Examples**:
```bash
# Export to JSON (default)
export

# Export to CSV
export --format csv

# Export to specific file
export --format json --output my-analysis.json

# Export to CSV with custom name
export --format csv --output codebase-stats.csv
```

**JSON Output Structure**:
```json
{
  "timestamp": "2025-11-26T15:30:00Z",
  "summary": {
    "totalFiles": 50,
    "totalClasses": 45,
    "totalMethods": 287,
    "totalFields": 94
  },
  "classes": [
    {
      "name": "Main",
      "package": "com.analyzer.cli",
      "methods": [...],
      "fields": [...]
    }
  ],
  "keywords": {
    "public": 287,
    "private": 156,
    ...
  }
}
```

**CSV Output Structure**:
```csv
ClassName,Package,MethodCount,FieldCount,LineCount
Main,com.analyzer.cli,12,3,145
GenericList,com.containers,45,4,523
...
```

---

### 10. help - Show Help

**Purpose**: Display help information about available commands.

**Syntax**:
```bash
help
```

**Examples**:
```bash
help
```

**Output**: Shows formatted help with all commands and examples.

---

## Inspection Commands

### 11. inspect - Inspect Class or File

**Purpose**: Display detailed information about a specific class or file, including all methods, fields, and statistics.

**Syntax**:
```bash
inspect <class-name-or-file>
```

**Examples**:
```bash
# Inspect by class name
inspect Main

# Inspect by file name
inspect Main.java

# Inspect by full qualified name
inspect com.analyzer.cli.Main

# Inspect another class
inspect GenericList
```

**Output**:
```
╔════════════════════════════════════════════════════════════════╗
║                    Class Inspection                            ║
╚════════════════════════════════════════════════════════════════╝

Class Name:    Main
Package:       com.analyzer.cli
Full Name:     com.analyzer.cli.Main
File:          /path/to/Main.java

═══════════════════════════════════════════════════════════════
                         Statistics
═══════════════════════════════════════════════════════════════
Total Methods: 12
Total Fields:  3

───────────────────────────────────────────────────────────────
Fields (3):
───────────────────────────────────────────────────────────────
   1. commands                       : Map<String, Command>

───────────────────────────────────────────────────────────────
Methods (12):
───────────────────────────────────────────────────────────────
   1. void main(String[])
      LOC: 45, Line: 30
   2. void printHelp()
      LOC: 67, Line: 75
   ... (10 more methods)

Total Lines of Code (Methods): 456
Average LOC per Method: 38.0
╚════════════════════════════════════════════════════════════════╝
```

**What it shows**:
- Class name, package, and file location
- Total method and field counts
- Complete list of all fields with types
- Complete list of all methods with signatures
- Lines of code per method
- Method starting line numbers
- Aggregate statistics

**Use Cases**:
- Understand class structure quickly
- Count variables/methods in a specific class
- Review method signatures before modification
- Identify large methods needing refactoring

---

### 12. top - Top N Classes by Metric

**Purpose**: Show the top N classes ranked by a specific metric (methods, fields, etc.). Uses `PriorityQueueCustom` for efficient top-N extraction.

**Syntax**:
```bash
top <N> <metric>
```

**Metrics**:
- `methods`: Rank by number of methods
- `fields` or `variables`: Rank by number of fields

**Examples**:
```bash
# Top 5 classes with most methods
top 5 methods

# Top 10 classes with most fields
top 10 fields

# Top 3 classes by method count
top 3 methods

# Find classes with most variables
top 20 variables
```

**Output (top 5 methods)**:
```
╔════════════════════════════════════════════════════════════════╗
║              Top 5 Classes by Method Count                     ║
╚════════════════════════════════════════════════════════════════╝

 1. com.containers.GenericList                   :  45 methods
    File: GenericList.java
 2. com.analyzer.cli.Main                        :  12 methods
    File: Main.java
 3. com.analyzer.core.Parser                     :  18 methods
    File: Parser.java
 4. com.containers.PriorityQueueCustom           :  15 methods
    File: PriorityQueueCustom.java
 5. com.analyzer.cli.ReplCommand                 :  10 methods
    File: ReplCommand.java
```

**Output (top 5 fields)**:
```
╔════════════════════════════════════════════════════════════════╗
║              Top 5 Classes by Field Count                      ║
╚════════════════════════════════════════════════════════════════╝

 1. com.analyzer.model.ClassInfo                 :   8 fields
    File: ClassInfo.java
 2. com.analyzer.model.MethodInfo                :   5 fields
    File: MethodInfo.java
 3. com.analyzer.core.Index                      :   4 fields
    File: Index.java
 4. com.containers.ListContainer                 :   2 fields
    File: ListContainer.java
 5. com.analyzer.cli.Main                        :   1 fields
    File: Main.java
```

**How it works**:
1. Retrieves all classes from Index
2. Adds them to `PriorityQueueCustom` with appropriate comparator
3. Uses max-heap (reversed comparator) for descending order
4. Extracts top N elements efficiently (O(n log k) complexity)

**Use Cases**:
- Identify most complex classes (by method count)
- Find classes with excessive state (by field count)
- Target refactoring candidates
- Understand codebase complexity distribution
- Compare class sizes across packages

**Implementation Notes**:
- Uses functional `Comparator.comparingInt().reversed()` for max-heap
- Leverages custom PriorityQueueCustom container
- Efficient for large codebases (heap-based extraction)

---

### 10. help - Show Help

# Export to CSV with custom name
export --format csv --output codebase-stats.csv
```

**JSON Output Structure**:
```json
{
  "timestamp": "2025-11-26T15:30:00Z",
  "summary": {
    "totalFiles": 50,
    "totalClasses": 45,
    "totalMethods": 287,
    "totalFields": 94
  },
  "classes": [
    {
      "name": "Main",
      "package": "com.analyzer.cli",
      "methods": [...],
      "fields": [...]
    }
  ],
  "keywords": {
    "public": 287,
    "private": 156,
    ...
  }
}
```

**CSV Output Structure**:
```csv
ClassName,Package,MethodCount,FieldCount,LineCount
Main,com.analyzer.cli,12,3,145
GenericList,com.containers,45,4,523
...
```

---

### 10. help - Show Help

**Purpose**: Display help information about available commands.

**Syntax**:
```bash
help
```

**Examples**:
```bash
help
```

**Output**: Shows formatted help with all commands and examples.

---

## REPL Mode

### Starting REPL

```bash
# Method 1: Using Make
make run

# Method 2: Using run script
./run.sh repl

# Method 3: Direct execution
java -jar codebase-analyzer.jar repl
```

### REPL-Specific Commands

```bash
> help              # Show help in REPL
> history           # Show command history (uses Stack)
> exit              # Exit REPL mode
```

### REPL Features

1. **Command History**: Uses `Stack<String>` to maintain history
   - Commands stored in LIFO order
   - View with `history` command

2. **Persistent Session**: Index remains in memory
   - No need to re-analyze between commands
   - Fast subsequent queries

3. **Error Handling**: Graceful error messages
   - Invalid commands show suggestion to use `help`
   - Exceptions caught and displayed

4. **Tab Completion**: (Future feature)

### REPL Example Session

```bash
$ make run
Starting Codebase Analyzer (Interactive Mode)...

Scanning and indexing...
Index built. Found 45 classes.
Entering REPL mode. Type 'exit' to quit.

> help
[Help output displayed]

> list classes
Classes found: 45
1. com.analyzer.cli.Main
2. com.analyzer.cli.AnalyzeCommand
...

> grep "ArrayList"
Matches found: 23
...

> keywords --top 5
Top 5 Keywords:
1. public  : 287
2. private : 156
...

> history
History (last command on top):
keywords --top 5
grep "ArrayList"
list classes

> exit
Goodbye!
```

---

## Examples & Use Cases

### Use Case 1: Find All TODOs

```bash
make run
> analyze --path ./src
> grep "TODO"
> sort-by-keyword "TODO"
> export --format csv --output todos.csv
```

### Use Case 2: Analyze Code Complexity

```bash
./run.sh analyze --path ./src
./run.sh aggregate
./run.sh sort-by-keyword "if"
./run.sh metrics Main.java
```

### Use Case 3: Keyword Frequency Analysis (Assignment Example)

**Goal**: List keywords from multiple files in decreasing order of frequency.

```bash
# Start REPL
make run

# Analyze directory
> analyze --path ./testdata

# Get keyword frequencies
> keywords --top 10

# Export results
> export --format json --output keyword-analysis.json
```

**Output**:
```
Top 10 Keywords:
1. public     : 287 occurrences
2. private    : 156 occurrences
3. void       : 143 occurrences
...
```

### Use Case 4: Find Largest/Most Complex Classes

```bash
make run
> analyze --path ./src
> aggregate
> sort-by-keyword "if"      # Complexity indicator
> metrics GenericList.java  # Detailed metrics
```

### Use Case 5: Inspect Specific Class Details

```bash
# Start REPL
make run

# Analyze codebase
> analyze --path ./src

# Find class with most methods
> top 5 methods

# Inspect the top class
> inspect GenericList

# Check another class
> inspect Main.java

# Find variables in a class
> inspect ClassInfo
```

### Use Case 6: Identify Refactoring Candidates

```bash
./run.sh analyze --path ./src
./run.sh top 10 methods     # Find complex classes
./run.sh top 10 fields      # Find classes with too much state
./run.sh inspect Main       # Examine details
```

### Use Case 7: Package Structure Analysis

```bash
./run.sh analyze --path ./src
./run.sh sort-by-class-count
./run.sh aggregate
```

---

## Command Line Options

### Global Options (apply to multiple commands)

- `--path <directory>`: Specify directory to analyze (analyze command)
- `--top <N>`: Limit results to top N items (keywords command)
- `--format <json|csv>`: Output format (export command)
- `--output <file>`: Output filename (export command)

### Command Aliases

Some commands have multiple ways to invoke:

```bash
# These are equivalent
make run
./run.sh repl
java -jar codebase-analyzer.jar repl

# These are equivalent
make exec CMD="list classes"
./run.sh list classes
```

---

## Performance Notes

### For Large Codebases

**Optimization tips for analyzing large projects (1000+ files)**:

1. **Use specific paths**: 
   ```bash
   analyze --path ./src/main/java  # More specific
   # Instead of
   analyze --path ./  # Entire project
   ```

2. **Limit results**:
   ```bash
   keywords --top 20  # Instead of showing all
   ```

3. **Memory settings**:
   ```bash
   java -Xmx2g -jar codebase-analyzer.jar analyze --path ./large-project
   ```

### Expected Performance

| Files | Analysis Time | Memory Usage |
|-------|---------------|--------------|
| 10    | < 1 second    | ~50 MB       |
| 100   | 2-3 seconds   | ~100 MB      |
| 1000  | 15-20 seconds | ~250 MB      |
| 5000  | 60-90 seconds | ~500 MB      |

---

## Troubleshooting

### Common Errors

**Error**: `Scanner closed`
**Solution**: Fixed in latest version. Update ReplCommand.java if using old version.

**Error**: `NoSuchElementException` in REPL
**Cause**: Trying to access empty history or empty queue
**Solution**: Ensure data exists before accessing (check with `isEmpty()`)

**Error**: `ClassNotFoundException`
**Cause**: Missing JAR in classpath
**Solution**: Ensure `lib/` directory has all required JARs

**Error**: `OutOfMemoryError`
**Cause**: Analyzing very large codebase
**Solution**: Increase heap size with `-Xmx` flag

### Debug Mode

```bash
# Enable verbose output (if implemented)
java -Dverbose=true -jar codebase-analyzer.jar analyze --path ./src
```

---

## Advanced Usage

### Chaining Commands (via scripts)

```bash
#!/bin/bash
# analyze-and-report.sh

# Build
make build

# Analyze
java -jar codebase-analyzer.jar analyze --path ./src > /dev/null

# Generate reports
java -jar codebase-analyzer.jar aggregate > aggregate.txt
java -jar codebase-analyzer.jar keywords --top 20 > keywords.txt
java -jar codebase-analyzer.jar export --format json --output analysis.json

echo "Reports generated: aggregate.txt, keywords.txt, analysis.json"
```

### Integration with CI/CD

```yaml
# .github/workflows/code-analysis.yml
name: Code Analysis

on: [push, pull_request]

jobs:
  analyze:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Build Analyzer
        run: make build
      - name: Run Analysis
        run: |
          make exec CMD="analyze --path ./src"
          make exec CMD="aggregate"
          make exec CMD="export --format json --output analysis.json"
      - name: Upload Results
        uses: actions/upload-artifact@v2
        with:
          name: analysis-results
          path: analysis.json
```

---

## Quick Reference Card

```
╔══════════════════════════════════════════════════════════════════╗
║                    QUICK REFERENCE                               ║
╠══════════════════════════════════════════════════════════════════╣
║  Build:           make build                                     ║
║  Run REPL:        make run                                       ║
║  Run Command:     make exec CMD="<command>"                      ║
║  Test:            make test                                      ║
║  Clean:           make clean                                     ║
╠══════════════════════════════════════════════════════════════════╣
║  ANALYSIS COMMANDS                                               ║
║  analyze          Scan and index directory                       ║
║  list             List classes/methods/variables/fields          ║
║  grep             Search for patterns                            ║
║  keywords         Keyword frequency analysis                     ║
║  sort-by-keyword  Sort files by keyword count                    ║
║  aggregate        Show statistics                                ║
║  metrics          File-specific metrics                          ║
║  export           Export to JSON/CSV                             ║
║                                                                  ║
║  INSPECTION COMMANDS                                             ║
║  inspect          Detailed info about class/file                 ║
║                   Usage: inspect <class-or-file>                 ║
║  top              Top N classes by metric                        ║
║                   Usage: top <N> <methods|fields>                ║
║                                                                  ║
║  UTILITY                                                         ║
║  help             Show help                                      ║
╚══════════════════════════════════════════════════════════════════╝
```

---

**For architecture details and design rationale, see `README.md`**
