package com.analyzer.cli;

import com.analyzer.core.Index;
import com.analyzer.model.ClassInfo;
import com.containers.GenericList;
import com.containers.PriorityQueueCustom;

import java.util.Comparator;

public class TopCommand implements Command {
    @Override
    public void execute(String[] args, Index index) {
        if (args.length < 2) {
            System.err.println("Usage: top <N> <metric>");
            System.err.println("Metrics: methods, fields, variables");
            System.err.println("Example: top 5 methods");
            System.err.println("Example: top 10 fields");
            return;
        }

        int n;
        try {
            n = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.err.println("Invalid number: " + args[0]);
            return;
        }

        String metric = args[1];

        if (metric.equals("methods")) {
            topByMethods(index, n);
        } else if (metric.equals("fields") || metric.equals("variables")) {
            topByFields(index, n);
        } else {
            System.err.println("Unknown metric: " + metric);
            System.err.println("Available metrics: methods, fields, variables");
        }
    }

    private void topByMethods(Index index, int n) {
        GenericList<ClassInfo> classes = index.getClasses();

        // Use PriorityQueueCustom with max-heap (reversed comparator)
        Comparator<ClassInfo> methodComparator = Comparator.comparingInt(ClassInfo::getMethodCount).reversed();
        PriorityQueueCustom<ClassInfo> pq = new PriorityQueueCustom<>(methodComparator);

        // Add all classes to priority queue
        for (int i = 0; i < classes.size(); i++) {
            pq.add(classes.get(i));
        }

        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║              Top " + n + " Classes by Method Count                     ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();

        int count = 0;
        while (!pq.isEmpty() && count < n) {
            ClassInfo c = pq.remove();
            count++;
            System.out.printf("%2d. %-45s : %3d methods\n",
                    count,
                    c.getPackageName() + "." + c.getName(),
                    c.getMethodCount());
            System.out.printf("    File: %s\n", extractFileName(c.getFilePath()));
        }
        System.out.println();
    }

    private void topByFields(Index index, int n) {
        GenericList<ClassInfo> classes = index.getClasses();

        // Use PriorityQueueCustom with max-heap (reversed comparator)
        Comparator<ClassInfo> fieldComparator = Comparator.comparingInt(ClassInfo::getFieldCount).reversed();
        PriorityQueueCustom<ClassInfo> pq = new PriorityQueueCustom<>(fieldComparator);

        // Add all classes to priority queue
        for (int i = 0; i < classes.size(); i++) {
            pq.add(classes.get(i));
        }

        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║              Top " + n + " Classes by Field Count                      ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();

        int count = 0;
        while (!pq.isEmpty() && count < n) {
            ClassInfo c = pq.remove();
            count++;
            System.out.printf("%2d. %-45s : %3d fields\n",
                    count,
                    c.getPackageName() + "." + c.getName(),
                    c.getFieldCount());
            System.out.printf("    File: %s\n", extractFileName(c.getFilePath()));
        }
        System.out.println();
    }

    private String extractFileName(String filePath) {
        if (filePath == null)
            return "Unknown";
        int lastSeparator = Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\'));
        return lastSeparator >= 0 ? filePath.substring(lastSeparator + 1) : filePath;
    }
}
