package com.adsquare.tictactoe.domain.exceptions;

public class FailedToCreateNewGameException extends RuntimeException {
    public FailedToCreateNewGameException(Throwable exception) {
        super("Failed to create a new game", exception);
    }
}
