package com.analyzer.cli;

import com.analyzer.core.Index;
import com.analyzer.model.ClassInfo;
import com.analyzer.model.MethodInfo;
import com.analyzer.model.FieldInfo;
import com.containers.GenericList;

public class InspectCommand implements Command {
    @Override
    public void execute(String[] args, Index index) {
        if (args.length == 0) {
            System.err.println("Usage: inspect <class-name-or-file>");
            System.err.println("Example: inspect Main");
            System.err.println("Example: inspect Main.java");
            return;
        }

        String target = args[0];
        GenericList<ClassInfo> classes = index.getClasses();
        ClassInfo found = null;

        // Try to find by class name or file name
        for (int i = 0; i < classes.size(); i++) {
            ClassInfo c = classes.get(i);
            String fileName = extractFileName(c.getFilePath());

            if (c.getName().equals(target) ||
                    c.getName().equalsIgnoreCase(target) ||
                    fileName.equals(target) ||
                    fileName.equalsIgnoreCase(target) ||
                    (c.getPackageName() + "." + c.getName()).equals(target)) {
                found = c;
                break;
            }
        }

        if (found == null) {
            System.err.println("Class or file not found: " + target);
            System.err.println("Use 'list classes' to see available classes");
            return;
        }

        displayClassDetails(found);
    }

    private void displayClassDetails(ClassInfo c) {
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    Class Inspection                            ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();

        System.out.println("Class Name:    " + c.getName());
        System.out.println("Package:       " + c.getPackageName());
        System.out.println("Full Name:     " + c.getPackageName() + "." + c.getName());
        System.out.println("File:          " + c.getFilePath());
        System.out.println();

        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("                         Statistics");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("Total Methods: " + c.getMethodCount());
        System.out.println("Total Fields:  " + c.getFieldCount());
        System.out.println();

        // List all fields
        if (c.getFieldCount() > 0) {
            System.out.println("───────────────────────────────────────────────────────────────");
            System.out.println("Fields (" + c.getFieldCount() + "):");
            System.out.println("───────────────────────────────────────────────────────────────");
            GenericList<FieldInfo> fields = c.getFields();
            for (int i = 0; i < fields.size(); i++) {
                FieldInfo f = fields.get(i);
                System.out.printf("  %2d. %-30s : %s\n", (i + 1), f.getName(), f.getType());
            }
            System.out.println();
        }

        // List all methods
        if (c.getMethodCount() > 0) {
            System.out.println("───────────────────────────────────────────────────────────────");
            System.out.println("Methods (" + c.getMethodCount() + "):");
            System.out.println("───────────────────────────────────────────────────────────────");
            GenericList<MethodInfo> methods = c.getMethods();

            int totalLoc = 0;
            for (int i = 0; i < methods.size(); i++) {
                MethodInfo m = methods.get(i);
                totalLoc += m.getLoc();

                System.out.printf("  %2d. %s %s(", (i + 1), m.getReturnType(), m.getName());

                GenericList<String> params = m.getParameters();
                for (int j = 0; j < params.size(); j++) {
                    System.out.print(params.get(j));
                    if (j < params.size() - 1)
                        System.out.print(", ");
                }
                System.out.println(")");
                System.out.printf("      LOC: %d, Line: %d\n", m.getLoc(), m.getStartLine());
            }
            System.out.println();
            System.out.println("Total Lines of Code (Methods): " + totalLoc);
            if (c.getMethodCount() > 0) {
                System.out.printf("Average LOC per Method: %.1f\n", (double) totalLoc / c.getMethodCount());
            }
        }

        System.out.println("╚════════════════════════════════════════════════════════════════╝");
    }

    private String extractFileName(String filePath) {
        if (filePath == null)
            return "Unknown";
        int lastSeparator = Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\'));
        return lastSeparator >= 0 ? filePath.substring(lastSeparator + 1) : filePath;
    }
}
