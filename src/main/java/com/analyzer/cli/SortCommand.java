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
            return Integer.compare(this.count, other.count);
        }
    }

    @Override
    public void execute(String[] args, Index index) {
        if (args.length == 0) {
            sortByClassCount(index);
        } else {
            String keyword = args[0];
            if (keyword.startsWith("\"") && keyword.endsWith("\"")) {
                keyword = keyword.substring(1, keyword.length() - 1);
            }
            sortByKeyword(index, keyword);
        }
    }

    private void sortByKeyword(Index index, String keyword) {
        GenericList<ClassInfo> classes = index.getClasses();
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
        GenericList<ClassInfo> classes = index.getClasses();
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
