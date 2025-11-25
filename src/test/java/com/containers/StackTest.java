package com.containers;

import org.junit.jupiter.api.Test;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;

public class StackTest {

    @Test
    public void testEmptyStack() {
        System.out.println("\n========================================");
        System.out.println("Running testEmptyStack...");
        System.out.println("========================================");
        Stack<Integer> s = new Stack<>();
        System.out.println("Created new Stack: " + s);

        assertTrue(s.isEmpty());
        System.out.println("Verified stack is empty.");
        assertEquals(0, s.size());
        System.out.println("Verified stack size is 0.");
        assertThrows(NoSuchElementException.class, s::pop);
        System.out.println("Verified pop throws NoSuchElementException on empty stack.");
        assertThrows(NoSuchElementException.class, s::peek);
        System.out.println("Verified peek throws NoSuchElementException on empty stack.");
        System.out.println("✓ testEmptyStack passed.");
        System.out.println("========================================\n");
    }

    @Test
    public void testSingleElement() {
        System.out.println("\n========================================");
        System.out.println("Running testSingleElement...");
        System.out.println("========================================");
        Stack<String> s = new Stack<>();
        s.push("hello");
        System.out.println("Pushed: hello → " + s);

        assertFalse(s.isEmpty());
        System.out.println("Verified stack is not empty.");
        assertEquals(1, s.size());
        System.out.println("Verified stack size is 1.");
        assertEquals("hello", s.peek());
        System.out.println("Peek: hello");
        assertEquals("hello", s.pop());
        System.out.println("Popped: hello → " + s);

        assertTrue(s.isEmpty());
        System.out.println("Verified stack is empty.");
        assertEquals(0, s.size());
        System.out.println("Verified stack size is 0.");
        System.out.println("✓ testSingleElement passed.");
        System.out.println("========================================\n");
    }

    @Test
    public void testMultipleElementsLIFO() {
        System.out.println("\n========================================");
        System.out.println("Running testMultipleElementsLIFO...");
        System.out.println("========================================");
        Stack<Integer> s = new Stack<>();

        s.push(1);
        System.out.println("Pushed: 1 → " + s);
        s.push(2);
        System.out.println("Pushed: 2 → " + s);
        s.push(3);
        System.out.println("Pushed: 3 → " + s);

        int peek = s.peek();
        System.out.println("Peek: " + peek + " (stack unchanged)");
        assertEquals(3, peek);

        int pop1 = s.pop();
        System.out.println("Popped: " + pop1 + " → " + s);
        assertEquals(3, pop1);

        int pop2 = s.pop();
        System.out.println("Popped: " + pop2 + " → " + s);
        assertEquals(2, pop2);

        int pop3 = s.pop();
        System.out.println("Popped: " + pop3 + " → " + s);
        assertEquals(1, pop3);

        assertTrue(s.isEmpty());
        System.out.println("Verified stack is empty.");
        System.out.println("✓ testMultipleElementsLIFO passed.");
        System.out.println("========================================\n");
    }
}
