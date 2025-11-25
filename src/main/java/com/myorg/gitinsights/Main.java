package com.myorg.gitinsights;

import java.util.Scanner;

/**
 * Main CLI entry point for Git Commit Log Analyzer.
 * Pure terminal-based interaction with command loop.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════╗");
        System.out.println("║   Git Commit Log Analyzer - Functional-OO Demo     ║");
        System.out.println("║   Using Custom Containers (MyList, PriorityQueue)  ║");
        System.out.println("╚════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("Type 'help' for available commands, 'exit' to quit.");
        System.out.println();

        CommandHandler handler = new CommandHandler();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.print("git-insights> ");

            if (!scanner.hasNextLine()) {
                break;
            }

            String line = scanner.nextLine().trim();

            if (line.isEmpty()) {
                continue;
            }

            String result = handler.executeCommand(line);

            if ("EXIT".equals(result)) {
                running = false;
                System.out.println("Goodbye!");
            } else {
                System.out.println(result);
            }
        }

        scanner.close();
    }
}
