package com.ark.darthsystem;

/**
 *
 * @author Keven
 */
public class GameOverException extends RuntimeException {

    public GameOverException() {
        super("You Lose.");
    }

}
