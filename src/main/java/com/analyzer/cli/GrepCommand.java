package com.analyzer.cli;

import com.analyzer.core.Index;
import com.analyzer.model.ClassInfo;
import com.containers.GenericList;
import com.containers.Queue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GrepCommand implements Command {
    @Override
    public void execute(String[] args, Index index) {
        if (args.length == 0) {
            System.err.println("Usage: grep <pattern>");
            return;
        }

        String patternStr = args[0];
        Pattern pattern = Pattern.compile(patternStr);
        Queue<String> matches = new Queue<>();

        GenericList<ClassInfo> classes = index.getClasses();
        // Naive approach: re-read files based on class info or just scan all files if
        // we had them.
        // Since Index only stores ClassInfo, we might miss non-class files.
        // But for this assignment, let's iterate over unique files in the index.

        GenericList<String> processedFiles = new GenericList<>();

        for (int i = 0; i < classes.size(); i++) {
            String filePath = classes.get(i).getFilePath();

            // Avoid processing same file multiple times
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
                int lineNumber = 0;
                while ((line = reader.readLine()) != null) {
                    lineNumber++;
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        matches.enqueue(String.format("%s:%d: %s", filePath, lineNumber, line.trim()));
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading file: " + filePath);
            }
        }

        System.out.println("Grep Results:");
        while (!matches.isEmpty()) {
            System.out.println(matches.dequeue());
        }
    }
}
