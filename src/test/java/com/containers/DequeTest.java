package com.containers;

import org.junit.jupiter.api.Test;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;

public class DequeTest {

    @Test
    public void testEmptyDeque() {
        System.out.println("\n========================================");
        System.out.println("Running testEmptyDeque...");
        System.out.println("========================================");
        Deque<Integer> d = new Deque<>();
        System.out.println("Created new Deque: " + d);

        assertTrue(d.isEmpty());
        System.out.println("Verified deque is empty.");
        assertEquals(0, d.size());
        System.out.println("Verified deque size is 0.");
        assertThrows(NoSuchElementException.class, d::peek);
        System.out.println("Verified peek throws NoSuchElementException on empty deque.");
        assertThrows(NoSuchElementException.class, d::remove);
        System.out.println("Verified remove throws NoSuchElementException on empty deque.");
        System.out.println("✓ testEmptyDeque passed.");
        System.out.println("========================================\n");
    }

    @Test
    public void testAddRemoveBothEnds() {
        System.out.println("\n========================================");
        System.out.println("Running testAddRemoveBothEnds...");
        System.out.println("========================================");
        Deque<Integer> d = new Deque<>();

        d.addFirst(1); // [1]
        System.out.println("Added first: 1 → " + d);
        d.addLast(2); // [1,2]
        System.out.println("Added last: 2 → " + d);
        d.addFirst(0); // [0,1,2]
        System.out.println("Added first: 0 → " + d);

        int peekLast = d.peekLast();
        System.out.println("Peek last: " + peekLast + " (deque unchanged)");
        assertEquals(2, peekLast);

        int removeLast = d.removeLast();
        System.out.println("Removed last: " + removeLast + " → " + d);
        assertEquals(2, removeLast);

        int removeFirst1 = d.removeFirst();
        System.out.println("Removed first: " + removeFirst1 + " → " + d);
        assertEquals(0, removeFirst1);

        int removeFirst2 = d.removeFirst();
        System.out.println("Removed first: " + removeFirst2 + " → " + d);
        assertEquals(1, removeFirst2);

        assertTrue(d.isEmpty());
        System.out.println("Verified deque is empty.");
        System.out.println("✓ testAddRemoveBothEnds passed.");
        System.out.println("========================================\n");
    }

    @Test
    public void testPeekMappingDefaults() {
        System.out.println("\n========================================");
        System.out.println("Running testPeekMappingDefaults...");
        System.out.println("========================================");
        Deque<Integer> d = new Deque<>();

        d.addLast(10);
        System.out.println("Added last: 10 → " + d);
        d.addLast(20);
        System.out.println("Added last: 20 → " + d);

        int peek = d.peek();
        System.out.println("Peek (default): " + peek + " (deque unchanged)");
        assertEquals(10, peek); // default peek -> first

        int remove = d.remove();
        System.out.println("Remove (default): " + remove + " → " + d);
        assertEquals(10, remove); // default remove -> first
        System.out.println("✓ testPeekMappingDefaults passed.");
        System.out.println("========================================\n");
    }
}
