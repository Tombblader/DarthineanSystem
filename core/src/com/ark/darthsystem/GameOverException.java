package com.ark.darthsystem;

public class GameOverException extends RuntimeException {

    public GameOverException() {
        super("You Lose.");
    }

    public GameOverException(String message) {
        super(message);
    }

    public GameOverException(String message, Throwable cause) {
        super(message, cause);
    }

    public GameOverException(Throwable cause) {
        super(cause);
    }

}
