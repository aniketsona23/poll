package com.analyzer.cli;

import com.analyzer.core.Index;
import com.analyzer.model.ClassInfo;
import com.containers.GenericList;
import com.containers.PriorityQueueCustom;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SortCommand implements Command {

    private static class ItemCount implements Comparable<ItemCount> {
        String name;
        int count;

        public ItemCount(String name, int count) {
            this.name = name;
            this.count = count;
        }

        @Override
        public int compareTo(ItemCount other) {
            // Max-heap logic: reverse natural order
            return Integer.compare(this.count, other.count);
        }
    }

    @Override
    public void execute(String[] args, Index index) {
        if (args.length == 0) {
            System.err.println("Usage: sort-by-keyword <keyword> | sort-by-class-count");
            return;
        }

        String subCmd = args[0];
        if (subCmd.startsWith("sort-by-keyword")) {
            // sort-by-keyword <keyword>
            if (args.length < 2) {
                System.err.println("Usage: sort-by-keyword <keyword>");
                return;
            }
            String keyword = args[1];
            sortByKeyword(index, keyword);
        } else if (subCmd.equals("sort-by-class-count")) {
            sortByClassCount(index);
        } else {
            System.err.println("Unknown sort command: " + subCmd);
        }
    }

    private void sortByKeyword(Index index, String keyword) {
        GenericList<ClassInfo> classes = index.getClasses();
        // We want to sort FILES by keyword count.
        // Map file -> count
        // Use PriorityQueueCustom for top-N (say 10)

        PriorityQueueCustom<ItemCount> pq = new PriorityQueueCustom<>((a, b) -> Integer.compare(b.count, a.count));
        GenericList<String> processedFiles = new GenericList<>();

        for (int i = 0; i < classes.size(); i++) {
            String filePath = classes.get(i).getFilePath();
            boolean alreadyProcessed = false;
            for (int j = 0; j < processedFiles.size(); j++) {
                if (processedFiles.get(j).equals(filePath)) {
                    alreadyProcessed = true;
                    break;
                }
            }
            if (alreadyProcessed)
                continue;
            processedFiles.add(filePath);

            int count = 0;
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // Simple count
                    int lastIndex = 0;
                    while ((lastIndex = line.indexOf(keyword, lastIndex)) != -1) {
                        count++;
                        lastIndex += keyword.length();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (count > 0) {
                pq.add(new ItemCount(filePath, count));
            }
        }

        System.out.println("Top files for keyword '" + keyword + "':");
        int topN = 10;
        for (int i = 0; i < topN && !pq.isEmpty(); i++) {
            ItemCount item = pq.remove();
            System.out.println(item.name + ": " + item.count);
        }
    }

    private void sortByClassCount(Index index) {
        // Rank packages by number of classes
        GenericList<ClassInfo> classes = index.getClasses();
        // Map package -> count
        // Since we don't have a Map container, we can use a list of pairs and update
        // manually?
        // Or just use Java Map for transient aggregation as allowed?
        // "Do not use java.util collections for main data storage — only for transient
        // helper operations if absolutely necessary"
        // Aggregation is transient.

        java.util.Map<String, Integer> packageCounts = new java.util.HashMap<>();
        for (int i = 0; i < classes.size(); i++) {
            String pkg = classes.get(i).getPackageName();
            packageCounts.put(pkg, packageCounts.getOrDefault(pkg, 0) + 1);
        }

        PriorityQueueCustom<ItemCount> pq = new PriorityQueueCustom<>((a, b) -> Integer.compare(b.count, a.count));
        for (java.util.Map.Entry<String, Integer> entry : packageCounts.entrySet()) {
            pq.add(new ItemCount(entry.getKey(), entry.getValue()));
        }

        System.out.println("Top Packages by Class Count:");
        int topN = 10;
        for (int i = 0; i < topN && !pq.isEmpty(); i++) {
            ItemCount item = pq.remove();
            System.out.println(item.name + ": " + item.count);
        }
    }
}
