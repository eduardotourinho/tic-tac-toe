package com.adsquare.tictactoe.domain.services.validators;

import com.adsquare.tictactoe.domain.models.Board;
import com.adsquare.tictactoe.domain.models.Game;
import com.adsquare.tictactoe.domain.models.PlayerEnum;
import com.adsquare.tictactoe.domain.models.Position;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Component
public class PlayValidator {

    public boolean playIsValid(Game game, PlayerEnum player) {
        return (game.getLastPlay() == null || game.getLastPlay().player() != player);
    }

    public boolean hasPlayerWon(@NonNull Board board, @NonNull final PlayerEnum player) {
        var rowWon = rowWon(board, player);
        if (rowWon) {
            return true;
        }

        var colWon = colWon(board, player);
        if (colWon) {
            return true;
        }

        return diagsWon(board, player);
    }

    private boolean rowWon(Board board, PlayerEnum player) {
        var won = false;

        // Check rows
        for (var row = 0; row < board.getRows(); row++) {
            final int rowNum = row;

            won = board.getGrid().entrySet().stream()
                    .filter(entry -> entry.getKey().row() == rowNum)
                    .allMatch(entry -> entry.getValue() == player);

            if (won) {
                break;
            }
        }

        return won;
    }

    private boolean colWon(Board board, PlayerEnum player) {
        var won = false;

        for (var col = 0; col < board.getColumns(); col++) {
            final int colNum = col;

            won = board.getGrid().entrySet().stream()
                    .filter(entry -> entry.getKey().column() == colNum)
                    .allMatch(entry -> entry.getValue() == player);

            if (won) {
                break;
            }
        }

        return won;
    }

    private boolean diagsWon(Board board, PlayerEnum player) {
        var diag1Won = IntStream.range(0, board.getRows())
                .mapToObj(index -> new Position(index, index))
                .allMatch(position -> board.getPlayerAt(position) == player);

        var diag2Won = IntStream.range(0, board.getRows())
                .mapToObj(index -> new Position(index, board.getColumns() - index - 1))
                .allMatch(position -> board.getPlayerAt(position) == player);

        return diag1Won || diag2Won;
    }
}
