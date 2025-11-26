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
            return Integer.compare(this.count, other.count);
        }
    }

    @Override
    public void execute(String[] args, Index index) {
        int topN = 10;

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
                        if (word.length() > 2) {
                            counts.put(word, counts.getOrDefault(word, 0) + 1);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Use max-heap to find top N keywords
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
