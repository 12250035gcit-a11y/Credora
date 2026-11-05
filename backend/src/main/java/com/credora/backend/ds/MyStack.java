package com.credora.backend.ds;

/**
 * Hand-written LIFO stack (the "Stack + Push/Pop" structure from the slide's
 * feature table), backed by its own singly linked nodes - no java.util.Deque
 * involved.
 */
public class MyStack<T> {

    private static class Node<T> {
        private final T value;
        private Node<T> next;

        Node(T value, Node<T> next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node<T> top;
    private int size;

    public void push(T value) {
        top = new Node<>(value, top);
        size++;
    }

    public T pop() {
        if (top == null) {
            throw new IllegalStateException("Cannot pop from an empty stack.");
        }
        T value = top.value;
        top = top.next;
        size--;
        return value;
    }

    public T peek() {
        if (top == null) {
            throw new IllegalStateException("Cannot peek an empty stack.");
        }
        return top.value;
    }

    public boolean isEmpty() {
        return top == null;
    }

    public int size() {
        return size;
    }

    /** Drops the oldest (bottom) entry, used to cap history size. */
    public void removeOldest() {
        if (top == null) {
            return;
        }
        if (top.next == null) {
            top = null;
            size = 0;
            return;
        }
        Node<T> current = top;
        while (current.next.next != null) {
            current = current.next;
        }
        current.next = null;
        size--;
    }
}
