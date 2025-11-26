# POPL Assignment Submission: Codebase Analyzer

**Team**: Aniket Sonawane (2022B3A70031G), Vanshaj Bhudolia (2022B3A70972G), Vedant Kamath (2022B4A70073G), Mayukh Saha (2022B1A71033G)

## Project Overview
This project is a **Codebase Analyzer** CLI tool designed to analyze Java source code. It allows users to scan directories, list classes and methods, search for patterns, and generate statistics about their code. The core goal was to demonstrate **Functional Object-Oriented Programming** by building custom data structures from scratch and using them to solve real-world problems.

## The Functional-OO Approach
We adopted a hybrid approach that leverages the best of both paradigms:

-   **Object-Oriented (The Structure)**: We used a strong class hierarchy for our containers. A base `ListContainer` defines the common behavior, while specific implementations like `Stack`, `Queue`, and `GenericList` extend it. This ensures code reuse and encapsulation.
-   **Functional (The Behavior)**: Instead of writing loops for every operation, we implemented functional methods like `map`, `filter`, and `reduce` in our `GenericList`. This allows us to write declarative code. For example, to find all public methods, we simply `filter` the list of methods rather than iterating and checking conditions manually.

## Custom Containers: How We Used Them
We didn't use standard Java collections (like `ArrayList` or `LinkedList`) for our core logic. Instead, we built and used our own:

1.  **GenericList**: Our "Swiss Army Knife". It stores the indexed data (classes, methods). We use it whenever we need a list that we can transform or filter.
2.  **Queue**: Used in our **Scanner**. When we scan a directory, we find a folder, put it in the Queue, and then process it later. This allows us to traverse deep directory structures without recursion (Breadth-First Search).
3.  **Stack**: Used in our **REPL (Interactive Mode)**. Every time you type a command, we push it onto the Stack. This gives us a history feature where we can look back at what was executed.
4.  **PriorityQueue**: Used for **Sorting and Top-N** commands. When you ask for the "Top 10 Keywords", we throw all keywords into this queue, and it automatically bubbles the most frequent ones to the top.

## The "What" vs "How" Design
We strictly separated the *intent* from the *implementation*:

-   **The "What" (Commands)**: The CLI commands (like `AnalyzeCommand`, `GrepCommand`) only care about *what* the user wants to do. They ask the `Index` for data and the `GenericList` to filter it. They don't know *how* the data is stored or sorted.
-   **The "How" (Core & Containers)**: The `Scanner` knows *how* to walk the file system. The `Parser` knows *how* to read Java syntax. The `PriorityQueue` knows *how* to maintain heap order. The Commands just use these tools.

## Implemented Commands
-   `analyze`: Scans and indexes the codebase.
-   `list`: Shows classes, methods, and variables.
-   `grep`: Searches for patterns.
-   `keywords`: Counts keyword frequency.
-   `sort-by-keyword`: Finds files with the most occurrences of a keyword.
-   `sort-by-class-count`: Finds packages with the most classes.
-   `aggregate`: Displays overall project statistics.
-   `metrics`: Detailed analysis of a single file.
-   `top`: Lists the top N classes by method or field count.
-   `export`: Saves analysis data to JSON/CSV.

## Project Documentation Links
Here are the detailed documents for this submission:

-   **[User Guide & Commands](COMMANDS.md)**: Complete reference for all CLI commands.
-   **[Run Guide](docs/Run_Guide.md)**: Instructions for building and running the project.
-   **[Development Timeline](docs/Timeline.md)**: A chronological log of our 2-day implementation sprint.
-   **[GenAI Usage Report](docs/GenAI_Usage.md)**: How we used AI tools to assist development.
