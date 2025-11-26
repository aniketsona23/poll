package com.analyzer.cli;

import com.analyzer.core.Index;
import com.analyzer.core.Parser;
import com.analyzer.core.Scanner;
import com.analyzer.model.ClassInfo;
import com.containers.GenericList;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final Map<String, Command> commands = new HashMap<>();

    static {
        commands.put("analyze", new AnalyzeCommand());
        commands.put("list", new ListCommand());
        commands.put("grep", new GrepCommand());
        commands.put("keywords", new KeywordsCommand());
        commands.put("repl", new ReplCommand());
        commands.put("sort-by-keyword", new SortCommand());
        commands.put("sort-by-class-count", new SortCommand());
        commands.put("aggregate", new AggregateCommand());
        commands.put("metrics", new MetricsCommand());
        commands.put("export", new ExportCommand());
        commands.put("inspect", new InspectCommand());
        commands.put("top", new TopCommand());
        commands.put("help", null); // Help is handled separately
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            printHelp();
            return;
        }

        String commandName = args[0];
        if (commandName.equals("help")) {
            printHelp();
            return;
        }

        String[] commandArgs = Arrays.copyOfRange(args, 1, args.length);

        // Global flags parsing (simplified)
        String path = ".";
        for (int i = 0; i < commandArgs.length; i++) {
            if (commandArgs[i].equals("--path") && i + 1 < commandArgs.length) {
                path = commandArgs[i + 1];
            }
        }

        // Build Index
        System.out.println("Scanning and indexing...");
        Scanner scanner = new Scanner();
        GenericList<File> files = scanner.scan(path);
        Parser parser = new Parser();
        GenericList<ClassInfo> classes = parser.parse(files);
        Index index = new Index();
        index.setClasses(classes);
        System.out.println("Index built. Found " + classes.size() + " classes.");

        Command command = commands.get(commandName);
        if (command != null) {
            command.execute(commandArgs, index);
        } else {
            System.err.println("Unknown command: " + commandName);
            printHelp();
        }
    }

    private static void printHelp() {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║           Codebase Analyzer - Help                           ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("USAGE:");
        System.out.println("  ./run.sh <command> [options]");
        System.out.println("  ./run.bat <command> [options]     (Windows)");
        System.out.println("  make run                          (Interactive REPL mode)");
        System.out.println();
        System.out.println("COMMANDS:");
        System.out.println();
        System.out.println("  analyze [--path <directory>]");
        System.out.println("      Scans and indexes Java files in the specified directory.");
        System.out.println("      Default path is current directory (.)");
        System.out.println("      Example: analyze --path ./src");
        System.out.println();
        System.out.println("  list <type>");
        System.out.println("      Lists classes, methods, or variables from the indexed codebase.");
        System.out.println("      Types: classes | methods | variables | fields");
        System.out.println("      Example: list classes");
        System.out.println("      Example: list methods");
        System.out.println();
        System.out.println("  grep <pattern>");
        System.out.println("      Search for identifiers or regex patterns in the codebase.");
        System.out.println("      Example: grep \"ArrayList\"");
        System.out.println();
        System.out.println("  inspect <class-name-or-file>");
        System.out.println("      Show detailed information about a specific class or file.");
        System.out.println("      Example: inspect Main");
        System.out.println("      Example: inspect Main.java");
        System.out.println();
        System.out.println("  top <N> <metric>");
        System.out.println("      Show top N classes by a specific metric.");
        System.out.println("      Metrics: methods | fields | variables");
        System.out.println("      Example: top 5 methods");
        System.out.println("      Example: top 10 fields");
        System.out.println();
        System.out.println("  keywords [--top <N>]");
        System.out.println("      Count and list top-N keywords in the codebase.");
        System.out.println("      Example: keywords --top 10");
        System.out.println();
        System.out.println("  sort-by-keyword <keyword>");
        System.out.println("      Sort files by the count of a specific keyword.");
        System.out.println("      Example: sort-by-keyword \"TODO\"");
        System.out.println();
        System.out.println("  sort-by-class-count");
        System.out.println("      Sort packages by the number of classes they contain.");
        System.out.println("      Example: sort-by-class-count");
        System.out.println();
        System.out.println("  aggregate");
        System.out.println("      Display codebase statistics and aggregated metrics.");
        System.out.println("      Example: aggregate");
        System.out.println();
        System.out.println("  metrics <filename>");
        System.out.println("      Show detailed metrics for a specific file.");
        System.out.println("      Example: metrics Main.java");
        System.out.println();
        System.out.println("  export [--format <json|csv>] [--output <file>]");
        System.out.println("      Export analysis results to JSON or CSV format.");
        System.out.println("      Example: export --format json --output results.json");
        System.out.println();
        System.out.println("  repl");
        System.out.println("      Start interactive REPL mode with command history.");
        System.out.println("      Type commands directly and 'exit' to quit.");
        System.out.println("      Example: repl");
        System.out.println();
        System.out.println("  help");
        System.out.println("      Show this help message.");
        System.out.println();
        System.out.println("EXAMPLES:");
        System.out.println("  ./run.sh analyze --path ./testdata");
        System.out.println("  ./run.sh list classes");
        System.out.println("  ./run.sh grep \"public static\"");
        System.out.println("  ./run.sh keywords --top 5");
        System.out.println("  ./run.sh aggregate");
        System.out.println("  ./run.sh repl");
        System.out.println();
    }
}
