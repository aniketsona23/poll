package com.analyzer.cli;

import com.analyzer.core.Index;
import com.analyzer.model.ClassInfo;
import com.analyzer.model.MethodInfo;
import com.containers.GenericList;
import com.containers.PriorityQueueCustom;

public class AggregateCommand implements Command {

    private static class StatItem implements Comparable<StatItem> {
        String name;
        int value;

        public StatItem(String name, int value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public int compareTo(StatItem other) {
            return Integer.compare(this.value, other.value);
        }
    }

    @Override
    public void execute(String[] args, Index index) {
        GenericList<ClassInfo> classes = index.getClasses();
        int totalClasses = classes.size();
        int totalMethods = 0;
        int totalFields = 0;

        PriorityQueueCustom<StatItem> topClassesByMethods = new PriorityQueueCustom<>(
                (a, b) -> Integer.compare(b.value, a.value));
        PriorityQueueCustom<StatItem> topMethodsByLoc = new PriorityQueueCustom<>(
                (a, b) -> Integer.compare(b.value, a.value));

        for (int i = 0; i < classes.size(); i++) {
            ClassInfo c = classes.get(i);
            totalMethods += c.getMethodCount();
            totalFields += c.getFieldCount();

            topClassesByMethods.add(new StatItem(c.getName(), c.getMethodCount()));

            GenericList<MethodInfo> methods = c.getMethods();
            for (int j = 0; j < methods.size(); j++) {
                MethodInfo m = methods.get(j);
                topMethodsByLoc.add(new StatItem(c.getName() + "." + m.getName(), m.getLoc()));
            }
        }

        System.out.println("=== Aggregate Stats ===");
        System.out.println("Total Classes: " + totalClasses);
        System.out.println("Total Methods: " + totalMethods);
        System.out.println("Total Fields: " + totalFields);
        System.out.println("Avg Methods/Class: " + (totalClasses > 0 ? (double) totalMethods / totalClasses : 0));
        System.out.println("Avg Fields/Class: " + (totalClasses > 0 ? (double) totalFields / totalClasses : 0));

        System.out.println("\nTop 5 Classes by Method Count:");
        for (int i = 0; i < 5 && !topClassesByMethods.isEmpty(); i++) {
            StatItem item = topClassesByMethods.remove();
            System.out.println(item.name + ": " + item.value);
        }

        System.out.println("\nTop 5 Methods by LOC:");
        for (int i = 0; i < 5 && !topMethodsByLoc.isEmpty(); i++) {
            StatItem item = topMethodsByLoc.remove();
            System.out.println(item.name + ": " + item.value);
        }
    }
}
