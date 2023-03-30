package com.adsquare.tictactoe.domain.exceptions;

import java.util.UUID;

public class GameNotAvailableException extends RuntimeException {

    public GameNotAvailableException(UUID gameId) {
        super(String.format("Game %s does not exist", gameId));
    }
}
