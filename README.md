# Git Commit Log Analyzer

A powerful command-line tool for analyzing Git commit logs using **Functional-OO programming** and **custom containers**.

## 🎯 Overview

This project demonstrates the integration of functional programming concepts (map, filter, reduce) with object-oriented design, built entirely on custom container implementations (no external collections library usage).

## ✨ Features

### 📊 Analytics
- **Top Authors**: Identify most active contributors
- **Word Frequency**: Analyze commit message patterns
- **Time Series**: Track commits per day/week/month
- **Statistics**: Comprehensive dataset insights

### 🔍 Filtering & Search
- Filter by author, date range, message content
- Regex-based message search
- Hash prefix lookup
- Cumulative filtering support

### 📝 Sorting
- Sort by date, author, message length, or hash
- Stable merge-sort implementation

### 💾 Data Management
- Load/reload commit files
- Save filtered results
- Export to CSV format

## 🚀 Quick Start

### Using Build Scripts (Recommended)

**Windows:**
```bash
# Compile
build.bat compile

# Run the analyzer
build.bat analyzer

# Clean build artifacts
build.bat clean

# Show help
build.bat help
```

**Linux/Mac (with Make):**
```bash
# Compile
make compile

# Run the analyzer
make analyzer

# Run Maven tests
make test-all

# Clean
make clean

# Show help
make help
```

### Manual Compilation

```bash
mkdir bin
javac -d bin src\main\java\com\containers\*.java src\main\java\com\myorg\gitinsights\*.java
```

### Running

```bash
java -cp bin com.myorg.gitinsights.Main
```

### Generate Sample Data

Generate a git log file from any repository:

```bash
git log --pretty=format:"==COMMIT==%n%H%n%an%n%ad%n%B%n==END==" > commits.txt
```

## 📖 Usage Examples

### Basic Analysis

```bash
git-insights> load commits.txt
Loaded 20 commits from commits.txt

git-insights> stats
=== Dataset Statistics ===
Total commits loaded: 20
Working set size: 20
Unique authors: 4
Average message length: 143.60
Date range: 2024-01-15 10:23:45 to 2024-02-09 09:48:28

git-insights> top-authors 3
Top 3 Authors:
Rank  Author                         Commits
--------------------------------------------------
1     Alice Johnson                      8
2     Bob Smith                          5
3     Charlie Brown                      4
```

### Filtering

```bash
git-insights> filter-author Alice
Filtered to 8 commits by author matching 'Alice'

git-insights> filter-message fix
Filtered to 3 commits with message containing 'fix'

git-insights> reset-filters
Reset filters. Working set now has 20 commits
```

### Time Analysis

```bash
git-insights> commits-per-month
Commits Per Month:
Period          Count
-------------------------
2024-01            13
2024-02             7
```

## 🛠️ Available Commands

### Loading & Saving
- `load <file>` - Load commits from file
- `reload` - Reload last file
- `save <file>` - Save current working set
- `export-csv <file>` - Export to CSV

### Filtering (cumulative)
- `filter-author <name>` - Filter by author
- `filter-message <text>` - Filter by message content
- `filter-message-regex <pattern>` - Filter by regex
- `filter-date-range <start> <end>` - Filter by date range (YYYY-MM-DD)
- `filter-before <date>` - Filter commits before date
- `filter-after <date>` - Filter commits after date
- `reset-filters` - Reset to all commits

### Sorting
- `sort-date-asc` - Sort by date ascending
- `sort-date-desc` - Sort by date descending
- `sort-author` - Sort by author name
- `sort-message-length` - Sort by message length
- `sort-hash` - Sort by hash

### Analytics
- `top-authors <n>` - Top N authors by commit count
- `top-words <n>` - Top N words in messages
- `commits-per-day` - Commits grouped by day
- `commits-per-week` - Commits grouped by week
- `commits-per-month` - Commits grouped by month
- `count` - Count commits
- `count-authors` - Count unique authors
- `avg-message-length` - Average message length
- `inversion-count` - Calculate inversions

### Search
- `search-message <keyword>` - Search in messages
- `search-author <partial>` - Search authors
- `search-hash <prefix>` - Search by hash prefix

### Utility
- `stats` - Show dataset statistics
- `help` - Show this help
- `exit` - Exit program

## 🏗️ Architecture

### Custom Containers Used

All data storage uses the existing custom containers from `com.containers`:

- **GenericList<T>**: Array-backed list with merge sort
- **PriorityQueueCustom<T>**: Binary heap implementation
- **MyList<T>**: Extended list with functional operations

### Functional Operations

```java
// Map
MyList<String> authors = commits.map(Commit::getAuthor);

// Filter
MyList<Commit> fixes = commits.filter(c -> 
    c.getMessage().contains("fix"));

// Reduce
int totalLength = commits.reduce(0, 
    (sum, c) -> sum + c.getMessage().length());

// ForEach
commits.forEach(c -> System.out.println(c.getAuthor()));
```

## 📋 Design Principles

1. **Custom Containers Only**: No use of Java collections (ArrayList, HashMap, etc.) in core logic
2. **Functional-OO Hybrid**: Combines OOP structure with functional operations
3. **Zero External Dependencies**: Pure Java + custom containers
4. **Immutable Data**: Commit objects are immutable
5. **Type Safety**: Full generic type support

## 🧪 Testing

A sample `commits.txt` file with 20 commits is included. To test:

```bash
java -cp bin com.myorg.gitinsights.Main
git-insights> load commits.txt
git-insights> help
```

## 📦 Project Structure

```
src/main/java/
├── com/containers/
│   ├── ListContainer.java        # Abstract base
│   ├── GenericList.java          # Generic list implementation
│   └── PriorityQueueCustom.java  # Priority queue
└── com/myorg/gitinsights/
    ├── MyList.java               # Extended list with functional ops
    ├── Commit.java               # Commit data model
    ├── GitLogReader.java         # File I/O and parsing
    ├── Analyzer.java             # Analytics engine
    ├── CommandHandler.java       # Command parsing
    ├── Main.java                 # CLI entry point
    ├── AuthorStats.java          # Author statistics
    ├── WordCount.java            # Word frequency
    └── TimeSeriesStats.java      # Time-series data
```

## 🎓 Educational Value

This project demonstrates:

- **Functional Programming**: Higher-order functions (map, filter, reduce)
- **Object-Oriented Design**: Clean separation of concerns
- **Data Structures**: Custom container implementations
- **Algorithms**: Merge sort, inversion counting
- **Software Engineering**: Command pattern, immutability
- **Java Features**: Generics, lambdas, streams (concept), functional interfaces

## 📝 Requirements

- Java 8 or higher (for lambda support)
- No external dependencies
- No Maven or Gradle required

## 🤝 Contributing

This is an educational project demonstrating Functional-OO programming with custom containers.

## 📄 License

Educational project for Programming Languages (PoPL) course.

## 👨‍💻 Author

Created as part of the PoPL Quiz assignment, demonstrating functional-OO programming concepts.

---

**Note**: This project intentionally avoids using Java's built-in collections framework to showcase custom container implementations and functional programming patterns.
