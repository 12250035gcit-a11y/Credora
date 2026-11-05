package com.credora.backend.ds;

/** Tiny stand-in for java.util.function.Function, used only by MyMap.computeIfAbsent. */
public interface ValueFactory<K, V> {
    V create(K key);
}
