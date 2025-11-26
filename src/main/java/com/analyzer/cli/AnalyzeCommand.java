package com.analyzer.cli;

import com.analyzer.core.Index;

public class AnalyzeCommand implements Command {
    @Override
    public void execute(String[] args, Index index) {
        System.out.println("Analysis complete.");

    }
}
