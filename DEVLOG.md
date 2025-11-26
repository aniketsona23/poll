# Development Log

## 2025-11-25

### 21:51 - Project Start
- Analyzed requirements: Java-based CLI, custom containers usage, functional programming style.
- Created `task.md` and `implementation_plan.md`.

### 21:56 - Plan Update
- User requested explicit usage of `PriorityQueue`, `Queue`, `Stack`.
- Updated plan to include:
    - `Queue` for `Scanner` (BFS) and `GrepCommand`.
    - `Stack` for `ReplCommand` history.
    - `PriorityQueueCustom` for `KeywordsCommand`, `SortCommand`, `AggregateCommand`.

### 22:06 - Execution Start
- Created `pom.xml` with dependencies: `javaparser-core`, `gson`, `junit-jupiter`.
- Created `run.sh` and `run.bat`.
- Modified `GenericList` to add `map`, `filter`, `reduce`.

### 22:15 - Core Architecture
- Implemented `Scanner` using `Queue` for BFS directory traversal.
- Implemented `ClassInfo`, `MethodInfo`, `FieldInfo` models using `GenericList`.
- Implemented `Parser` using `JavaParser`, wrapping results in `GenericList`.
- Implemented `Index` to hold analysis results.

### 22:25 - CLI Implementation
- Created `Main` entry point with command dispatch.
- Implemented `AnalyzeCommand` and `ListCommand`.
- Implemented `GrepCommand` using `Queue` to store matches.
- Implemented `KeywordsCommand` using `PriorityQueueCustom` for top-N keywords.
- Implemented `ReplCommand` using `Stack` for command history.
- Implemented `SortCommand` and `AggregateCommand` using `PriorityQueueCustom`.
- Implemented `MetricsCommand` and `ExportCommand`.

### 22:40 - Verification
- Created `testdata` directory with sample Java files.
- Verified CLI commands against `testdata`.
- Added `ContainerUsageTest` to assert strict usage of custom containers in public APIs.

## LLM Prompts & Insights

**Prompt:** "Implement a Java-based 'Programming Codebase Analyzer' CLI + library... reuse and rely on the student's custom containers... Do not use java.util collections for main data storage".

**Insight:** The core challenge was to bridge the gap between `JavaParser` (which returns `java.util.List`) and the requirement to use custom containers.
**Solution:** Created `Parser` logic that immediately iterates over JavaParser results and populates `GenericList` and other custom containers, ensuring that the internal domain model (`ClassInfo`, etc.) only exposes custom containers.

**Prompt:** "I want you to use priorityQueue, queue, stack etc, anyway".

**Insight:** The user wanted to ensure all custom container types were utilized.
**Solution:**
- `Queue`: Used for BFS in `Scanner` and storing matches in `GrepCommand`.
- `Stack`: Used for REPL history.
- `PriorityQueueCustom`: Used for all sorting and top-N aggregation tasks.
