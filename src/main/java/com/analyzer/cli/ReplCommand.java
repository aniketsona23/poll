package com.analyzer.cli;

import com.analyzer.core.Index;
import com.containers.Stack;

import java.util.Scanner;
import java.util.Arrays;

public class ReplCommand implements Command {
    @Override
    public void execute(String[] args, Index index) {
        Scanner scanner = new Scanner(System.in);
        Stack<String> history = new Stack<>();

        System.out.println("Entering REPL mode. Type 'exit' to quit.");

        while (true) {
            System.out.print("> ");
            if (!scanner.hasNextLine())
                break;

            String line = scanner.nextLine().trim();
            if (line.isEmpty())
                continue;

            if (line.equals("exit"))
                break;

            if (line.equals("help")) {
                printReplHelp();
                continue;
            }

            if (line.equals("history")) {

                System.out.println("History (last command on top):");
                Stack<String> temp = new Stack<>();
                while (!history.isEmpty()) {
                    String cmd = history.pop();
                    System.out.println(cmd);
                    temp.push(cmd);
                }
                while (!temp.isEmpty()) {
                    history.push(temp.pop());
                }
                continue;
            }

            history.push(line);

            // Execute command
            // Simple parsing: split by space
            String[] parts = line.split("\\s+");
            String cmdName = parts[0];
            String[] cmdArgs = Arrays.copyOfRange(parts, 1, parts.length);

            Command cmd = null;
            switch (cmdName) {
                case "analyze":
                    cmd = new AnalyzeCommand();
                    break;
                case "list":
                    cmd = new ListCommand();
                    break;
                case "grep":
                    cmd = new GrepCommand();
                    break;
                case "keywords":
                    cmd = new KeywordsCommand();
                    break;
                case "aggregate":
                    cmd = new AggregateCommand();
                    break;
                case "metrics":
                    cmd = new MetricsCommand();
                    break;
                case "export":
                    cmd = new ExportCommand();
                    break;
                case "inspect":
                    cmd = new InspectCommand();
                    break;
                case "top":
                    cmd = new TopCommand();
                    break;
                case "sort-by-keyword":
                case "sort-by-class-count":
                    cmd = new SortCommand();
                    break;
                default:
                    System.out.println("Unknown command: " + cmdName);
                    System.out.println("Type 'help' for available commands.");
            }

            if (cmd != null) {
                try {
                    cmd.execute(cmdArgs, index);
                } catch (Exception e) {
                    System.err.println("Error executing command: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        scanner.close();
    }

    private void printReplHelp() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║           REPL Mode - Available Commands                     ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("ANALYSIS COMMANDS:");
        System.out.println("  list classes           - List all classes with file locations");
        System.out.println("  list methods           - List all methods with signatures");
        System.out.println("  list variables         - List all fields/variables");
        System.out.println("  grep <pattern>         - Search for pattern");
        System.out.println("  keywords               - Show keyword statistics");
        System.out.println("  aggregate              - Show codebase statistics");
        System.out.println("  metrics <filename>     - Show metrics for specific file");
        System.out.println();
        System.out.println("INSPECTION:");
        System.out.println("  inspect <class/file>   - Detailed info about class or file");
        System.out.println("                           Example: inspect Main");
        System.out.println("  top <N> <metric>       - Top N classes by metric");
        System.out.println("                           Example: top 5 methods");
        System.out.println("                           Example: top 10 fields");
        System.out.println();
        System.out.println("SORTING:");
        System.out.println("  sort-by-keyword <kw>   - Sort files by keyword count");
        System.out.println("  sort-by-class-count    - Sort packages by class count");
        System.out.println();
        System.out.println("EXPORT:");
        System.out.println("  export                 - Export results to JSON/CSV");
        System.out.println();
        System.out.println("REPL COMMANDS:");
        System.out.println("  help                   - Show this help message");
        System.out.println("  history                - Show command history");
        System.out.println("  exit                   - Exit REPL mode");
        System.out.println();
    }
}
