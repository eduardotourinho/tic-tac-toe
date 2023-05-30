package com.gamemechanics.tictactoe.adapters.out.storage.exceptions;

import java.util.UUID;

public class GameNotFoundException extends RuntimeException {

    public GameNotFoundException(UUID gameId) {
        super(String.format("Game \"%s\" not found", gameId));
    }
}
