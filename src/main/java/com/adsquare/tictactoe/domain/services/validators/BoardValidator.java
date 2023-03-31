package com.adsquare.tictactoe.domain.services.validators;

import com.adsquare.tictactoe.domain.models.Board;
import com.adsquare.tictactoe.domain.models.PlayerEnum;
import com.adsquare.tictactoe.domain.models.Position;
import org.springframework.stereotype.Component;

@Component
public class BoardValidator {

    public boolean hasEmptySpaces(Board board) {
        return board.getGrid().values().stream()
                .anyMatch(playerEnum -> playerEnum == PlayerEnum.EMPTY);
    }

    public boolean playIsValid(Board board, Position position) {

        if (!hasEmptySpaces(board) || isOutOfBounds(board, position)) {
            return false;
        }

        var currentBoardPosition = board.getPlayerAt(position);
        return currentBoardPosition == PlayerEnum.EMPTY;
    }

    private boolean isOutOfBounds(Board board, Position position) {
        return (position.row() < 0 || position.row() >= board.getRows())
                || (position.column() < 0 || position.column() >= board.getColumns());
    }
}
