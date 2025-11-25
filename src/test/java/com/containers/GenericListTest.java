package com.containers;

import org.junit.jupiter.api.Test;
import java.util.Comparator;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for GenericList: index ops, addAt/removeAt/get, update/contains, and
 * stable sort.
 */
public class GenericListTest {

    @Test
    public void testAddAtRemoveAtGetAndBounds() {
        System.out.println("\n========================================");
        System.out.println("Running testAddAtRemoveAtGetAndBounds...");
        System.out.println("========================================");
        GenericList<Integer> list = new GenericList<>();

        list.addLast(1);
        System.out.println("Added last: 1 → " + list);
        list.addLast(2);
        System.out.println("Added last: 2 → " + list);
        list.addLast(3);
        System.out.println("Added last: 3 → " + list);
        assertEquals(3, list.size());
        System.out.println("Verified list size is 3.");

        list.addAt(1, 9); // [1,9,2,3]
        System.out.println("Added at index 1: 9 → " + list);

        int get1 = list.get(1);
        System.out.println("Get at index 1: " + get1);
        assertEquals(9, get1);

        int removeAt1 = list.removeAt(1);
        System.out.println("Removed at index 1: " + removeAt1 + " → " + list);
        assertEquals(9, removeAt1);

        int get1AfterRemove = list.get(1);
        System.out.println("Get at index 1 after remove: " + get1AfterRemove);
        assertEquals(2, get1AfterRemove); // back to [1,2,3]

        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        System.out.println("Verified get(-1) throws IndexOutOfBoundsException.");
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(list.size()));
        System.out.println("Verified get(size) throws IndexOutOfBoundsException.");
        assertThrows(IndexOutOfBoundsException.class, () -> list.addAt(100, 5));
        System.out.println("Verified addAt(100, 5) throws IndexOutOfBoundsException.");
        System.out.println("✓ testAddAtRemoveAtGetAndBounds passed.");
        System.out.println("========================================\n");
    }

    @Test
    public void testUpdateAndContains() {
        System.out.println("\n========================================");
        System.out.println("Running testUpdateAndContains...");
        System.out.println("========================================");
        GenericList<String> list = new GenericList<>();

        list.add("a");
        System.out.println("Added: a → " + list);
        list.add("b");
        System.out.println("Added: b → " + list);
        list.add("c");
        System.out.println("Added: c → " + list);

        boolean containsB = list.contains("b");
        System.out.println("Contains 'b': " + containsB);
        assertTrue(containsB);

        boolean containsZ = list.contains("z");
        System.out.println("Contains 'z': " + containsZ);
        assertFalse(containsZ);

        boolean ok = list.update(1, "B");
        System.out.println("Updated index 1 to 'B': " + ok + " → " + list);
        assertTrue(ok);

        String get1 = list.get(1);
        System.out.println("Get at index 1: " + get1);
        assertEquals("B", get1);

        boolean updateNeg1 = list.update(-1, "x");
        System.out.println("Update index -1: " + updateNeg1);
        assertFalse(updateNeg1);

        boolean updateSize = list.update(list.size(), "x");
        System.out.println("Update index size: " + updateSize);
        assertFalse(updateSize);
        System.out.println("✓ testUpdateAndContains passed.");
        System.out.println("========================================\n");
    }

    static class Item {
        final int key;
        final int id;

        Item(int key, int id) {
            this.key = key;
            this.id = id;
        }

        @Override
        public String toString() {
            return "{" + key + "," + id + "}";
        }
    }

    @Test
    public void testSortStability() {
        System.out.println("\n========================================");
        System.out.println("Running testSortStability...");
        System.out.println("========================================");
        GenericList<Item> list = new GenericList<>();

        // initial order: (1,0), (1,1), (0,2)
        list.add(new Item(1, 0));
        System.out.println("Added: {1,0} → " + list);
        list.add(new Item(1, 1));
        System.out.println("Added: {1,1} → " + list);
        list.add(new Item(0, 2));
        System.out.println("Added: {0,2} → " + list);

        // comparator by key only
        Comparator<Item> cmp = Comparator.comparingInt(i -> i.key);
        list.sort(cmp);
        System.out.println("Sorted list by key → " + list);

        // after stable sort: (0,2),(1,0),(1,1)
        Item item0 = list.get(0);
        System.out.println("Item at 0: " + item0);
        assertEquals(0, item0.key);
        assertEquals(2, item0.id);

        Item item1 = list.get(1);
        System.out.println("Item at 1: " + item1);
        assertEquals(1, item1.key);
        assertEquals(0, item1.id);

        Item item2 = list.get(2);
        System.out.println("Item at 2: " + item2);
        assertEquals(1, item2.key);
        assertEquals(1, item2.id);
        System.out.println("✓ testSortStability passed.");
        System.out.println("========================================\n");
    }

    @Test
    public void testPeekRemoveEmptyBehaviour() {
        System.out.println("\n========================================");
        System.out.println("Running testPeekRemoveEmptyBehaviour...");
        System.out.println("========================================");
        GenericList<Integer> list = new GenericList<>();
        System.out.println("Created new GenericList: " + list);

        assertTrue(list.isEmpty());
        System.out.println("Verified list is empty.");
        assertThrows(NoSuchElementException.class, list::peek);
        System.out.println("Verified peek throws NoSuchElementException on empty list.");
        assertThrows(NoSuchElementException.class, list::remove);
        System.out.println("Verified remove throws NoSuchElementException on empty list.");
        System.out.println("✓ testPeekRemoveEmptyBehaviour passed.");
        System.out.println("========================================\n");
    }
}
