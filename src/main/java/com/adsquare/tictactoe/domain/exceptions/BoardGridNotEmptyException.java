package com.adsquare.tictactoe.domain.exceptions;

import com.adsquare.tictactoe.domain.models.Position;

public class BoardGridNotEmptyException extends RuntimeException {
    public BoardGridNotEmptyException(Position position) {
        super(String.format("Grid [%d, %d] not empty", position.row(), position.column()));
    }
}
