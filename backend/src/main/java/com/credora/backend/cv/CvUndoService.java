package com.credora.backend.cv;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

/**
 * Implements "Undo Changes -> Stack + Push/Pop -> Reverse latest change" from
 * the slide's feature table.
 *
 * One Deque (used purely as a STACK: addFirst = push, pollFirst = pop) per
 * user, capped at 10 so memory does not grow forever. This history is kept
 * in memory only, not in the database - it only needs to survive for the
 * current editing session, so there is no reason to pay for persisting it.
 */
@Service
public class CvUndoService {

    private static final int MAX_HISTORY = 10;

    private final Map<Long, Deque<CvSnapshot>> historyByUser = new ConcurrentHashMap<>();

    public void push(Long userId, CvSnapshot snapshot) {
        Deque<CvSnapshot> stack = historyByUser.computeIfAbsent(userId, id -> new ArrayDeque<>());
        stack.push(snapshot);
        while (stack.size() > MAX_HISTORY) {
            stack.removeLast();
        }
    }

    public Optional<CvSnapshot> pop(Long userId) {
        Deque<CvSnapshot> stack = historyByUser.get(userId);
        if (stack == null || stack.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(stack.pop());
    }

    public boolean hasHistory(Long userId) {
        Deque<CvSnapshot> stack = historyByUser.get(userId);
        return stack != null && !stack.isEmpty();
    }
}
