package com.analyzer.model;

import com.containers.GenericList;

public class MethodInfo {
    private String name;
    private String returnType;
    private GenericList<String> parameters;
    private int loc;
    private int startLine;

    public MethodInfo(String name, String returnType, int loc, int startLine) {
        this.name = name;
        this.returnType = returnType;
        this.loc = loc;
        this.startLine = startLine;
        this.parameters = new GenericList<>();
    }

    public void addParameter(String paramType) {
        parameters.add(paramType);
    }

    public String getName() {
        return name;
    }

    public String getReturnType() {
        return returnType;
    }

    public int getLoc() {
        return loc;
    }

    public int getStartLine() {
        return startLine;
    }

    public GenericList<String> getParameters() {
        return parameters;
    }

    public int getParameterCount() {
        return parameters.size();
    }

    @Override
    public String toString() {
        return name + "()";
    }
}
