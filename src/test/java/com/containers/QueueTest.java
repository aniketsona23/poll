package com.containers;

import org.junit.jupiter.api.Test;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;

public class QueueTest {

    @Test
    public void testEmptyQueue() {
        System.out.println("\n========================================");
        System.out.println("Running testEmptyQueue...");
        System.out.println("========================================");
        Queue<Integer> q = new Queue<>();
        System.out.println("Created new Queue: " + q);

        assertTrue(q.isEmpty());
        System.out.println("Verified queue is empty.");
        assertEquals(0, q.size());
        System.out.println("Verified queue size is 0.");
        assertThrows(NoSuchElementException.class, q::dequeue);
        System.out.println("Verified dequeue throws NoSuchElementException on empty queue.");
        assertThrows(NoSuchElementException.class, q::peek);
        System.out.println("Verified peek throws NoSuchElementException on empty queue.");
        System.out.println("✓ testEmptyQueue passed.");
        System.out.println("========================================\n");
    }

    @Test
    public void testSingleElement() {
        System.out.println("\n========================================");
        System.out.println("Running testSingleElement...");
        System.out.println("========================================");
        Queue<String> q = new Queue<>();
        q.enqueue("a");
        System.out.println("Enqueued: a → " + q);

        assertFalse(q.isEmpty());
        System.out.println("Verified queue is not empty.");
        assertEquals(1, q.size());
        System.out.println("Verified queue size is 1.");
        assertEquals("a", q.peek());
        System.out.println("Peek: a (queue unchanged)");
        assertEquals("a", q.dequeue());
        System.out.println("Dequeued: a → " + q);

        assertTrue(q.isEmpty());
        System.out.println("Verified queue is empty.");
        System.out.println("✓ testSingleElement passed.");
        System.out.println("========================================\n");
    }

    @Test
    public void testMultipleElementsFIFO() {
        System.out.println("\n========================================");
        System.out.println("Running testMultipleElementsFIFO...");
        System.out.println("========================================");
        Queue<Integer> q = new Queue<>();

        q.enqueue(1);
        System.out.println("Enqueued: 1 → " + q);
        q.enqueue(2);
        System.out.println("Enqueued: 2 → " + q);
        q.enqueue(3);
        System.out.println("Enqueued: 3 → " + q);

        int dequeue1 = q.dequeue();
        System.out.println("Dequeued: " + dequeue1 + " → " + q);
        assertEquals(1, dequeue1);

        int peek = q.peek();
        System.out.println("Peek: " + peek + " (queue unchanged)");
        assertEquals(2, peek);

        int dequeue2 = q.dequeue();
        System.out.println("Dequeued: " + dequeue2 + " → " + q);
        assertEquals(2, dequeue2);

        int dequeue3 = q.dequeue();
        System.out.println("Dequeued: " + dequeue3 + " → " + q);
        assertEquals(3, dequeue3);

        assertTrue(q.isEmpty());
        System.out.println("Verified queue is empty.");
        System.out.println("✓ testMultipleElementsFIFO passed.");
        System.out.println("========================================\n");
    }
}
