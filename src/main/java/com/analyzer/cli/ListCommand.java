package com.analyzer.cli;

import com.analyzer.core.Index;
import com.analyzer.model.ClassInfo;
import com.analyzer.model.MethodInfo;
import com.analyzer.model.FieldInfo;
import com.containers.GenericList;

public class ListCommand implements Command {
    @Override
    public void execute(String[] args, Index index) {
        if (args.length == 0) {
            System.err.println("Usage: list <classes|methods|variables|fields>");
            return;
        }

        String subCommand = args[0];
        if (subCommand.equals("classes")) {
            listClasses(index);
        } else if (subCommand.equals("methods")) {
            listMethods(index);
        } else if (subCommand.equals("variables") || subCommand.equals("fields")) {
            listVariables(index);
        } else {
            System.err.println("Unknown list subcommand: " + subCommand);
            System.err.println("Available: classes, methods, variables, fields");
        }
    }

    private void listClasses(Index index) {
        GenericList<ClassInfo> classes = index.getClasses();
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    Classes Found: " + classes.size() + "                         ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();

        for (int i = 0; i < classes.size(); i++) {
            ClassInfo c = classes.get(i);
            System.out.printf("%3d. %-40s [File: %s]\n",
                    (i + 1),
                    c.getPackageName() + "." + c.getName(),
                    extractFileName(c.getFilePath()));
            System.out.printf("     Methods: %d, Fields: %d\n",
                    c.getMethodCount(),
                    c.getFieldCount());
        }
        System.out.println();
    }

    private void listMethods(Index index) {
        GenericList<ClassInfo> classes = index.getClasses();
        int totalMethods = 0;

        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    Methods Listing                             ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();

        for (int i = 0; i < classes.size(); i++) {
            ClassInfo c = classes.get(i);
            GenericList<MethodInfo> methods = c.getMethods();

            if (methods.size() > 0) {
                System.out.println("Class: " + c.getPackageName() + "." + c.getName());
                System.out.println("File: " + extractFileName(c.getFilePath()));

                for (int j = 0; j < methods.size(); j++) {
                    MethodInfo m = methods.get(j);
                    System.out.printf("  - %s %s(", m.getReturnType(), m.getName());

                    GenericList<String> params = m.getParameters();
                    for (int k = 0; k < params.size(); k++) {
                        System.out.print(params.get(k));
                        if (k < params.size() - 1)
                            System.out.print(", ");
                    }
                    System.out.printf(") [LOC: %d, Line: %d]\n", m.getLoc(), m.getStartLine());
                    totalMethods++;
                }
                System.out.println();
            }
        }

        System.out.println("Total methods found: " + totalMethods);
        System.out.println();
    }

    private void listVariables(Index index) {
        GenericList<ClassInfo> classes = index.getClasses();
        int totalFields = 0;

        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    Variables/Fields Listing                    ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();

        for (int i = 0; i < classes.size(); i++) {
            ClassInfo c = classes.get(i);
            GenericList<FieldInfo> fields = c.getFields();

            if (fields.size() > 0) {
                System.out.println("Class: " + c.getPackageName() + "." + c.getName());
                System.out.println("File: " + extractFileName(c.getFilePath()));

                for (int j = 0; j < fields.size(); j++) {
                    FieldInfo f = fields.get(j);
                    System.out.printf("  - %s : %s\n", f.getName(), f.getType());
                    totalFields++;
                }
                System.out.println();
            }
        }

        System.out.println("Total fields found: " + totalFields);
        System.out.println();
    }

    private String extractFileName(String filePath) {
        if (filePath == null)
            return "Unknown";
        int lastSeparator = Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\'));
        return lastSeparator >= 0 ? filePath.substring(lastSeparator + 1) : filePath;
    }
}
