package com.analyzer.cli;

import com.analyzer.core.Index;
import com.analyzer.model.ClassInfo;
import com.analyzer.model.MethodInfo;
import com.containers.GenericList;

import java.io.File;

public class MetricsCommand implements Command {
    @Override
    public void execute(String[] args, Index index) {
        if (args.length < 2 || !args[0].equals("--file")) {
            System.err.println("Usage: metrics --file <path>");
            return;
        }

        String targetFile = new File(args[1]).getAbsolutePath();
        GenericList<ClassInfo> classes = index.getClasses();

        int fileClasses = 0;
        int fileMethods = 0;
        int maxMethodLoc = 0;
        String longestMethodName = "";

        boolean found = false;
        for (int i = 0; i < classes.size(); i++) {
            ClassInfo c = classes.get(i);
            if (new File(c.getFilePath()).getAbsolutePath().equals(targetFile)) {
                found = true;
                fileClasses++;
                fileMethods += c.getMethodCount();

                GenericList<MethodInfo> methods = c.getMethods();
                for (int j = 0; j < methods.size(); j++) {
                    MethodInfo m = methods.get(j);
                    if (m.getLoc() > maxMethodLoc) {
                        maxMethodLoc = m.getLoc();
                        longestMethodName = m.getName();
                    }
                }
            }
        }

        if (!found) {
            System.out.println("File not found in index or no classes found in file: " + targetFile);
            return;
        }

        System.out.println("Metrics for file: " + targetFile);
        System.out.println("Classes: " + fileClasses);
        System.out.println("Methods: " + fileMethods);
        System.out.println("Longest Method: " + longestMethodName + " (" + maxMethodLoc + " LOC)");
    }
}
