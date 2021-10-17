package com.lighthouse.kalah.game;

/**
 * Used to handle the overall status of the board
 * IN_PROGRESS    : Both players still can make a move.
 * SOUTH_FINISHED : South player has six empty cups.
 * NORTH_FINISHED : North player has six empty cups.
 */
public enum KalahahStatus {
    IN_PROGRESS,
    SOUTH_FINISHED,
    NORTH_FINISHED
}
