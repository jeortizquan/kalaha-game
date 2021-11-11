package com.lighthouse.kalah;

import com.lighthouse.kalah.game.Kalahah;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableList;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test tools helpers
 *
 * @author Jorge Ortiz
 */
public class TestTools {

    public static void assertBoard(List<Integer> expectedBoard, Kalahah actualBoard) {
        assertEquals(expectedBoard, actualBoard.getStatus().values().stream().collect(Collectors.toList()), "unexpected pebbles");
    }

    public static List<Integer> expectedBoardPebbles(List<Integer> expectedBoardPebbles) {
        return unmodifiableList(expectedBoardPebbles);
    }

    public static ConcurrentHashMap<Integer, Kalahah> createGame() {
        return new ConcurrentHashMap<>();
    }
}
