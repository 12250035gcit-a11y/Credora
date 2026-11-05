package com.credora.backend.cv;

import com.credora.backend.ds.MyMap;
import com.credora.backend.ds.MyStack;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Implements "Undo Changes -> Stack + Push/Pop -> Reverse latest change" from
 * the slide's feature table.
 *
 * One hand-written {@link MyStack} per user (held in a hand-written
 * {@link MyMap}, not java.util.Map/HashMap), capped at 10 so memory does not
 * grow forever. This history is kept in memory only, not in the database -
 * it only needs to survive for the current editing session, so there is no
 * reason to pay for persisting it.
 *
 * MyMap/MyStack are not internally thread-safe, so every method here is
 * synchronized on the service instance.
 */
@Service
public class CvUndoService {

    private static final int MAX_HISTORY = 10;

    private final MyMap<Long, MyStack<CvSnapshot>> historyByUser = new MyMap<>();

    public synchronized void push(Long userId, CvSnapshot snapshot) {
        MyStack<CvSnapshot> stack = historyByUser.computeIfAbsent(userId, id -> new MyStack<>());
        stack.push(snapshot);
        while (stack.size() > MAX_HISTORY) {
            stack.removeOldest();
        }
    }

    public synchronized Optional<CvSnapshot> pop(Long userId) {
        MyStack<CvSnapshot> stack = historyByUser.get(userId);
        if (stack == null || stack.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(stack.pop());
    }

    public synchronized boolean hasHistory(Long userId) {
        MyStack<CvSnapshot> stack = historyByUser.get(userId);
        return stack != null && !stack.isEmpty();
    }
}
