package com.myorg.gitinsights;

import com.containers.GenericList;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Consumer;

/**
 * Enhanced list with functional operations (map, filter, reduce, forEach).
 * Extends GenericList from the custom containers package.
 */
public class MyList<T> extends GenericList<T> {

    public MyList() {
        super();
    }

    /**
     * Map each element to a new type using the mapper function.
     * Returns a new MyList with mapped elements.
     */
    public <R> MyList<R> map(Function<T, R> mapper) {
        MyList<R> result = new MyList<>();
        for (int i = 0; i < size(); i++) {
            result.add(mapper.apply(get(i)));
        }
        return result;
    }

    /**
     * Filter elements that match the predicate.
     * Returns a new MyList with only matching elements.
     */
    public MyList<T> filter(Predicate<T> predicate) {
        MyList<T> result = new MyList<>();
        for (int i = 0; i < size(); i++) {
            T element = get(i);
            if (predicate.test(element)) {
                result.add(element);
            }
        }
        return result;
    }

    /**
     * Reduce the list to a single value using accumulator function.
     */
    public <U> U reduce(U identity, BiFunction<U, T, U> accumulator) {
        U result = identity;
        for (int i = 0; i < size(); i++) {
            result = accumulator.apply(result, get(i));
        }
        return result;
    }

    /**
     * Apply an action to each element.
     */
    public void forEach(Consumer<T> action) {
        for (int i = 0; i < size(); i++) {
            action.accept(get(i));
        }
    }

    /**
     * Count inversions in the list (functional algorithm using merge sort).
     * An inversion is a pair (i, j) where i < j but elements[i] > elements[j].
     * Requires T to be Comparable or you can pass a Comparator.
     */
    public long inversionCount() {
        if (size() <= 1)
            return 0;
        return mergeSortAndCount(0, size() - 1, null);
    }

    private long mergeSortAndCount(int left, int right, java.util.Comparator<? super T> comp) {
        if (left >= right)
            return 0;

        int mid = left + (right - left) / 2;
        long count = 0;

        count += mergeSortAndCount(left, mid, comp);
        count += mergeSortAndCount(mid + 1, right, comp);
        count += mergeAndCount(left, mid, right, comp);

        return count;
    }

    private long mergeAndCount(int left, int mid, int right, java.util.Comparator<? super T> comp) {
        MyList<T> temp = new MyList<>();
        int i = left, j = mid + 1;
        long inversions = 0;

        while (i <= mid && j <= right) {
            T leftElem = get(i);
            T rightElem = get(j);

            int cmp;
            if (comp != null) {
                cmp = comp.compare(leftElem, rightElem);
            } else {
                @SuppressWarnings("unchecked")
                Comparable<? super T> c = (Comparable<? super T>) leftElem;
                cmp = c.compareTo(rightElem);
            }

            if (cmp <= 0) {
                temp.add(leftElem);
                i++;
            } else {
                temp.add(rightElem);
                inversions += (mid - i + 1);
                j++;
            }
        }

        while (i <= mid) {
            temp.add(get(i++));
        }
        while (j <= right) {
            temp.add(get(j++));
        }

        for (int k = 0; k < temp.size(); k++) {
            update(left + k, temp.get(k));
        }

        return inversions;
    }
}
