package com.containers;

import org.junit.jupiter.api.Test;
import java.util.Comparator;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;

public class PriorityQueueCustomTest {

    @Test
    public void testEmptyPriorityQueue() {
        System.out.println("\n========================================");
        System.out.println("Running testEmptyPriorityQueue...");
        System.out.println("========================================");
        PriorityQueueCustom<Integer> pq = new PriorityQueueCustom<>();
        System.out.println("Created new PriorityQueue: " + pq);

        assertTrue(pq.isEmpty());
        System.out.println("Verified priority queue is empty.");
        assertThrows(NoSuchElementException.class, pq::peek);
        System.out.println("Verified peek throws NoSuchElementException on empty queue.");
        assertThrows(NoSuchElementException.class, pq::remove);
        System.out.println("Verified remove throws NoSuchElementException on empty queue.");
        System.out.println("✓ testEmptyPriorityQueue passed.");
        System.out.println("========================================\n");
    }

    @Test
    public void testMinHeapNaturalOrdering() {
        System.out.println("\n========================================");
        System.out.println("Running testMinHeapNaturalOrdering...");
        System.out.println("========================================");
        PriorityQueueCustom<Integer> pq = new PriorityQueueCustom<>();

        pq.add(5);
        System.out.println("Added: 5 → " + pq);
        pq.add(1);
        System.out.println("Added: 1 → " + pq);
        pq.add(3);
        System.out.println("Added: 3 → " + pq);

        int peek = pq.peek();
        System.out.println("Peek: " + peek + " (heap unchanged)");
        assertEquals(1, peek);

        int remove1 = pq.remove();
        System.out.println("Removed: " + remove1 + " → " + pq);
        assertEquals(1, remove1);

        int remove2 = pq.remove();
        System.out.println("Removed: " + remove2 + " → " + pq);
        assertEquals(3, remove2);

        int remove3 = pq.remove();
        System.out.println("Removed: " + remove3 + " → " + pq);
        assertEquals(5, remove3);

        assertTrue(pq.isEmpty());
        System.out.println("Verified priority queue is empty.");
        System.out.println("✓ testMinHeapNaturalOrdering passed.");
        System.out.println("========================================\n");
    }

    @Test
    public void testMaxHeapWithComparator() {
        System.out.println("\n========================================");
        System.out.println("Running testMaxHeapWithComparator...");
        System.out.println("========================================");
        PriorityQueueCustom<Integer> pq = new PriorityQueueCustom<>(Comparator.reverseOrder());

        pq.add(5);
        System.out.println("Added: 5 → " + pq);
        pq.add(1);
        System.out.println("Added: 1 → " + pq);
        pq.add(3);
        System.out.println("Added: 3 → " + pq);

        int remove1 = pq.remove();
        System.out.println("Removed: " + remove1 + " → " + pq);
        assertEquals(5, remove1);

        int remove2 = pq.remove();
        System.out.println("Removed: " + remove2 + " → " + pq);
        assertEquals(3, remove2);

        int remove3 = pq.remove();
        System.out.println("Removed: " + remove3 + " → " + pq);
        assertEquals(1, remove3);
        System.out.println("✓ testMaxHeapWithComparator passed.");
        System.out.println("========================================\n");
    }

    @Test
    public void testDuplicatesPreserveCorrectRemovals() {
        System.out.println("\n========================================");
        System.out.println("Running testDuplicatesPreserveCorrectRemovals...");
        System.out.println("========================================");
        PriorityQueueCustom<Integer> pq = new PriorityQueueCustom<>();

        pq.add(2);
        System.out.println("Added: 2 → " + pq);
        pq.add(2);
        System.out.println("Added: 2 → " + pq);
        pq.add(1);
        System.out.println("Added: 1 → " + pq);

        int remove1 = pq.remove();
        System.out.println("Removed: " + remove1 + " → " + pq);
        assertEquals(1, remove1);

        int remove2 = pq.remove();
        System.out.println("Removed: " + remove2 + " → " + pq);
        assertEquals(2, remove2);

        int remove3 = pq.remove();
        System.out.println("Removed: " + remove3 + " → " + pq);
        assertEquals(2, remove3);
        System.out.println("✓ testDuplicatesPreserveCorrectRemovals passed.");
        System.out.println("========================================\n");
    }
}
