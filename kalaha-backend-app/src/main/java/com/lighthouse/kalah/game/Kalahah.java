package com.lighthouse.kalah.game;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/***
 * This class contains the Kalahah Board game logic
 *
 * @author Jorge Ortiz
 * @since Oktober 2021
 */
public class Kalahah {

    public static final Integer KALAH_PLAYER_SOUTH = 7;
    public static final Integer KALAH_PLAYER_NORTH = 14;
    public static final int GAMES_ORIGIN = 1000;
    public static final int GAMES_LIMIT = 10000;

    private final Integer stones;
    private Integer gameId;
    private Player turn;
    private ConcurrentHashMap<Integer, Integer> status;

    public Kalahah(final Integer stones) {
        this.stones = stones;
        this.turn = Player.SOUTH;
        this.status = new ConcurrentHashMap<>();
        this.setGameId(ThreadLocalRandom.current().nextInt(GAMES_ORIGIN, GAMES_LIMIT));
        initStonesBoard(this.status, this.stones);
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(final Integer gameId) {
        this.gameId = gameId;
    }

    public ConcurrentHashMap<Integer, Integer> getStatus() {
        return status;
    }

    public void setStatus(ConcurrentHashMap<Integer, Integer> status) {
        this.status = status;
    }

    public void move(final Integer pitId) {
        if (gameHasEnded() == KalahahStatus.IN_PROGRESS) {
            if (isValidPit(pitId)) {
                if (pitHasItems(pitId)) {
                    if (isValidToPickStonesFromPit(pitId, turn)) {
                        int pitStones = this.status.get(pitId);
                        //pickup the stones
                        this.status.put(pitId, 0);
                        //place stones counter-clockwise
                        int pitDestination = pitId;
                        //do this while I have stones in my hand
                        while (pitStones > 0) {
                            pitDestination = pitDestination % 14 + 1;

                            boolean wasEmpty = isEmptyPit(pitDestination);
                            //only on my valid pits
                            if ((turn.equals(Player.SOUTH) && pitDestination != KALAH_PLAYER_NORTH) ||
                                    (turn.equals(Player.NORTH) && pitDestination != KALAH_PLAYER_SOUTH))
                                this.status.put(pitDestination, this.status.get(pitDestination) + 1);
                            else
                                pitStones++;
                            if (pitStones == 1 && wasEmpty) {
                                // pull opponent stones if last n stone lands on empty pit and
                                // the opponent has stones else do nothing the stone was placed yet.
                                processLastStoneNEmptyPitToPullOpponentStones(pitDestination, this.turn);
                            }

                            pitStones--;
                        }
                        // rule, if last stone is placed on kalah you have another turn
                        if (turn.equals(Player.SOUTH)) {
                            if (isLastStonePlacedOnKalah(pitDestination, this.turn)) {
                                this.turn = Player.SOUTH;
                            } else {
                                this.turn = Player.NORTH;
                            }
                        } else if (turn.equals(Player.NORTH)) {
                            if (isLastStonePlacedOnKalah(pitDestination, this.turn)) {
                                this.turn = Player.NORTH;
                            } else {
                                this.turn = Player.SOUTH;
                            }
                        }
                        // rule, The game is over as soon as one of the sides run out of stones
                        switch (gameHasEnded()) {
                            case SOUTH_FINISHED:
                                moveRemainingStonesToKalah(KALAH_PLAYER_NORTH);
                                break;
                            case NORTH_FINISHED:
                                moveRemainingStonesToKalah(KALAH_PLAYER_SOUTH);
                                break;
                        }
                    } else {
                        throw new RuntimeException("Player " + turn.toString() + " cannot move pieces from this pit");
                    }
                } else {
                    throw new RuntimeException("Pit doesn't have items");
                }
            } else {
                throw new IllegalArgumentException("Invalid move this is a Kalahah");
            }
        } else {
            int south = this.status.get(7);
            int north = this.status.get(14);
            String msg = "Player";
            if (south > north)
                msg += " South win!";
            else if (south < north)
                msg += " North win!";
            else if (south == north)
                msg += "s are in a tie game";
            throw new RuntimeException("Game has ended, no more moves! " + msg);
        }
    }

    public Player getTurn() {
        return turn;
    }

    public KalahahStatus gameHasEnded() {
        int southEmpty = 0;
        int northEmpty = 0;
        for (int s = 1; s <= 6; s++)
            southEmpty += this.status.get(s);
        for (int n = 8; n <= 13; n++)
            northEmpty += this.status.get(n);
        if (southEmpty == 0)
            return KalahahStatus.SOUTH_FINISHED;
        if (northEmpty == 0)
            return KalahahStatus.NORTH_FINISHED;
        return KalahahStatus.IN_PROGRESS;
    }

    public boolean isValidToPickStonesFromPit(final Integer pitId, final Player turn) {
        return (turn.equals(Player.SOUTH) && pitId.intValue() >= 1 && pitId.intValue() <= 6) ||
                (turn.equals(Player.NORTH) && pitId.intValue() >= 8 && pitId.intValue() <= 13);
    }

    public boolean isValidPit(final Integer pitId) {
        return !pitId.equals(KALAH_PLAYER_SOUTH) && !pitId.equals(KALAH_PLAYER_NORTH);
    }

    private void moveRemainingStonesToKalah(final Integer pitId) {
        if (pitId.equals(KALAH_PLAYER_NORTH)) {
            int northStones = 0;
            //pickup the stones
            for (int n = 8; n <= 13; n++) {
                northStones += this.status.get(n);
                this.status.put(n, 0);
            }
            this.status.put(KALAH_PLAYER_NORTH, this.status.get(KALAH_PLAYER_NORTH) + northStones);

        } else if (pitId.equals(KALAH_PLAYER_SOUTH)) {
            int southStones = 0;
            //pickup the stones
            for (int s = 1; s <= 6; s++) {
                southStones += this.status.get(s);
                this.status.put(s, 0);
            }
            this.status.put(KALAH_PLAYER_SOUTH, this.status.get(KALAH_PLAYER_SOUTH) + southStones);
        }
    }

    private boolean pitHasItems(final Integer pitId) {
        return this.status.get(pitId).intValue() > 0;
    }

    private boolean isEmptyPit(final Integer pitId) {
        return this.status.get(pitId).equals(0);
    }

    private boolean isLastStonePlacedOnKalah(final Integer pitId, final Player turn) {
        if (turn == Player.SOUTH)
            return pitId.equals(KALAH_PLAYER_SOUTH);
        if (turn == Player.NORTH)
            return pitId.equals(KALAH_PLAYER_NORTH);
        return false;
    }

    private boolean isThereOponentStones(final Integer pitId) {
        return this.status.get(14 - pitId.intValue()) > 0;
    }

    private void processLastStoneNEmptyPitToPullOpponentStones(final Integer pitId, final Player turn) {
        switch (turn) {
            case SOUTH:
                if (pitId.intValue() >= 1 && pitId.intValue() <= 6 && isThereOponentStones(pitId)) {
                    this.status.put(KALAH_PLAYER_SOUTH, this.status.get(KALAH_PLAYER_SOUTH) + this.status.get(14 - pitId.intValue()) + 1);
                    //clear the pits
                    this.status.put(14 - pitId.intValue(), 0);
                    this.status.put(pitId.intValue(), 0);
                }
                break;
            case NORTH:
                if (pitId.intValue() >= 8 && pitId.intValue() <= 13 && isThereOponentStones(pitId)) {
                    this.status.put(KALAH_PLAYER_NORTH, this.status.get(KALAH_PLAYER_NORTH) + this.status.get(14 - pitId.intValue()) + 1);
                    //clear the pits
                    this.status.put(14 - pitId.intValue(), 0);
                    this.status.put(pitId.intValue(), 0);
                }
                break;
        }
    }

    private void initStonesBoard(ConcurrentHashMap<Integer, Integer> board, final Integer stones) {
        for (Integer pitId = 1; pitId < 15; pitId++) {
            if (isValidPit(pitId)) {
                board.put(pitId, stones);
            } else {
                board.put(pitId, 0);
            }
        }
    }
}
