package com.analyzer.core;

import com.containers.GenericList;
import com.containers.Queue;
import java.io.File;

public class Scanner {

    public GenericList<File> scan(String rootPath) {
        GenericList<File> javaFiles = new GenericList<>();
        File root = new File(rootPath);

        if (!root.exists()) {
            return javaFiles;
        }

        Queue<File> queue = new Queue<>();
        queue.enqueue(root);

        while (!queue.isEmpty()) {
            File current = queue.dequeue();

            if (current.isDirectory()) {
                File[] children = current.listFiles();
                if (children != null) {
                    for (File child : children) {
                        queue.enqueue(child);
                    }
                }
            } else if (current.getName().endsWith(".java")) {
                javaFiles.add(current);
            }
        }

        return javaFiles;
    }
}
