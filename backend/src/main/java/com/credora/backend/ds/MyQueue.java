package com.credora.backend.ds;

/**
 * Hand-written FIFO queue (the "Queue + Enqueue/Dequeue" structure from the
 * slide's feature table), backed by its own singly linked nodes with head
 * and tail pointers so both operations are O(1) - no java.util.Queue
 * involved.
 */
public class MyQueue<T> {

    private static class Node<T> {
        private final T value;
        private Node<T> next;

        Node(T value) {
            this.value = value;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public void enqueue(T value) {
        Node<T> node = new Node<>(value);
        if (tail == null) {
            head = node;
        } else {
            tail.next = node;
        }
        tail = node;
        size++;
    }

    /** Removes and returns the oldest entry, or null if the queue is empty. */
    public T dequeue() {
        if (head == null) {
            return null;
        }
        T value = head.value;
        head = head.next;
        if (head == null) {
            tail = null;
        }
        size--;
        return value;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public int size() {
        return size;
    }
}
