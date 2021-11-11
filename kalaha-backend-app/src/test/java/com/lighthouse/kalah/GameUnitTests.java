package com.lighthouse.kalah;

import com.lighthouse.kalah.game.Game;
import com.lighthouse.kalah.game.Kalahah;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static com.lighthouse.kalah.TestTools.assertBoard;
/**
 *  Unit tests for {@link com.lighthouse.kalah.game.Game}
 *
 * @author Jorge Ortiz
 */
public class GameUnitTests {

    @Test
    public void createGame() {
        Game game = new Game(TestTools.createGame());
        Integer id = game.create();

        assertTrue(id > Kalahah.GAMES_ORIGIN);
        assertBoard(asList(6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0),game.getGame(id));
    }

    @Test
    public void getGameDontExists() {
        Game game = new Game(TestTools.createGame());
        Integer id = game.create();
        assertTrue(id > Kalahah.GAMES_ORIGIN);

        RuntimeException expectedException = assertThrows( RuntimeException.class, () -> game.getGame(id+1));
        assertTrue(expectedException.getMessage().contains("Invalid gameId"));
    }

    @Test
    public void move() {
        Game game = new Game(TestTools.createGame());
        Integer id = game.create();
        assertTrue(id > Kalahah.GAMES_ORIGIN);

        assertBoard(asList(0, 7, 7, 7, 7, 7, 1, 6, 6, 6, 6, 6, 6, 0),game.move(id,1));
    }
}
