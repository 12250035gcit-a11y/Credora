package com.credora.backend.ds;

/**
 * Hand-written hash map (separate chaining over an array of buckets, each a
 * singly linked list of entries) - no java.util.Map/HashMap involved.
 * Not thread-safe by itself; callers that need that must synchronize.
 */
public class HashTable<K, V> {

    private static class Entry<K, V> {
        private final K key;
        private V value;
        private Entry<K, V> next;

        Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR_THRESHOLD = 0.75;

    private Entry<K, V>[] buckets;
    private int size;

    @SuppressWarnings("unchecked")
    public HashTable() {
        buckets = (Entry<K, V>[]) new Entry[DEFAULT_CAPACITY];
    }

    public V get(K key) {
        Entry<K, V> entry = findEntry(key);
        return entry == null ? null : entry.value;
    }

    public void put(K key, V value) {
        int index = bucketIndex(key, buckets.length);
        Entry<K, V> entry = buckets[index];
        while (entry != null) {
            if (keysEqual(entry.key, key)) {
                entry.value = value;
                return;
            }
            entry = entry.next;
        }
        buckets[index] = new Entry<>(key, value, buckets[index]);
        size++;
        resizeIfNeeded();
    }

    /** Returns the existing value for key, or creates, stores and returns one via factory. */
    public V computeIfAbsent(K key, ValueFactory<K, V> factory) {
        Entry<K, V> entry = findEntry(key);
        if (entry != null) {
            return entry.value;
        }
        V created = factory.create(key);
        put(key, created);
        return created;
    }

    public int size() {
        return size;
    }

    private Entry<K, V> findEntry(K key) {
        int index = bucketIndex(key, buckets.length);
        Entry<K, V> entry = buckets[index];
        while (entry != null) {
            if (keysEqual(entry.key, key)) {
                return entry;
            }
            entry = entry.next;
        }
        return null;
    }

    private boolean keysEqual(K a, K b) {
        return a == null ? b == null : a.equals(b);
    }

    private int bucketIndex(K key, int bucketCount) {
        int hash = key == null ? 0 : key.hashCode();
        hash ^= (hash >>> 16);
        return (hash & 0x7fffffff) % bucketCount;
    }

    @SuppressWarnings("unchecked")
    private void resizeIfNeeded() {
        if (size < buckets.length * LOAD_FACTOR_THRESHOLD) {
            return;
        }
        Entry<K, V>[] oldBuckets = buckets;
        buckets = (Entry<K, V>[]) new Entry[oldBuckets.length * 2];
        for (Entry<K, V> head : oldBuckets) {
            Entry<K, V> entry = head;
            while (entry != null) {
                int newIndex = bucketIndex(entry.key, buckets.length);
                Entry<K, V> next = entry.next;
                entry.next = buckets[newIndex];
                buckets[newIndex] = entry;
                entry = next;
            }
        }
    }
}
