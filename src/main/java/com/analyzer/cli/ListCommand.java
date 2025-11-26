package com.analyzer.cli;

import com.analyzer.core.Index;
import com.analyzer.model.ClassInfo;
import com.containers.GenericList;

public class ListCommand implements Command {
    @Override
    public void execute(String[] args, Index index) {
        if (args.length == 0) {
            System.err.println("Usage: list <classes|methods|variables>");
            return;
        }

        String subCommand = args[0];
        if (subCommand.equals("classes")) {
            listClasses(index);
        } else if (subCommand.equals("methods")) {
            // TODO: Implement list methods
            System.out.println("Listing methods not yet implemented.");
        } else {
            System.err.println("Unknown list subcommand: " + subCommand);
        }
    }

    private void listClasses(Index index) {
        GenericList<ClassInfo> classes = index.getClasses();
        // Example of using map to transform to string summary
        GenericList<String> summaries = classes.map(c -> String.format("Class: %s (Methods: %d, Fields: %d)",
                c.getName(), c.getMethodCount(), c.getFieldCount()));

        for (int i = 0; i < summaries.size(); i++) {
            System.out.println(summaries.get(i));
        }
    }
}
