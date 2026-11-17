package com.credora.backend.ds;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Hand-written resizable array list (the "Array/List" structure from the
 * slide's feature table) - no java.util.List/ArrayList involved in storage
 * or growth, only a plain Object[] that doubles in capacity when full.
 */
public class DynamicArray<T> implements Iterable<T> {

    private static final int DEFAULT_CAPACITY = 10;

    private Object[] elements;
    private int size;

    public DynamicArray() {
        elements = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    public void add(T value) {
        ensureCapacity(size + 1);
        elements[size] = value;
        size++;
    }

    @SuppressWarnings("unchecked")
    public T get(int index) {
        checkIndex(index);
        return (T) elements[index];
    }

    public void set(int index, T value) {
        checkIndex(index);
        elements[index] = value;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds for size " + size);
        }
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity <= elements.length) {
            return;
        }
        int newCapacity = elements.length * 2;
        Object[] grown = new Object[newCapacity];
        System.arraycopy(elements, 0, grown, 0, size);
        elements = grown;
    }

    /** Escape hatch for the Spring/Jackson boundary, which expects java.util.List. */
    public ArrayList<T> toJavaList() {
        ArrayList<T> copy = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            copy.add(get(i));
        }
        return copy;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            @SuppressWarnings("unchecked")
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return (T) elements[cursor++];
            }
        };
    }
}
