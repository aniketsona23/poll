package com.myorg.gitinsights;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/**
 * Reads and parses git log file into MyList of Commit objects.
 * Expected format from: git log
 * --pretty=format:"==COMMIT==%n%H%n%an%n%ad%n%B%n==END=="
 */
public class GitLogReader {

    /**
     * Read commits from file and return MyList<Commit>.
     */
    public static MyList<Commit> readCommits(String filePath) throws IOException {
        MyList<Commit> commits = new MyList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String hash = null;
            String author = null;
            Instant date = null;
            StringBuilder message = new StringBuilder();
            boolean inCommit = false;
            int fieldIndex = 0;

            while ((line = reader.readLine()) != null) {
                if (line.trim().equals("==COMMIT==")) {
                    inCommit = true;
                    fieldIndex = 0;
                    hash = null;
                    author = null;
                    date = null;
                    message.setLength(0);
                } else if (line.trim().equals("==END==")) {
                    if (inCommit && hash != null && author != null && date != null) {
                        commits.add(new Commit(hash, author, date, message.toString().trim()));
                    }
                    inCommit = false;
                } else if (inCommit) {
                    if (fieldIndex == 0) {
                        // Hash
                        hash = line.trim();
                        fieldIndex++;
                    } else if (fieldIndex == 1) {
                        // Author
                        author = line.trim();
                        fieldIndex++;
                    } else if (fieldIndex == 2) {
                        // Date
                        date = parseDate(line.trim());
                        fieldIndex++;
                    } else {
                        // Message (can be multiple lines)
                        if (message.length() > 0) {
                            message.append("\n");
                        }
                        message.append(line);
                    }
                }
            }
        }

        return commits;
    }

    /**
     * Parse various git date formats to Instant.
     */
    private static Instant parseDate(String dateStr) {
        try {
            // Try common git date formats
            // Format: "Mon Jan 15 14:23:45 2024 +0530"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM d HH:mm:ss yyyy Z", Locale.ENGLISH);
            return ZonedDateTime.parse(dateStr, formatter).toInstant();
        } catch (DateTimeParseException e1) {
            try {
                // ISO format: "2024-01-15T14:23:45+05:30"
                return Instant.parse(dateStr);
            } catch (DateTimeParseException e2) {
                try {
                    // Alternative format: "2024-01-15 14:23:45 +0530"
                    DateTimeFormatter altFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z",
                            Locale.ENGLISH);
                    return ZonedDateTime.parse(dateStr, altFormatter).toInstant();
                } catch (DateTimeParseException e3) {
                    // Fallback to current time if parsing fails
                    System.err.println("Warning: Could not parse date: " + dateStr + ", using current time");
                    return Instant.now();
                }
            }
        }
    }

    /**
     * Save commits to file.
     */
    public static void saveCommits(MyList<Commit> commits, String filePath) throws IOException {
        try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(filePath))) {
            for (int i = 0; i < commits.size(); i++) {
                Commit c = commits.get(i);
                writer.println("==COMMIT==");
                writer.println(c.getHash());
                writer.println(c.getAuthor());
                writer.println(c.getDate().toString());
                writer.println(c.getMessage());
                writer.println("==END==");
            }
        }
    }

    /**
     * Export commits to CSV format.
     */
    public static void exportToCSV(MyList<Commit> commits, String filePath) throws IOException {
        try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(filePath))) {
            writer.println("Hash,Author,Date,Message");
            for (int i = 0; i < commits.size(); i++) {
                Commit c = commits.get(i);
                String msg = c.getMessage().replace("\"", "\"\"").replace("\n", " ");
                writer.printf("\"%s\",\"%s\",\"%s\",\"%s\"%n",
                        c.getHash(), c.getAuthor(), c.getFormattedDate(), msg);
            }
        }
    }
}
