package com.lighthouse.kalah;

import com.lighthouse.kalah.game.Kalahah;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static com.lighthouse.kalah.TestTools.expectedBoardPebbles;
import static com.lighthouse.kalah.game.Kalahah.KALAH_PLAYER_NORTH;
import static com.lighthouse.kalah.game.Kalahah.KALAH_PLAYER_SOUTH;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests {@link Kalahah}
 *
 * @author Jorge Ortiz
 */
public class KalahahUnitTests {

    @Test
    public void setupBoard() {
        Kalahah board = new Kalahah(6);
        assertBoard(asList(6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0), board);
    }

    /**
     * Method source provideMovesToBoard
     * 13 12 11 10  9  8
     * 6  6  6  6  6  6
     * 14 0              1 7
     * 0  7  7  7  7  7
     * 1  2  3  4  5  6
     *
     * @return Arguments Parameter Tests
     */
    private static Stream<Arguments> provideSouthMovesToBoard() {
        return Stream.of(
                Arguments.of(1, asList(0, 7, 7, 7, 7, 7, 1, 6, 6, 6, 6, 6, 6, 0), ""),
                Arguments.of(2, asList(6, 0, 7, 7, 7, 7, 1, 7, 6, 6, 6, 6, 6, 0), ""),
                Arguments.of(3, asList(6, 6, 0, 7, 7, 7, 1, 7, 7, 6, 6, 6, 6, 0), ""),
                Arguments.of(4, asList(6, 6, 6, 0, 7, 7, 1, 7, 7, 7, 6, 6, 6, 0), ""),
                Arguments.of(5, asList(6, 6, 6, 6, 0, 7, 1, 7, 7, 7, 7, 6, 6, 0), ""),
                Arguments.of(6, asList(6, 6, 6, 6, 6, 0, 1, 7, 7, 7, 7, 7, 6, 0), ""),
                Arguments.of(KALAH_PLAYER_SOUTH, asList(6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0), "Invalid move this is a Kalahah"),
                Arguments.of(8, asList(6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0), "Player SOUTH cannot move pieces from this pit"),
                Arguments.of(9, asList(6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0), "Player SOUTH cannot move pieces from this pit"),
                Arguments.of(10, asList(6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0), "Player SOUTH cannot move pieces from this pit"),
                Arguments.of(11, asList(6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0), "Player SOUTH cannot move pieces from this pit"),
                Arguments.of(12, asList(6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0), "Player SOUTH cannot move pieces from this pit"),
                Arguments.of(13, asList(6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0), "Player SOUTH cannot move pieces from this pit"),
                Arguments.of(KALAH_PLAYER_NORTH, asList(6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0), "Invalid move this is a Kalahah")
        );
    }

    @ParameterizedTest
    @MethodSource("provideSouthMovesToBoard")
    public void simpleMoveSouth(int pitId, List<Integer> expectedPebbles, String messageError) {
        Kalahah board = new Kalahah(6);

        if (pitId < 7) {
            board.move(pitId);
            assertBoard(expectedBoardPebbles(expectedPebbles), board);
        } else {
            RuntimeException expectedException = assertThrows(RuntimeException.class, () -> board.move(pitId));
            assertTrue(expectedException.getMessage().contains(messageError));
            assertBoard(expectedBoardPebbles(expectedPebbles), board);
        }
    }

    /**
     * Method source
     *             13 12 11 10  9  8
     *              6  6  6  6  6  7
     *           14 0              1 7
     *              6  0  7  7  7  7
     *              1  2  3  4  5  6
     * @return
     */
    private static Stream<Arguments> provideNorthMovesToBoard() {
        return Stream.of(
                Arguments.of(1, asList(6, 0, 7, 7, 7, 7, 1, 7, 6, 6, 6, 6, 6, 0), "Player NORTH cannot move pieces from this pit"),
                Arguments.of(2, asList(6, 0, 7, 7, 7, 7, 1, 7, 6, 6, 6, 6, 6, 0), "Player NORTH cannot move pieces from this pit"),
                Arguments.of(3, asList(6, 6, 0, 7, 7, 7, 1, 7, 7, 6, 6, 6, 6, 0), "Player NORTH cannot move pieces from this pit"),
                Arguments.of(4, asList(6, 6, 6, 0, 7, 7, 1, 7, 7, 7, 6, 6, 6, 0), "Player NORTH cannot move pieces from this pit"),
                Arguments.of(5, asList(6, 6, 6, 6, 0, 7, 1, 7, 7, 7, 7, 6, 6, 0), "Player NORTH cannot move pieces from this pit"),
                Arguments.of(6, asList(6, 6, 6, 6, 6, 0, 1, 7, 7, 7, 7, 7, 6, 0), "Player NORTH cannot move pieces from this pit"),
                Arguments.of(KALAH_PLAYER_SOUTH, asList(6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0), "Invalid move this is a Kalahah"),
                Arguments.of(8, asList(6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0), ""),
                Arguments.of(9, asList(6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0), ""),
                Arguments.of(10, asList(6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0), ""),
                Arguments.of(11, asList(6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0), ""),
                Arguments.of(12, asList(6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0), ""),
                Arguments.of(13, asList(6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0), ""),
                Arguments.of(KALAH_PLAYER_NORTH, asList(6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0), "Invalid move this is a Kalahah")
        );
    }
    
    @ParameterizedTest
    @MethodSource("provideNorthMovesToBoard")
    public void simpleMoveNorth(int pitId, List<Integer> expectedPebbles, String messageError) {
        Kalahah board = new Kalahah(6);
        board.move(2); //south & change to north turn

        assertEquals(6, board.getStatus().get(1));
        assertEquals(0, board.getStatus().get(2));
        assertEquals(7, board.getStatus().get(3));
        assertEquals(7, board.getStatus().get(4));
        assertEquals(7, board.getStatus().get(5));
        assertEquals(7, board.getStatus().get(6));
        assertEquals(1, board.getStatus().get(7));
        assertEquals(7, board.getStatus().get(8));
        assertEquals(6, board.getStatus().get(9));
        assertEquals(6, board.getStatus().get(10));
        assertEquals(6, board.getStatus().get(11));
        assertEquals(6, board.getStatus().get(12));
        assertEquals(6, board.getStatus().get(13));
        assertEquals(0, board.getStatus().get(14));

        assertThrows(RuntimeException.class, () -> {
            board.move(1); //move 1, but is north turn, so is not possible.
        });
    }

    @Test
    public void invalidPitMoveAndSouthGetsAnotherTurn() {
        Kalahah board = new Kalahah(6);
        //  13 12 11 10  9  8
        //   6  6  6  6  6  6
        //14 0              1 7
        //   0  7  7  7  7  7
        //   1  2  3  4  5  6
        board.move(1); //south player
        assertEquals(0, board.getStatus().get(1));
        assertEquals(7, board.getStatus().get(2));
        assertEquals(7, board.getStatus().get(3));
        assertEquals(7, board.getStatus().get(4));
        assertEquals(7, board.getStatus().get(5));
        assertEquals(7, board.getStatus().get(6));
        assertEquals(1, board.getStatus().get(7));
        assertEquals(6, board.getStatus().get(8));
        assertEquals(6, board.getStatus().get(9));
        assertEquals(6, board.getStatus().get(10));
        assertEquals(6, board.getStatus().get(11));
        assertEquals(6, board.getStatus().get(12));
        assertEquals(6, board.getStatus().get(13));
        assertEquals(0, board.getStatus().get(14));
        assertThrows(RuntimeException.class, () -> {
            board.move(11); // move pieces from north player when still south turn
        });
        //  13 12 11 10  9  8
        //   6  6  6  6  7  7
        //14 0              2 7
        //   0  0  8  8  8  8
        //   1  2  3  4  5  6
        board.move(2);
        assertEquals(0, board.getStatus().get(1));
        assertEquals(0, board.getStatus().get(2));
        assertEquals(8, board.getStatus().get(3));
        assertEquals(8, board.getStatus().get(4));
        assertEquals(8, board.getStatus().get(5));
        assertEquals(8, board.getStatus().get(6));
        assertEquals(2, board.getStatus().get(7));
        assertEquals(7, board.getStatus().get(8));
        assertEquals(7, board.getStatus().get(9));
        assertEquals(6, board.getStatus().get(10));
        assertEquals(6, board.getStatus().get(11));
        assertEquals(6, board.getStatus().get(12));
        assertEquals(6, board.getStatus().get(13));
        assertEquals(0, board.getStatus().get(14));
    }

    @Test
    public void moveItemsFromEmptyPit() {
        //  13 12 11 10  9  8
        //   6  6  6  6  6  6
        //14 0              6 7
        //   6  6  6  6  0  6
        //   1  2  3  4  5  6
        assertThrows(RuntimeException.class, () -> {
            Kalahah board = new Kalahah(6);
            ConcurrentHashMap<Integer, Integer> tempBoard = board.getStatus();
            tempBoard.put(5, 0);
            tempBoard.put(7, 6);
            board.setStatus(tempBoard);
            board.move(5);
        });
    }

    //you can steal stones in front of you from empty spots, and you take the opponent stones to your kalah.
    @Test
    public void captureStonesFromEmptyCup() {
        // before
        //  13 12 11 10  9  8
        //   6  6  6  6  6  6
        //14 2              6 7
        //   4  6  6  6  0  6
        //   1  2  3  4  5  6

        // after
        //  13 12 11 10  9  8
        //   6  6  6  6  0  6
        //14 2             13 7
        //   0  7  7  7  0  6
        //   1  2  3  4  5  6

        Kalahah board = new Kalahah(6);
        ConcurrentHashMap<Integer, Integer> tempBoard = board.getStatus();
        tempBoard.put(1, 4);
        tempBoard.put(5, 0);
        tempBoard.put(7, 6);
        tempBoard.put(14, 2);
        board.setStatus(tempBoard);
        board.move(1);
        assertEquals(7, board.getStatus().get(2));
        assertEquals(7, board.getStatus().get(3));
        assertEquals(7, board.getStatus().get(4));
        assertEquals(0, board.getStatus().get(5));
        assertEquals(6, board.getStatus().get(6));
        assertEquals(13, board.getStatus().get(7));
        assertEquals(6, board.getStatus().get(8));
        assertEquals(0, board.getStatus().get(9));
        assertEquals(6, board.getStatus().get(10));
        assertEquals(6, board.getStatus().get(11));
        assertEquals(6, board.getStatus().get(12));
        assertEquals(6, board.getStatus().get(13));
        assertEquals(2, board.getStatus().get(14));
    }

    @Test
    public void cannotCaptureStonesFromEmptyCupAndThereNoOpponentStones() {
        // before
        //  13 12 11 10  9  8
        //   7  7  7  7  0  6
        //14 4              6 7
        //   4  6  6  6  0  6
        //   1  2  3  4  5  6

        // after
        //  13 12 11 10  9  8
        //   7  7  7  7  0  6
        //14 4              6 7
        //   0  7  7  7  1  6
        //   1  2  3  4  5  6

        Kalahah board = new Kalahah(6);
        ConcurrentHashMap<Integer, Integer> tempBoard = board.getStatus();
        tempBoard.put(1, 4);
        tempBoard.put(5, 0);
        tempBoard.put(7, 6);
        tempBoard.put(9, 0);
        tempBoard.put(10, 7);
        tempBoard.put(11, 7);
        tempBoard.put(12, 7);
        tempBoard.put(13, 7);
        tempBoard.put(14, 4);
        board.setStatus(tempBoard);
        board.move(1);
        assertEquals(7, board.getStatus().get(2));
        assertEquals(7, board.getStatus().get(3));
        assertEquals(7, board.getStatus().get(4));
        assertEquals(1, board.getStatus().get(5));
        assertEquals(6, board.getStatus().get(6));
        assertEquals(6, board.getStatus().get(7));
        assertEquals(6, board.getStatus().get(8));
        assertEquals(0, board.getStatus().get(9));
        assertEquals(7, board.getStatus().get(10));
        assertEquals(7, board.getStatus().get(11));
        assertEquals(7, board.getStatus().get(12));
        assertEquals(7, board.getStatus().get(13));
        assertEquals(4, board.getStatus().get(14));
    }

    @Test
    public void moreThanSixStonesDoNotFillOpponentsKalahah() {
        // before  <---
        //  13 12 11 10  9  8
        //   6  6  6  6  6  6
        //14 0              0 7
        //   6  3  3 12  6  6
        //   1  2  3  4  5  6
        // --->

        // after
        //  13 12 11 10  9  8
        //   7  7  7  7  7  7
        //14 0              1 7
        //   7  4  4  0  7  7
        //   1  2  3  4  5  6
        Kalahah board = new Kalahah(6);
        ConcurrentHashMap<Integer, Integer> tempBoard = board.getStatus();
        tempBoard.put(2, 3);
        tempBoard.put(3, 3);
        tempBoard.put(4, 12);
        board.setStatus(tempBoard);
        board.move(4);
        assertEquals(7, board.getStatus().get(1));
        assertEquals(4, board.getStatus().get(2));
        assertEquals(4, board.getStatus().get(3));
        assertEquals(0, board.getStatus().get(4));
        assertEquals(7, board.getStatus().get(5));
        assertEquals(7, board.getStatus().get(6));
        assertEquals(1, board.getStatus().get(7));
        assertEquals(7, board.getStatus().get(8));
        assertEquals(7, board.getStatus().get(9));
        assertEquals(7, board.getStatus().get(10));
        assertEquals(7, board.getStatus().get(11));
        assertEquals(7, board.getStatus().get(12));
        assertEquals(7, board.getStatus().get(13));
        assertEquals(0, board.getStatus().get(14));
    }

    @Test
    public void endOfGameGetStonesToPlayerX() {
        // before
        //  13 12 11 10  9  8
        //   4  6  6  6  6  6
        //14 2             35 7
        //   0  0  0  0  0  6
        //   1  2  3  4  5  6

        // after move
        //  13 12 11 10  9  8
        //   6  7  7  7  7  7
        //14 2             36 7
        //   0  0  0  0  0  0
        //   1  2  3  4  5  6

        // after pulling all stones
        //  13 12 11 10  9  8
        //   0  0  0  0  0  0
        //14 43             36 7
        //   0  0  0  0  0  0
        //   1  2  3  4  5  6

        Kalahah board = new Kalahah(6);
        ConcurrentHashMap<Integer, Integer> tempBoard = board.getStatus();
        tempBoard.put(1, 0);
        tempBoard.put(2, 0);
        tempBoard.put(3, 0);
        tempBoard.put(4, 0);
        tempBoard.put(5, 0);
        tempBoard.put(7, 35);
        tempBoard.put(14, 2);
        board.setStatus(tempBoard);
        board.move(6);
        assertEquals(0, board.getStatus().get(1));
        assertEquals(0, board.getStatus().get(2));
        assertEquals(0, board.getStatus().get(3));
        assertEquals(0, board.getStatus().get(4));
        assertEquals(0, board.getStatus().get(5));
        assertEquals(0, board.getStatus().get(6));
        assertEquals(36, board.getStatus().get(7));
        assertEquals(0, board.getStatus().get(8));
        assertEquals(0, board.getStatus().get(9));
        assertEquals(0, board.getStatus().get(10));
        assertEquals(0, board.getStatus().get(11));
        assertEquals(0, board.getStatus().get(12));
        assertEquals(0, board.getStatus().get(13));
        assertEquals(43, board.getStatus().get(14));
        assertThrows(RuntimeException.class, () -> board.move(1));
    }

    private void assertBoard(List<Integer> expectedBoard, Kalahah actualBoard) {
        assertEquals(expectedBoard, actualBoard.getStatus().values().stream().toList(), "unexpected pebbles");
    }
}
