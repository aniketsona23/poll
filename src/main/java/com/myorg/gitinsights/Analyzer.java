package com.myorg.gitinsights;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.Comparator;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Analytics engine for commit data.
 * All methods use ONLY custom containers (MyList, PriorityQueueCustom).
 */
public class Analyzer {

    // ==================== TOP-K ANALYTICS ====================

    /**
     * Get top K authors by commit count.
     */
    public static MyList<AuthorStats> topAuthors(MyList<Commit> commits, int k) {
        // Count commits per author using MyList
        MyList<AuthorStats> allStats = new MyList<>();

        // Manual grouping (no HashMap allowed)
        for (int i = 0; i < commits.size(); i++) {
            String author = commits.get(i).getAuthor();
            boolean found = false;

            for (int j = 0; j < allStats.size(); j++) {
                if (allStats.get(j).getAuthor().equals(author)) {
                    // Increment count by replacing
                    AuthorStats old = allStats.get(j);
                    allStats.update(j, new AuthorStats(author, old.getCommitCount() + 1));
                    found = true;
                    break;
                }
            }

            if (!found) {
                allStats.add(new AuthorStats(author, 1));
            }
        }

        // Sort and take top k
        allStats.sort(null); // Uses Comparable implementation

        MyList<AuthorStats> result = new MyList<>();
        int limit = Math.min(k, allStats.size());
        for (int i = 0; i < limit; i++) {
            result.add(allStats.get(i));
        }

        return result;
    }

    /**
     * Get top K words from commit messages.
     */
    public static MyList<WordCount> topWords(MyList<Commit> commits, int k) {
        MyList<WordCount> allWords = new MyList<>();

        // Trivial words to filter
        MyList<String> stopWords = new MyList<>();
        String[] stops = { "the", "a", "an", "and", "or", "but", "in", "on", "at", "to", "for",
                "of", "with", "is", "was", "are", "be", "been", "have", "has", "had" };
        for (String s : stops)
            stopWords.add(s);

        // Process each commit message
        for (int i = 0; i < commits.size(); i++) {
            String message = commits.get(i).getMessage().toLowerCase();
            // Split on non-word characters
            String[] words = message.split("[\\s\\p{Punct}]+");

            for (String word : words) {
                word = word.trim();
                if (word.isEmpty() || word.length() < 3)
                    continue;

                // Check if stop word
                boolean isStopWord = false;
                for (int j = 0; j < stopWords.size(); j++) {
                    if (stopWords.get(j).equals(word)) {
                        isStopWord = true;
                        break;
                    }
                }
                if (isStopWord)
                    continue;

                // Count word
                boolean found = false;
                for (int j = 0; j < allWords.size(); j++) {
                    if (allWords.get(j).getWord().equals(word)) {
                        WordCount old = allWords.get(j);
                        allWords.update(j, new WordCount(word, old.getCount() + 1));
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    allWords.add(new WordCount(word, 1));
                }
            }
        }

        // Sort and take top k
        allWords.sort(null);

        MyList<WordCount> result = new MyList<>();
        int limit = Math.min(k, allWords.size());
        for (int i = 0; i < limit; i++) {
            result.add(allWords.get(i));
        }

        return result;
    }

    // ==================== TIME SERIES ANALYTICS ====================

    /**
     * Commits per day.
     */
    public static MyList<TimeSeriesStats> commitsPerDay(MyList<Commit> commits) {
        MyList<TimeSeriesStats> stats = new MyList<>();

        for (int i = 0; i < commits.size(); i++) {
            LocalDate day = commits.get(i).getDate().atZone(ZoneId.systemDefault()).toLocalDate();
            String dayStr = day.toString();

            boolean found = false;
            for (int j = 0; j < stats.size(); j++) {
                if (stats.get(j).getPeriod().equals(dayStr)) {
                    TimeSeriesStats old = stats.get(j);
                    stats.update(j, new TimeSeriesStats(dayStr, old.getCount() + 1));
                    found = true;
                    break;
                }
            }

            if (!found) {
                stats.add(new TimeSeriesStats(dayStr, 1));
            }
        }

        stats.sort(null);
        return stats;
    }

    /**
     * Commits per week.
     */
    public static MyList<TimeSeriesStats> commitsPerWeek(MyList<Commit> commits) {
        MyList<TimeSeriesStats> stats = new MyList<>();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        for (int i = 0; i < commits.size(); i++) {
            LocalDate day = commits.get(i).getDate().atZone(ZoneId.systemDefault()).toLocalDate();
            int year = day.getYear();
            int week = day.get(weekFields.weekOfWeekBasedYear());
            String weekStr = String.format("%04d-W%02d", year, week);

            boolean found = false;
            for (int j = 0; j < stats.size(); j++) {
                if (stats.get(j).getPeriod().equals(weekStr)) {
                    TimeSeriesStats old = stats.get(j);
                    stats.update(j, new TimeSeriesStats(weekStr, old.getCount() + 1));
                    found = true;
                    break;
                }
            }

            if (!found) {
                stats.add(new TimeSeriesStats(weekStr, 1));
            }
        }

        stats.sort(null);
        return stats;
    }

    /**
     * Commits per month.
     */
    public static MyList<TimeSeriesStats> commitsPerMonth(MyList<Commit> commits) {
        MyList<TimeSeriesStats> stats = new MyList<>();

        for (int i = 0; i < commits.size(); i++) {
            LocalDate day = commits.get(i).getDate().atZone(ZoneId.systemDefault()).toLocalDate();
            String monthStr = String.format("%04d-%02d", day.getYear(), day.getMonthValue());

            boolean found = false;
            for (int j = 0; j < stats.size(); j++) {
                if (stats.get(j).getPeriod().equals(monthStr)) {
                    TimeSeriesStats old = stats.get(j);
                    stats.update(j, new TimeSeriesStats(monthStr, old.getCount() + 1));
                    found = true;
                    break;
                }
            }

            if (!found) {
                stats.add(new TimeSeriesStats(monthStr, 1));
            }
        }

        stats.sort(null);
        return stats;
    }

    // ==================== FILTERING ====================

    /**
     * Filter commits by author name (exact or partial match).
     */
    public static MyList<Commit> filterByAuthor(MyList<Commit> commits, String authorName) {
        return commits.filter(c -> c.getAuthor().toLowerCase().contains(authorName.toLowerCase()));
    }

    /**
     * Filter commits by message substring.
     */
    public static MyList<Commit> filterByMessage(MyList<Commit> commits, String text) {
        return commits.filter(c -> c.getMessage().toLowerCase().contains(text.toLowerCase()));
    }

    /**
     * Filter commits by message regex.
     */
    public static MyList<Commit> filterByMessageRegex(MyList<Commit> commits, String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        return commits.filter(c -> pattern.matcher(c.getMessage()).find());
    }

    /**
     * Filter commits by date range (inclusive).
     */
    public static MyList<Commit> filterByDateRange(MyList<Commit> commits, LocalDate startDate, LocalDate endDate) {
        Instant start = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant end = endDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
        return commits.filter(c -> !c.getDate().isBefore(start) && c.getDate().isBefore(end));
    }

    /**
     * Filter commits before a date.
     */
    public static MyList<Commit> filterBefore(MyList<Commit> commits, LocalDate date) {
        Instant instant = date.atStartOfDay(ZoneId.systemDefault()).toInstant();
        return commits.filter(c -> c.getDate().isBefore(instant));
    }

    /**
     * Filter commits after a date.
     */
    public static MyList<Commit> filterAfter(MyList<Commit> commits, LocalDate date) {
        Instant instant = date.atStartOfDay(ZoneId.systemDefault()).toInstant();
        return commits.filter(c -> c.getDate().isAfter(instant));
    }

    /**
     * Search commits by hash prefix.
     */
    public static MyList<Commit> searchByHash(MyList<Commit> commits, String hashPrefix) {
        return commits.filter(c -> c.getHash().toLowerCase().startsWith(hashPrefix.toLowerCase()));
    }

    // ==================== SORTING ====================

    /**
     * Sort by date ascending.
     */
    public static MyList<Commit> sortByDateAsc(MyList<Commit> commits) {
        MyList<Commit> copy = copyList(commits);
        copy.sort(Comparator.comparing(Commit::getDate));
        return copy;
    }

    /**
     * Sort by date descending.
     */
    public static MyList<Commit> sortByDateDesc(MyList<Commit> commits) {
        MyList<Commit> copy = copyList(commits);
        copy.sort(Comparator.comparing(Commit::getDate).reversed());
        return copy;
    }

    /**
     * Sort by author name.
     */
    public static MyList<Commit> sortByAuthor(MyList<Commit> commits) {
        MyList<Commit> copy = copyList(commits);
        copy.sort(Comparator.comparing(Commit::getAuthor));
        return copy;
    }

    /**
     * Sort by message length.
     */
    public static MyList<Commit> sortByMessageLength(MyList<Commit> commits) {
        MyList<Commit> copy = copyList(commits);
        copy.sort(Comparator.comparingInt(c -> c.getMessage().length()));
        return copy;
    }

    /**
     * Sort by hash.
     */
    public static MyList<Commit> sortByHash(MyList<Commit> commits) {
        MyList<Commit> copy = copyList(commits);
        copy.sort(Comparator.comparing(Commit::getHash));
        return copy;
    }

    // ==================== AGGREGATIONS ====================

    /**
     * Count total commits.
     */
    public static int countCommits(MyList<Commit> commits) {
        return commits.size();
    }

    /**
     * Count unique authors.
     */
    public static int countAuthors(MyList<Commit> commits) {
        MyList<String> uniqueAuthors = new MyList<>();
        for (int i = 0; i < commits.size(); i++) {
            String author = commits.get(i).getAuthor();
            if (!uniqueAuthors.contains(author)) {
                uniqueAuthors.add(author);
            }
        }
        return uniqueAuthors.size();
    }

    /**
     * Average message length.
     */
    public static double avgMessageLength(MyList<Commit> commits) {
        if (commits.isEmpty())
            return 0.0;

        int total = commits.reduce(0, (sum, c) -> sum + c.getMessage().length());
        return (double) total / commits.size();
    }

    /**
     * Calculate inversion count (uses functional merge-sort based algorithm).
     */
    public static long inversionCount(MyList<Commit> commits) {
        // Create a copy and compute inversions based on date ordering
        MyList<Commit> copy = copyList(commits);
        return copy.inversionCount();
    }

    // ==================== UTILITIES ====================

    /**
     * Copy a list.
     */
    private static MyList<Commit> copyList(MyList<Commit> commits) {
        MyList<Commit> copy = new MyList<>();
        for (int i = 0; i < commits.size(); i++) {
            copy.add(commits.get(i));
        }
        return copy;
    }
}
