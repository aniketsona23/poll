package com.analyzer.model;

import com.containers.GenericList;

public class ClassInfo {
    private String name;
    private String packageName;
    private String filePath;
    private int methodCount;
    private int fieldCount;
    private GenericList<MethodInfo> methods;
    private GenericList<FieldInfo> fields;

    public ClassInfo(String name, String packageName, String filePath) {
        this.name = name;
        this.packageName = packageName;
        this.filePath = filePath;
        this.methods = new GenericList<>();
        this.fields = new GenericList<>();
    }

    public void addMethod(MethodInfo method) {
        methods.add(method);
        methodCount++;
    }

    public void addField(FieldInfo field) {
        fields.add(field);
        fieldCount++;
    }

    public String getName() {
        return name;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getMethodCount() {
        return methodCount;
    }

    public int getFieldCount() {
        return fieldCount;
    }

    public GenericList<MethodInfo> getMethods() {
        return methods;
    }

    public GenericList<FieldInfo> getFields() {
        return fields;
    }

    @Override
    public String toString() {
        return "ClassInfo{name='" + name + "', methods=" + methodCount + "}";
    }
}
