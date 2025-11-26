package com.analyzer.model;

public class FieldInfo {
    private String name;
    private String type;

    public FieldInfo(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
