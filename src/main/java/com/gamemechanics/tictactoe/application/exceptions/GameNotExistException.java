package com.gamemechanics.tictactoe.application.exceptions;

import java.util.UUID;

public class GameNotExistException extends RuntimeException {

    public GameNotExistException(UUID gameId) {
        super(String.format("The game [%s] was not found.", gameId));
    }

}
