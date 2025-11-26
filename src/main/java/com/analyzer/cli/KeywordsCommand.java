package com.analyzer.cli;

import com.analyzer.core.Index;
import com.analyzer.model.ClassInfo;
import com.containers.GenericList;
import com.containers.PriorityQueueCustom;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class KeywordsCommand implements Command {

    private static class KeywordCount implements Comparable<KeywordCount> {
        String keyword;
        int count;

        public KeywordCount(String keyword, int count) {
            this.keyword = keyword;
            this.count = count;
        }

        @Override
        public int compareTo(KeywordCount other) {
            // Reverse order for max-heap behavior if using natural ordering,
            // BUT PriorityQueueCustom is min-heap by default.
            // We want top-N frequent, so we want the SMALLEST count at the root to evict
            // it?
            // Or we want to extract max?
            // If we want to print top-N, we can put all in a Max-Heap and extract N times.
            // Or use a Min-Heap of size N to keep the top N.

            // Let's use a Max-Heap to extract top N easily.
            // PriorityQueueCustom is min-heap by default.
            // So we need a comparator that reverses the order.
            return Integer.compare(this.count, other.count);
        }
    }

    @Override
    public void execute(String[] args, Index index) {
        int topN = 10; // default
        // Parse args for --top N (omitted for brevity)

        Map<String, Integer> counts = new HashMap<>();
        GenericList<ClassInfo> classes = index.getClasses();
        GenericList<String> processedFiles = new GenericList<>();

        // 1. Count keywords
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

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] words = line.split("\\W+");
                    for (String word : words) {
                        if (word.length() > 2) { // simple filter
                            counts.put(word, counts.getOrDefault(word, 0) + 1);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 2. Use PriorityQueueCustom to find top N
        // We want a Max-Heap, so we pass a comparator that reverses natural order
        // (which is count ascending)
        // So (a, b) -> b.count - a.count
        PriorityQueueCustom<KeywordCount> pq = new PriorityQueueCustom<>((a, b) -> Integer.compare(b.count, a.count));

        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            pq.add(new KeywordCount(entry.getKey(), entry.getValue()));
        }

        System.out.println("Top " + topN + " Keywords:");
        for (int i = 0; i < topN && !pq.isEmpty(); i++) {
            KeywordCount kc = pq.remove();
            System.out.println(kc.keyword + ": " + kc.count);
        }
    }
}
