package com.analyzer.cli;

import com.analyzer.core.Index;

public interface Command {
    void execute(String[] args, Index index);
}
