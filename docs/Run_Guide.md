# Run Guide

## Prerequisites
- **Java 11** or higher
- **Maven** (optional, wrapper included)
- **Make** (optional, for using Makefile)

## Quick Start

> [!IMPORTANT]
> **Linux/Mac is the prioritized platform** for this assignment. While Windows is supported via `run.bat`, we strongly recommend using a Unix-based environment (Linux, macOS, or WSL) for the best experience and to match the evaluation environment.

### 1. Build the Project
Open a terminal in the project root and run:

**Linux/Mac (Recommended):**
```bash
./run.sh build
```

**Windows:**
```bat
.\run.bat build
```

*(Or if you have Make installed: `make build`)*

### 2. Run the Interactive REPL
This is the best way to explore the tool.

**Linux/Mac (Recommended):**
```bash
./run.sh repl
```

**Windows:**
```bat
.\run.bat repl
```

*(Or `make repl`)*

Inside the REPL, type `help` to see commands. Example session:
```
> analyze --path ./src
> list classes
> keywords --top 5
> exit
```

## Single Command Usage

You can also run commands directly without entering the REPL.

**Analyze a directory:**
```bash
# Windows
.\run.bat analyze --path .\src

# Linux/Mac
./run.sh analyze --path ./src
```

**List all methods:**
```bash
.\run.bat list methods
```

**Search for a pattern (Grep):**
```bash
.\run.bat grep "ArrayList"
```

**Get Top 10 Keywords:**
```bash
.\run.bat keywords --top 10
```

**Export Results:**
```bash
.\run.bat export --format json --output results.json
```

## Troubleshooting

- **"Permission denied"**: Run `chmod +x run.sh` on Linux/Mac.
- **"Java not found"**: Ensure `JAVA_HOME` is set or `java` is in your PATH.
- **Build fails**: Try cleaning first with `.\run.bat clean` or `make clean`.
