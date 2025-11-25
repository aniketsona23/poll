package com.myorg.gitinsights;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Represents a single git commit with essential metadata.
 */
public class Commit {
    private final String hash;
    private final String author;
    private final Instant date;
    private final String message;

    public Commit(String hash, String author, Instant date, String message) {
        this.hash = Objects.requireNonNull(hash, "hash cannot be null");
        this.author = Objects.requireNonNull(author, "author cannot be null");
        this.date = Objects.requireNonNull(date, "date cannot be null");
        this.message = Objects.requireNonNull(message, "message cannot be null");
    }

    public String getHash() {
        return hash;
    }

    public String getAuthor() {
        return author;
    }

    public Instant getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    /**
     * Get formatted date for display.
     */
    public String getFormattedDate() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault())
                .format(date);
    }

    /**
     * Get short hash (first 8 characters).
     */
    public String getShortHash() {
        return hash.length() > 8 ? hash.substring(0, 8) : hash;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s: %s",
                getShortHash(), getFormattedDate(), author,
                message.length() > 50 ? message.substring(0, 50) + "..." : message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Commit commit = (Commit) o;
        return hash.equals(commit.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hash);
    }
}
