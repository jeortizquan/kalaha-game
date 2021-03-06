package com.lighthouse.kalah.game;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Main Game class
 *
 * @author Jorge Ortiz
 */
public class Game {

    private static final Integer NEW_GAME_ATTEMPT_LIMIT = 5;
    private static final Integer STONES_LIMIT = 6;
    public final ConcurrentHashMap<Integer, Kalahah> games;

    public Game(ConcurrentHashMap<Integer, Kalahah> gameList) {
        this.games = gameList;
    }

    /**
     * Creates a board game and places in the game list.
     *
     * @return Integer Id of the Game
     */
    public Integer create() {
        Kalahah board = new Kalahah(STONES_LIMIT);
        int attempt = 0;
        while (exists(board.getGameId()) && attempt < NEW_GAME_ATTEMPT_LIMIT) {
            board.setGameId(ThreadLocalRandom.current().nextInt(Kalahah.GAMES_ORIGIN, Kalahah.GAMES_LIMIT));
            attempt++;
        }
        games.put(board.getGameId(), board);
        return board.getGameId();
    }

    /**
     * Checks if a board game already exists in the game list.
     *
     * @param gameId Identifier of Game
     * @return boolean, true = exists, false = don't
     */
    private boolean exists(Integer gameId) {
        return this.games.get(gameId) != null;
    }

    /**
     * Moves the stones according to game rules.
     *
     * @param gameId Identifier of Game
     * @param pitId Number of pit to move pebbles
     * @return Kalahah board object with the correspondent move
     */
    public Kalahah move(Integer gameId, Integer pitId) {
        if (exists(gameId)) {
            Kalahah board = this.games.get(gameId);
            board.move(pitId);
            this.games.put(gameId, board);
            return this.games.get(gameId);
        } else {
            throw new RuntimeException("Invalid gameId!");
        }
    }

    /**
     * Get Game
     *
     * @param gameId Identifier of Game
     * @return Kalahah board object
     */
    public Kalahah getGame(Integer gameId) {
        if (exists(gameId)) {
            return this.games.get(gameId);
        } else {
            throw new RuntimeException("Invalid gameId!");
        }
    }
}
