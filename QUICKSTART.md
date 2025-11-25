# Git Commit Log Analyzer - Quick Reference

## 🎯 What Was Built

A comprehensive Git Commit Log Analyzer demonstrating **Functional-OO programming** using **custom containers only** (no Java collections).

## 📁 Files Created

### Core Implementation (9 files)
1. `MyList.java` - Extended list with map/filter/reduce/forEach/inversionCount
2. `Commit.java` - Immutable commit data model
3. `GitLogReader.java` - File I/O and git log parsing
4. `Analyzer.java` - Analytics engine with 20+ methods
5. `CommandHandler.java` - Command parser and router
6. `Main.java` - CLI entry point
7. `AuthorStats.java` - Author statistics helper
8. `WordCount.java` - Word frequency helper
9. `TimeSeriesStats.java` - Time-series data helper

### Documentation & Test Data
- `README.md` - Comprehensive project documentation
- `commits.txt` - Sample data with 20 commits from 4 authors
- `walkthrough.md` - Implementation walkthrough
- `task.md` - Task checklist (all completed ✅)

## ⚡ Quick Start

```bash
# Compile
mkdir bin
javac -d bin src\main\java\com\containers\*.java src\main\java\com\myorg\gitinsights\*.java

# Run
java -cp bin com.myorg.gitinsights.Main

# Inside the program
git-insights> load commits.txt
git-insights> help
```

## 🌟 Key Features Implemented

### ✅ 30+ CLI Commands

**Loading**: load, reload, save, export-csv  
**Filtering**: filter-author, filter-message, filter-message-regex, filter-date-range, filter-before, filter-after, reset-filters  
**Sorting**: sort-date-asc, sort-date-desc, sort-author, sort-message-length, sort-hash  
**Analytics**: top-authors, top-words, commits-per-day, commits-per-week, commits-per-month, count, count-authors, avg-message-length, inversion-count  
**Search**: search-message, search-author, search-hash  
**Utility**: stats, help, exit

### ✅ Functional Operations

```java
// Map: transform each element
MyList<String> authors = commits.map(Commit::getAuthor);

// Filter: select matching elements
MyList<Commit> fixes = commits.filter(c -> c.getMessage().contains("fix"));

// Reduce: aggregate to single value
int total = commits.reduce(0, (sum, c) -> sum + c.getMessage().length());

// ForEach: perform action on each
commits.forEach(c -> System.out.println(c));
```

### ✅ Custom Containers Only

- **MyList<T>** for all data storage
- **PriorityQueueCustom<T>** available for heap operations
- **No ArrayList, HashMap, or any Java collections**
- Manual grouping/aggregation algorithms

### ✅ Advanced Algorithms

- Merge sort (stable, O(n log n))
- Inversion counting (functional merge-sort based)
- Top-K selection
- Time-series aggregation
- Word frequency analysis with stop-words

## 📊 Test Results

All tests passed successfully:

```
✅ Compilation: No errors
✅ Load 20 commits: Success
✅ Statistics calculation: Correct (4 authors, 143.60 avg length)
✅ Top authors: Ranked correctly (Alice: 8, Bob: 5, Charlie: 4)
✅ Top words: Frequency correct (now: 8, add: 7, user: 6)
✅ Filtering: Working (Alice filtered to 8 commits)
✅ Time series: Aggregated correctly (Jan: 13, Feb: 7)
```

## 🎓 Demonstrates

1. **Functional Programming**: map, filter, reduce, lambdas
2. **Object-Oriented Design**: Inheritance, encapsulation, polymorphism
3. **Custom Data Structures**: Extended existing containers
4. **Algorithms**: Merge sort, inversion count
5. **Software Patterns**: Command pattern, immutability
6. **Java Features**: Generics, functional interfaces, lambdas

## 📐 Architecture

```
┌─────────────────────────────────────┐
│           Main (CLI)                │
└─────────────┬───────────────────────┘
              │
┌─────────────▼───────────────────────┐
│       CommandHandler                │
│  (Parse & Route Commands)           │
└─────────────┬───────────────────────┘
              │
┌─────────────▼───────────────────────┐
│          Analyzer                   │
│  (Analytics & Transformations)      │
└─────────────┬───────────────────────┘
              │
┌─────────────▼───────────────────────┐
│  MyList<Commit> (Functional Ops)    │
│  map | filter | reduce | forEach    │
└─────────────┬───────────────────────┘
              │
┌─────────────▼───────────────────────┐
│      GenericList<T>                 │
│  (Custom Container - Merge Sort)    │
└─────────────────────────────────────┘
```

## 💡 Usage Examples

### Example 1: Analyze Author Activity
```bash
load commits.txt
top-authors 5
commits-per-month
```

### Example 2: Find Security Fixes
```bash
load commits.txt
filter-message-regex security|vulnerability|cve
count
```

### Example 3: Alice's January Work
```bash
load commits.txt
filter-author Alice
filter-date-range 2024-01-01 2024-01-31
save alice_january.txt
```

### Example 4: Export Recent Commits
```bash
load commits.txt
filter-after 2024-02-01
sort-date-desc
export-csv recent_commits.csv
```

## 🔧 Technical Highlights

### Zero External Dependencies
- No Maven/Gradle
- No external libraries
- Pure Java + custom containers
- Compiles with `javac`
- Runs with `java`

### Functional-OO Hybrid
- OOP structure (classes, inheritance)
- Functional operations (higher-order functions)
- Lambda expressions throughout
- Immutable data models

### Performance
- Linear filtering: O(n)
- Merge sort: O(n log n)
- Manual grouping: O(n²) but acceptable for typical datasets
- Memory efficient (no duplicate storage)

## 📝 Code Statistics

- **Total Java Files**: 9 classes
- **Total Lines**: ~1500 LOC
- **Methods**: 50+ public methods
- **Commands**: 30+ CLI commands
- **Test Data**: 20 sample commits

## 🚀 Next Steps (Optional Extensions)

If you want to extend this project:

1. Add support for file change statistics (`++`, `--` lines)
2. Implement graph visualization (commit timeline)
3. Add more regex patterns for commit message analysis
4. Create batch command files (.batch)
5. Add unit tests using JUnit (optional)
6. Implement custom HashMap for O(1) grouping

## ✅ Constraints Satisfied

✅ Uses ONLY custom containers (MyList, PriorityQueueCustom)  
✅ No external libraries  
✅ Functional operations (map, filter, reduce)  
✅ Compiles with plain javac  
✅ CLI-based (no GUI)  
✅ Full working implementation  
✅ Demonstrates Functional-OO paradigm  

---

**Status**: ✅ COMPLETE - All requirements met, fully tested and verified.
