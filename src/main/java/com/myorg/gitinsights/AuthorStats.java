package com.myorg.gitinsights;

/**
 * Statistics for a single author.
 */
public class AuthorStats implements Comparable<AuthorStats> {
    private final String author;
    private final int commitCount;

    public AuthorStats(String author, int commitCount) {
        this.author = author;
        this.commitCount = commitCount;
    }

    public String getAuthor() {
        return author;
    }

    public int getCommitCount() {
        return commitCount;
    }

    @Override
    public int compareTo(AuthorStats other) {
        // Sort by commit count descending
        return Integer.compare(other.commitCount, this.commitCount);
    }

    @Override
    public String toString() {
        return String.format("%-30s %5d", author, commitCount);
    }
}
