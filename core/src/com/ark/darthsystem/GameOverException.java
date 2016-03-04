package com.ark.darthsystem;

/**
 *
 * @author Keven
 */
public class GameOverException extends RuntimeException {

    /**
     *
     */
    public GameOverException() {
        super("You Lose.");
    }

    /**
     *
     * @param message
     */
    public GameOverException(String message) {
        super(message);
    }

    /**
     *
     * @param message
     * @param cause
     */
    public GameOverException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     *
     * @param cause
     */
    public GameOverException(Throwable cause) {
        super(cause);
    }

}
