# Codebase Analyzer

A Java-based CLI tool to analyze Java codebases, built using custom container implementations.

## Features

- **Analyze**: Scans and indexes Java files in a directory.
- **List**: Lists classes, methods, and variables.
- **Find/Grep**: Search for identifiers or regex patterns.
- **Keywords**: Count and list top-N keywords.
- **Sort**: Sort files by keyword count or packages by class count.
- **Aggregate**: Show codebase statistics.
- **Metrics**: Detailed metrics for individual files.
- **Export**: Export results to JSON or CSV.
- **REPL**: Interactive mode with command history.

## Architecture

The project is built around custom container implementations:
- `GenericList`: Used for storing lists of classes, methods, etc. Supports functional operations (`map`, `filter`, `reduce`).
- `Queue`: Used for BFS directory scanning and storing grep matches.
- `Stack`: Used for REPL command history.
- `PriorityQueueCustom`: Used for sorting and top-N aggregation.

## Usage

### Prerequisites
- Java 11+
- Maven (wrapper included)

### Building and Running

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
  ```

- **List Classes**:
  ```bash
  ./run.sh list classes
  ```

- **Grep**:
  ```bash
  ./run.sh grep "pattern"
  ```

- **Keywords**:
  ```bash
  ./run.sh keywords
  ```

- **Sort by Keyword**:
  ```bash
  ./run.sh sort-by-keyword "TODO"
  ```

- **Aggregate Stats**:
  ```bash
  ./run.sh aggregate
  ```

- **Interactive Mode**:
  ```bash
  ./run.sh repl
  ```

## Development

- **Build**: `mvn clean package`
- **Test**: `mvn test`

## Deliverables

- `src/`: Source code.
- `run.sh` / `run.bat`: Execution scripts.
- `DEVLOG.md`: Development log.
- `report.pdf`: (To be generated from `report.md` or similar).
