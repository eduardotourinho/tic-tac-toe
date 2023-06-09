package com.gamemechanics.tictactoe.application.domain.exceptions;

import com.gamemechanics.tictactoe.application.domain.models.Position;

public class GridPositionNotEmpty extends RuntimeException {
    public GridPositionNotEmpty(Position position) {
        super(String.format("Board position (%d, %d) not empty", position.row(), position.column()));
    }
}
