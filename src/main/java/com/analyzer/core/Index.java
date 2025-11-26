package com.analyzer.core;

import com.analyzer.model.ClassInfo;
import com.containers.GenericList;

public class Index {
    private GenericList<ClassInfo> classes;

    public Index() {
        this.classes = new GenericList<>();
    }

    public void setClasses(GenericList<ClassInfo> classes) {
        this.classes = classes;
    }

    public GenericList<ClassInfo> getClasses() {
        return classes;
    }

    // Example query method using functional extensions
    public GenericList<ClassInfo> findClassesByName(String name) {
        return classes.filter(c -> c.getName().equals(name));
    }
}
