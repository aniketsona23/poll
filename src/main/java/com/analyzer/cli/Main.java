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
        // Register other commands here
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
        System.out.println("Usage: ./run.sh <command> [options]");
        System.out.println("Commands:");
        System.out.println("  analyze [--path <dir>]");
        System.out.println("  list classes|methods|variables");
        System.out.println("  help");
    }
}
