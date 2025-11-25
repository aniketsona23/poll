package com.myorg.gitinsights;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Handles command parsing and execution.
 */
public class CommandHandler {
    private MyList<Commit> allCommits;
    private MyList<Commit> workingCommits;
    private String lastFilePath;

    public CommandHandler() {
        this.allCommits = new MyList<>();
        this.workingCommits = new MyList<>();
        this.lastFilePath = null;
    }

    /**
     * Execute a command and return result message.
     */
    public String executeCommand(String commandLine) {
        if (commandLine == null || commandLine.trim().isEmpty()) {
            return "";
        }

        String[] parts = commandLine.trim().split("\\s+", 2);
        String command = parts[0].toLowerCase();
        String args = parts.length > 1 ? parts[1] : "";

        try {
            switch (command) {
                // Loading & Saving
                case "load":
                    return cmdLoad(args);
                case "reload":
                    return cmdReload();
                case "save":
                    return cmdSave(args);
                case "export-csv":
                    return cmdExportCSV(args);

                // Filtering
                case "filter-author":
                    return cmdFilterAuthor(args);
                case "filter-message":
                    return cmdFilterMessage(args);
                case "filter-message-regex":
                    return cmdFilterMessageRegex(args);
                case "filter-date-range":
                    return cmdFilterDateRange(args);
                case "filter-before":
                    return cmdFilterBefore(args);
                case "filter-after":
                    return cmdFilterAfter(args);
                case "reset-filters":
                    return cmdResetFilters();

                // Sorting
                case "sort-date-asc":
                    return cmdSortDateAsc();
                case "sort-date-desc":
                    return cmdSortDateDesc();
                case "sort-author":
                    return cmdSortAuthor();
                case "sort-message-length":
                    return cmdSortMessageLength();
                case "sort-hash":
                    return cmdSortHash();

                // Analytics
                case "top-authors":
                    return cmdTopAuthors(args);
                case "top-words":
                    return cmdTopWords(args);
                case "commits-per-day":
                    return cmdCommitsPerDay();
                case "commits-per-week":
                    return cmdCommitsPerWeek();
                case "commits-per-month":
                    return cmdCommitsPerMonth();
                case "count":
                    return cmdCount();
                case "count-authors":
                    return cmdCountAuthors();
                case "avg-message-length":
                    return cmdAvgMessageLength();
                case "inversion-count":
                    return cmdInversionCount();

                // Search
                case "search-message":
                    return cmdSearchMessage(args);
                case "search-author":
                    return cmdSearchAuthor(args);
                case "search-hash":
                    return cmdSearchHash(args);

                // Utility
                case "stats":
                    return cmdStats();
                case "help":
                    return cmdHelp();
                case "exit":
                case "quit":
                    return "EXIT";

                default:
                    return "Unknown command: " + command + ". Type 'help' for available commands.";
            }
        } catch (Exception e) {
            return "Error executing command: " + e.getMessage();
        }
    }

    // ==================== LOADING & SAVING ====================

    private String cmdLoad(String filePath) {
        if (filePath.isEmpty()) {
            return "Usage: load <file-path>";
        }
        try {
            allCommits = GitLogReader.readCommits(filePath);
            workingCommits = copyList(allCommits);
            lastFilePath = filePath;
            return String.format("Loaded %d commits from %s", allCommits.size(), filePath);
        } catch (IOException e) {
            return "Error loading file: " + e.getMessage();
        }
    }

    private String cmdReload() {
        if (lastFilePath == null) {
            return "No file to reload. Use 'load <file>' first.";
        }
        return cmdLoad(lastFilePath);
    }

    private String cmdSave(String filePath) {
        if (filePath.isEmpty()) {
            return "Usage: save <file-path>";
        }
        try {
            GitLogReader.saveCommits(workingCommits, filePath);
            return String.format("Saved %d commits to %s", workingCommits.size(), filePath);
        } catch (IOException e) {
            return "Error saving file: " + e.getMessage();
        }
    }

    private String cmdExportCSV(String filePath) {
        if (filePath.isEmpty()) {
            return "Usage: export-csv <file-path>";
        }
        try {
            GitLogReader.exportToCSV(workingCommits, filePath);
            return String.format("Exported %d commits to %s", workingCommits.size(), filePath);
        } catch (IOException e) {
            return "Error exporting CSV: " + e.getMessage();
        }
    }

    // ==================== FILTERING ====================

    private String cmdFilterAuthor(String author) {
        if (author.isEmpty()) {
            return "Usage: filter-author <author-name>";
        }
        workingCommits = Analyzer.filterByAuthor(workingCommits, author);
        return String.format("Filtered to %d commits by author matching '%s'", workingCommits.size(), author);
    }

    private String cmdFilterMessage(String text) {
        if (text.isEmpty()) {
            return "Usage: filter-message <text>";
        }
        workingCommits = Analyzer.filterByMessage(workingCommits, text);
        return String.format("Filtered to %d commits with message containing '%s'", workingCommits.size(), text);
    }

    private String cmdFilterMessageRegex(String regex) {
        if (regex.isEmpty()) {
            return "Usage: filter-message-regex <pattern>";
        }
        try {
            workingCommits = Analyzer.filterByMessageRegex(workingCommits, regex);
            return String.format("Filtered to %d commits matching regex '%s'", workingCommits.size(), regex);
        } catch (Exception e) {
            return "Invalid regex: " + e.getMessage();
        }
    }

    private String cmdFilterDateRange(String args) {
        String[] dates = args.split("\\s+");
        if (dates.length < 2) {
            return "Usage: filter-date-range <start-date> <end-date> (format: YYYY-MM-DD)";
        }
        try {
            LocalDate start = LocalDate.parse(dates[0]);
            LocalDate end = LocalDate.parse(dates[1]);
            workingCommits = Analyzer.filterByDateRange(workingCommits, start, end);
            return String.format("Filtered to %d commits between %s and %s", workingCommits.size(), start, end);
        } catch (DateTimeParseException e) {
            return "Invalid date format. Use YYYY-MM-DD";
        }
    }

    private String cmdFilterBefore(String dateStr) {
        if (dateStr.isEmpty()) {
            return "Usage: filter-before <date> (format: YYYY-MM-DD)";
        }
        try {
            LocalDate date = LocalDate.parse(dateStr);
            workingCommits = Analyzer.filterBefore(workingCommits, date);
            return String.format("Filtered to %d commits before %s", workingCommits.size(), date);
        } catch (DateTimeParseException e) {
            return "Invalid date format. Use YYYY-MM-DD";
        }
    }

    private String cmdFilterAfter(String dateStr) {
        if (dateStr.isEmpty()) {
            return "Usage: filter-after <date> (format: YYYY-MM-DD)";
        }
        try {
            LocalDate date = LocalDate.parse(dateStr);
            workingCommits = Analyzer.filterAfter(workingCommits, date);
            return String.format("Filtered to %d commits after %s", workingCommits.size(), date);
        } catch (DateTimeParseException e) {
            return "Invalid date format. Use YYYY-MM-DD";
        }
    }

    private String cmdResetFilters() {
        workingCommits = copyList(allCommits);
        return String.format("Reset filters. Working set now has %d commits", workingCommits.size());
    }

    // ==================== SORTING ====================

    private String cmdSortDateAsc() {
        workingCommits = Analyzer.sortByDateAsc(workingCommits);
        return "Sorted by date (ascending)";
    }

    private String cmdSortDateDesc() {
        workingCommits = Analyzer.sortByDateDesc(workingCommits);
        return "Sorted by date (descending)";
    }

    private String cmdSortAuthor() {
        workingCommits = Analyzer.sortByAuthor(workingCommits);
        return "Sorted by author";
    }

    private String cmdSortMessageLength() {
        workingCommits = Analyzer.sortByMessageLength(workingCommits);
        return "Sorted by message length";
    }

    private String cmdSortHash() {
        workingCommits = Analyzer.sortByHash(workingCommits);
        return "Sorted by hash";
    }

    // ==================== ANALYTICS ====================

    private String cmdTopAuthors(String nStr) {
        int n = parseIntOrDefault(nStr, 10);
        MyList<AuthorStats> stats = Analyzer.topAuthors(workingCommits, n);

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\nTop %d Authors:\n", n));
        sb.append(String.format("%-5s %-30s %s\n", "Rank", "Author", "Commits"));
        sb.append("-".repeat(50)).append("\n");

        for (int i = 0; i < stats.size(); i++) {
            sb.append(String.format("%-5d %s\n", i + 1, stats.get(i)));
        }

        return sb.toString();
    }

    private String cmdTopWords(String nStr) {
        int n = parseIntOrDefault(nStr, 10);
        MyList<WordCount> stats = Analyzer.topWords(workingCommits, n);

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\nTop %d Words:\n", n));
        sb.append(String.format("%-5s %-20s %s\n", "Rank", "Word", "Count"));
        sb.append("-".repeat(35)).append("\n");

        for (int i = 0; i < stats.size(); i++) {
            sb.append(String.format("%-5d %s\n", i + 1, stats.get(i)));
        }

        return sb.toString();
    }

    private String cmdCommitsPerDay() {
        MyList<TimeSeriesStats> stats = Analyzer.commitsPerDay(workingCommits);
        return formatTimeSeries("Commits Per Day", stats);
    }

    private String cmdCommitsPerWeek() {
        MyList<TimeSeriesStats> stats = Analyzer.commitsPerWeek(workingCommits);
        return formatTimeSeries("Commits Per Week", stats);
    }

    private String cmdCommitsPerMonth() {
        MyList<TimeSeriesStats> stats = Analyzer.commitsPerMonth(workingCommits);
        return formatTimeSeries("Commits Per Month", stats);
    }

    private String formatTimeSeries(String title, MyList<TimeSeriesStats> stats) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(title).append(":\n");
        sb.append(String.format("%-15s %s\n", "Period", "Count"));
        sb.append("-".repeat(25)).append("\n");

        for (int i = 0; i < stats.size(); i++) {
            sb.append(stats.get(i)).append("\n");
        }

        return sb.toString();
    }

    private String cmdCount() {
        return String.format("Total commits: %d", Analyzer.countCommits(workingCommits));
    }

    private String cmdCountAuthors() {
        return String.format("Unique authors: %d", Analyzer.countAuthors(workingCommits));
    }

    private String cmdAvgMessageLength() {
        return String.format("Average message length: %.2f characters", Analyzer.avgMessageLength(workingCommits));
    }

    private String cmdInversionCount() {
        long inversions = Analyzer.inversionCount(workingCommits);
        return String.format("Inversion count: %d", inversions);
    }

    // ==================== SEARCH ====================

    private String cmdSearchMessage(String keyword) {
        if (keyword.isEmpty()) {
            return "Usage: search-message <keyword>";
        }
        MyList<Commit> results = Analyzer.filterByMessage(workingCommits, keyword);
        return formatCommitList("Search Results", results);
    }

    private String cmdSearchAuthor(String partial) {
        if (partial.isEmpty()) {
            return "Usage: search-author <partial-name>";
        }
        MyList<Commit> results = Analyzer.filterByAuthor(workingCommits, partial);
        return formatCommitList("Search Results", results);
    }

    private String cmdSearchHash(String prefix) {
        if (prefix.isEmpty()) {
            return "Usage: search-hash <hash-prefix>";
        }
        MyList<Commit> results = Analyzer.searchByHash(workingCommits, prefix);
        return formatCommitList("Search Results", results);
    }

    private String formatCommitList(String title, MyList<Commit> commits) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(title).append(" (").append(commits.size()).append(" commits):\n");

        int limit = Math.min(20, commits.size());
        for (int i = 0; i < limit; i++) {
            sb.append(commits.get(i)).append("\n");
        }

        if (commits.size() > 20) {
            sb.append(String.format("... (showing first 20 of %d)\n", commits.size()));
        }

        return sb.toString();
    }

    // ==================== UTILITY ====================

    private String cmdStats() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== Dataset Statistics ===\n");
        sb.append(String.format("Total commits loaded: %d\n", allCommits.size()));
        sb.append(String.format("Working set size: %d\n", workingCommits.size()));
        sb.append(String.format("Unique authors: %d\n", Analyzer.countAuthors(workingCommits)));
        sb.append(String.format("Average message length: %.2f\n", Analyzer.avgMessageLength(workingCommits)));

        if (!workingCommits.isEmpty()) {
            MyList<Commit> sorted = Analyzer.sortByDateAsc(workingCommits);
            sb.append(String.format("Date range: %s to %s\n",
                    sorted.get(0).getFormattedDate(),
                    sorted.get(sorted.size() - 1).getFormattedDate()));
        }

        return sb.toString();
    }

    private String cmdHelp() {
        return "\n=== Git Commit Log Analyzer ===\n\n" +
                "LOADING & SAVING:\n" +
                "  load <file>           - Load commits from file\n" +
                "  reload                - Reload last file\n" +
                "  save <file>           - Save current working set\n" +
                "  export-csv <file>     - Export to CSV\n\n" +
                "FILTERING (cumulative):\n" +
                "  filter-author <name>  - Filter by author\n" +
                "  filter-message <text> - Filter by message content\n" +
                "  filter-message-regex <pattern> - Filter by regex\n" +
                "  filter-date-range <start> <end> - Filter by date range\n" +
                "  filter-before <date>  - Filter commits before date\n" +
                "  filter-after <date>   - Filter commits after date\n" +
                "  reset-filters         - Reset to all commits\n\n" +
                "SORTING:\n" +
                "  sort-date-asc         - Sort by date ascending\n" +
                "  sort-date-desc        - Sort by date descending\n" +
                "  sort-author           - Sort by author name\n" +
                "  sort-message-length   - Sort by message length\n" +
                "  sort-hash             - Sort by hash\n\n" +
                "ANALYTICS:\n" +
                "  top-authors <n>       - Top N authors by commit count\n" +
                "  top-words <n>         - Top N words in messages\n" +
                "  commits-per-day       - Commits grouped by day\n" +
                "  commits-per-week      - Commits grouped by week\n" +
                "  commits-per-month     - Commits grouped by month\n" +
                "  count                 - Count commits\n" +
                "  count-authors         - Count unique authors\n" +
                "  avg-message-length    - Average message length\n" +
                "  inversion-count       - Calculate inversions\n\n" +
                "SEARCH:\n" +
                "  search-message <keyword> - Search in messages\n" +
                "  search-author <partial>  - Search authors\n" +
                "  search-hash <prefix>     - Search by hash prefix\n\n" +
                "UTILITY:\n" +
                "  stats                 - Show dataset statistics\n" +
                "  help                  - Show this help\n" +
                "  exit                  - Exit program\n";
    }

    // ==================== HELPERS ====================

    private int parseIntOrDefault(String str, int defaultValue) {
        try {
            return Integer.parseInt(str.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private MyList<Commit> copyList(MyList<Commit> commits) {
        MyList<Commit> copy = new MyList<>();
        for (int i = 0; i < commits.size(); i++) {
            copy.add(commits.get(i));
        }
        return copy;
    }
}
