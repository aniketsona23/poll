package com.analyzer;

import com.analyzer.core.Index;
import com.analyzer.core.Parser;
import com.analyzer.core.Scanner;
import com.analyzer.model.ClassInfo;
import com.containers.GenericList;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ContainerUsageTest {

    @Test
    public void testScannerReturnsGenericList() throws NoSuchMethodException {
        Method scanMethod = Scanner.class.getMethod("scan", String.class);
        assertEquals(GenericList.class, scanMethod.getReturnType(), "Scanner.scan() must return GenericList");
    }

    @Test
    public void testParserReturnsGenericList() throws NoSuchMethodException {
        Method parseMethod = Parser.class.getMethod("parse", GenericList.class);
        assertEquals(GenericList.class, parseMethod.getReturnType(), "Parser.parse() must return GenericList");
    }

    @Test
    public void testIndexUsesGenericList() throws NoSuchMethodException {
        Method getClassesMethod = Index.class.getMethod("getClasses");
        assertEquals(GenericList.class, getClassesMethod.getReturnType(), "Index.getClasses() must return GenericList");
    }

    @Test
    public void testClassInfoUsesGenericList() throws NoSuchMethodException {
        Method getMethods = ClassInfo.class.getMethod("getMethods");
        assertEquals(GenericList.class, getMethods.getReturnType(), "ClassInfo.getMethods() must return GenericList");

        Method getFields = ClassInfo.class.getMethod("getFields");
        assertEquals(GenericList.class, getFields.getReturnType(), "ClassInfo.getFields() must return GenericList");
    }
}
