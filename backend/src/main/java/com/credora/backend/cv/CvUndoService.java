package com.credora.backend.cv;

import com.credora.backend.ds.HashTable;
import com.credora.backend.ds.LinkedStack;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Implements "Undo Changes -> Stack + Push/Pop -> Reverse latest change" from
 * the slide's feature table.
 *
 * One hand-written {@link LinkedStack} per user (held in a hand-written
 * {@link HashTable}, not java.util.Map/HashMap), capped at 10 so memory does not
 * grow forever. This history is kept in memory only, not in the database -
 * it only needs to survive for the current editing session, so there is no
 * reason to pay for persisting it.
 *
 * HashTable/LinkedStack are not internally thread-safe, so every method here is
 * synchronized on the service instance.
 */
@Service
public class CvUndoService {

    private static final int MAX_HISTORY = 10;

    private final HashTable<Long, LinkedStack<CvSnapshot>> historyByUser = new HashTable<>();

    public synchronized void push(Long userId, CvSnapshot snapshot) {
        LinkedStack<CvSnapshot> stack = historyByUser.computeIfAbsent(userId, id -> new LinkedStack<>());
        stack.push(snapshot);
        while (stack.size() > MAX_HISTORY) {
            stack.removeOldest();
        }
    }

    public synchronized Optional<CvSnapshot> pop(Long userId) {
        LinkedStack<CvSnapshot> stack = historyByUser.get(userId);
        if (stack == null || stack.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(stack.pop());
    }

    public synchronized boolean hasHistory(Long userId) {
        LinkedStack<CvSnapshot> stack = historyByUser.get(userId);
        return stack != null && !stack.isEmpty();
    }
}
