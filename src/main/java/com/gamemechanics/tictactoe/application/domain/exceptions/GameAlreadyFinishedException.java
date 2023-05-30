package com.gamemechanics.tictactoe.application.domain.exceptions;

import java.util.UUID;

public class GameAlreadyFinishedException extends RuntimeException {

    public GameAlreadyFinishedException(UUID gameId) {
        super(String.format("Game [%s] has finished already.", gameId));
    }
}
