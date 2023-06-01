package com.gamemechanics.tictactoe.application.domain.ai;

import com.gamemechanics.tictactoe.application.domain.models.PlayerPersonaEnum;
import com.gamemechanics.tictactoe.application.domain.models.Board;
import com.gamemechanics.tictactoe.application.domain.models.Game;
import com.gamemechanics.tictactoe.application.domain.models.PlayerEnum;
import com.gamemechanics.tictactoe.application.domain.models.Position;
import com.gamemechanics.tictactoe.application.domain.services.validators.BoardValidator;
import com.gamemechanics.tictactoe.application.domain.services.validators.PlayValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AIMinimax implements AIPlayerPort {
    private final static int GAME_BASE_SCORE = 10;

    private final PlayValidator playValidator;
    private final BoardValidator boardValidator;


    private record Score(double score, int depth) {}

    @Override
    public Position generateMove(Game game, PlayerEnum currentPlayer, PlayerPersonaEnum currentPlayerPersona) {
        Position bestPosition = null;
        var bestScore = Double.NEGATIVE_INFINITY;

        for (var emptyPosition : getEmptyBoardPositions(game.getBoard())) {
            game.getBoard().add(currentPlayer, emptyPosition);
            var positionScore = minimax(game, currentPlayer, getNextPlayer(currentPlayer), 1, false);
            game.getBoard().add(PlayerEnum.EMPTY, emptyPosition);

            if (positionScore.score > bestScore) {
                bestScore = positionScore.score;
                bestPosition = emptyPosition;
            }
        }

        return bestPosition;
    }

    private Score minimax(Game game, PlayerEnum currentPlayer, PlayerEnum nextPlayer, int depth, boolean isMaximizing) {
        var winner = getWinner(game.getBoard());
        if (winner != null) {
            var score = calculateScore(currentPlayer, winner, depth);

            return new Score(score, depth);
        }

        var bestDepth = Double.POSITIVE_INFINITY;
        var bestScore = isMaximizing ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;

        for (var emptyPosition : getEmptyBoardPositions(game.getBoard())) {
            game.getBoard().add(nextPlayer, emptyPosition);
            var currentScore = minimax(game, currentPlayer, getNextPlayer(nextPlayer), depth + 1, !isMaximizing);
            game.getBoard().add(PlayerEnum.EMPTY, emptyPosition);

            bestDepth = Math.min(bestDepth, depth);
            bestScore = isMaximizing ? Math.max(bestScore, currentScore.score) : Math.min(bestScore, currentScore.score);
        }

        return new Score(bestScore, depth);
    }

    private List<Position> getEmptyBoardPositions(Board board) {
        return board.getGrid().entrySet().stream()
                .filter(entry -> entry.getValue().equals(PlayerEnum.EMPTY))
                .map(Map.Entry::getKey)
                .toList();
    }

    private PlayerEnum getNextPlayer(PlayerEnum currentPlayer) {
        if (currentPlayer.equals(PlayerEnum.X)) {
            return PlayerEnum.O;
        }
        return PlayerEnum.X;
    }

    private PlayerEnum getWinner(Board board) {
        if (playValidator.hasPlayerWon(board, PlayerEnum.X)) {
            return PlayerEnum.X;
        } else if (playValidator.hasPlayerWon(board, PlayerEnum.O)) {
            return PlayerEnum.O;
        } else if (!boardValidator.hasEmptySpaces(board)) {
            return PlayerEnum.EMPTY;
        }

        return null;
    }

    private double calculateScore(PlayerEnum currentPlayer, PlayerEnum winner, int depth) {
        if (winner.equals(PlayerEnum.EMPTY)) {
            return 0;
        }

        return (double) (currentPlayer.equals(winner) ? GAME_BASE_SCORE : -GAME_BASE_SCORE) / depth;
    }
}
