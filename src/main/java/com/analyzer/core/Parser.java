package com.analyzer.core;

import com.analyzer.model.ClassInfo;
import com.analyzer.model.FieldInfo;
import com.analyzer.model.MethodInfo;
import com.containers.GenericList;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;

public class Parser {

    public GenericList<ClassInfo> parse(GenericList<File> files) {
        GenericList<ClassInfo> classes = new GenericList<>();
        JavaParser javaParser = new JavaParser();

        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            try {
                ParseResult<CompilationUnit> result = javaParser.parse(file);
                if (result.isSuccessful() && result.getResult().isPresent()) {
                    CompilationUnit cu = result.getResult().get();
                    cu.accept(new ClassVisitor(file.getAbsolutePath(), classes), null);
                }
            } catch (IOException e) {
                System.err.println("Error parsing file: " + file.getAbsolutePath());
            }
        }
        return classes;
    }

    private static class ClassVisitor extends VoidVisitorAdapter<Void> {
        private final String filePath;
        private final GenericList<ClassInfo> classes;

        public ClassVisitor(String filePath, GenericList<ClassInfo> classes) {
            this.filePath = filePath;
            this.classes = classes;
        }

        @Override
        public void visit(ClassOrInterfaceDeclaration n, Void arg) {
            super.visit(n, arg);
            String packageName = n.findCompilationUnit().flatMap(CompilationUnit::getPackageDeclaration)
                    .map(pd -> pd.getNameAsString()).orElse("");

            ClassInfo classInfo = new ClassInfo(n.getNameAsString(), packageName, filePath);

            // Extract methods
            n.getMethods().forEach(m -> {
                int loc = m.getEnd().map(end -> end.line).orElse(0) - m.getBegin().map(begin -> begin.line).orElse(0)
                        + 1;
                MethodInfo methodInfo = new MethodInfo(m.getNameAsString(), m.getTypeAsString(), loc,
                        m.getBegin().map(b -> b.line).orElse(0));
                for (Parameter p : m.getParameters()) {
                    methodInfo.addParameter(p.getTypeAsString());
                }
                classInfo.addMethod(methodInfo);
            });

            // Extract fields
            n.getFields().forEach(f -> {
                String type = f.getElementType().asString();
                f.getVariables().forEach(v -> {
                    classInfo.addField(new FieldInfo(v.getNameAsString(), type));
                });
            });

            classes.add(classInfo);
        }
    }
}
