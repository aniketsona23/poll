package com.myorg.gitinsights;

/**
 * Word count statistics.
 */
public class WordCount implements Comparable<WordCount> {
    private final String word;
    private final int count;

    public WordCount(String word, int count) {
        this.word = word;
        this.count = count;
    }

    public String getWord() {
        return word;
    }

    public int getCount() {
        return count;
    }

    @Override
    public int compareTo(WordCount other) {
        // Sort by count descending
        return Integer.compare(other.count, this.count);
    }

    @Override
    public String toString() {
        return String.format("%-20s %5d", word, count);
    }
}
