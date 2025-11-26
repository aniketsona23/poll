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

            if (line.equals("history")) {
                // Show history (Stack doesn't support iteration easily without popping in
                // strict sense,
                // but our Stack extends ListContainer so we can iterate elements if we cast or
                // access protected)
                // But let's just pop and print for demo? No, that destroys history.
                // Our Stack extends ListContainer, so it has 'elements'.
                // But 'elements' is protected. We can't access it from here unless we are in
                // same package.
                // We are in com.analyzer.cli, Stack is in com.containers.
                // So we can only use public API: push, pop, peek.
                // To display history without destroying it, we'd need a different API or just
                // not show it this way.
                // However, the assignment asks to USE the stack.
                // Let's just push commands to it.
                System.out.println("History (last command on top):");
                // We can't iterate stack without popping.
                // So we will just say "History stored in Stack".
                // Or we can pop everything to a temp stack and push back.
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

            // We need access to the command map from Main, or we can just instantiate
            // commands.
            // For simplicity, let's switch-case or create new instances.
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
                default:
                    System.out.println("Unknown command: " + cmdName);
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
    }
}
