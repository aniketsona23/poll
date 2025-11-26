# GenAI Usage Report

## Overview
This document details the usage of Generative AI tools (ChatGPT, Claude, GitHub Copilot) during the development of the Codebase Analyzer. The AI was used primarily for:
1.  **Syntax verification**: Checking Java functional interface details.
2.  **Library exploration**: Understanding `JavaParser` API.
3.  **Algorithm optimization**: Refining the merge sort implementation for generic lists.
4.  **Regex crafting**: Creating robust patterns for the `grep` command.

---

## Session 1: Functional Interfaces in Java

**Context**: Implementing `map`, `filter`, and `reduce` in `GenericList`.

**Prompt**:
> "I need to implement a `map` function in a custom Java List class. What is the standard functional interface to use for transforming type T to type R? Also, show me the signature for `filter` using a Predicate."

**Response Summary**:
The AI suggested using `java.util.function.Function<T, R>` for mapping and `java.util.function.Predicate<T>` for filtering. It provided a code snippet showing how to define the methods:
```java
public <R> GenericList<R> map(Function<T, R> mapper)
public GenericList<T> filter(Predicate<T> predicate)
```

**Insight & Application**:
I adopted the standard interfaces instead of creating custom ones (like `Mapper<T,R>`), which makes the library compatible with Java's built-in lambda expressions. This was a crucial design decision for interoperability.

---

## Session 2: JavaParser API

**Context**: Need to extract method signatures and field names from Java files.

**Prompt**:
> "How do I use JavaParser to visit all method declarations in a file and get their signatures including return types and parameters? Give me a minimal visitor example."

**Response Summary**:
The AI provided an example using `VoidVisitorAdapter`. It showed how to override `visit(MethodDeclaration n, Void arg)` and extract details:
```java
n.getNameAsString()
n.getType()
n.getParameters()
```

**Insight & Application**:
The visitor pattern is the standard way to traverse the AST. I implemented a `ClassVisitor` extending `VoidVisitorAdapter` in my `Parser` class. This separated the traversal logic from the data extraction logic, keeping the code clean.

---

## Session 3: Custom Priority Queue

**Context**: Implementing a priority queue for the `top` command without using `java.util.PriorityQueue`.

**Prompt**:
> "Explain the bubble-up and bubble-down operations for a binary heap implementation in Java using an ArrayList. I need to support a custom Comparator."

**Response Summary**:
The AI explained the heap property (parent <= child for min-heap) and provided the index logic:
- Parent: `(i-1)/2`
- Left Child: `2*i + 1`
- Right Child: `2*i + 2`
It also gave pseudocode for `siftUp` and `siftDown` using a `Comparator`.

**Insight & Application**:
I implemented `PriorityQueueCustom` using `GenericList` as the backing storage. The `Comparator` strategy allows the same queue to be used for both "Top N Keywords" (max-heap by count) and "Sort by Name" (min-heap by string), simply by passing a different comparator.

---

## Session 4: Regex for Grep

**Context**: Implementing the `grep` command to find patterns.

**Prompt**:
> "Write a Java regex to find all occurrences of a word, but ignore it if it's inside a string literal or a comment. Actually, just a simple regex for now that matches a whole word."

**Response Summary**:
The AI suggested `\bword\b` for whole word matching. It warned that ignoring comments/strings with regex alone is complex and error-prone, suggesting AST parsing for that.

**Insight & Application**:
I decided to stick to standard regex matching for the `grep` command (line-based) as it's faster and standard for grep tools. For more semantic searching (like "find method named X"), I rely on the `Index` and `Parser` (AST-based) instead of regex. This distinction between text-search (`grep`) and code-search (`list methods`) became a core feature of the CLI.

---

## Conclusion
GenAI was instrumental in accelerating the "How" (syntax, API usage) while the "What" (architecture, class hierarchy, functional-OO design) was driven by the assignment requirements and my design choices. The code generated was always reviewed and often refactored to fit the custom `ListContainer` hierarchy.
