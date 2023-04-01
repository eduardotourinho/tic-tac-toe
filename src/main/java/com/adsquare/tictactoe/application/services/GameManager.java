package com.adsquare.tictactoe.application.services;

import com.adsquare.tictactoe.application.exceptions.FailedToCreateNewGameException;
import com.adsquare.tictactoe.application.domain.models.Game;
import com.adsquare.tictactoe.application.domain.models.PlayerEnum;
import com.adsquare.tictactoe.application.domain.models.Position;
import com.adsquare.tictactoe.application.domain.services.GamePlayService;
import com.adsquare.tictactoe.application.exceptions.GameNotExistException;
import com.adsquare.tictactoe.application.ports.in.ManageGameUseCase;
import com.adsquare.tictactoe.application.ports.in.command.PlayRoundCommand;
import com.adsquare.tictactoe.application.ports.out.FindGameUseCase;
import com.adsquare.tictactoe.application.ports.out.SaveGameUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameManager implements ManageGameUseCase {

    private final GamePlayService gamePlayService;

    private final FindGameUseCase findGameUseCase;
    private final SaveGameUseCase saveGameUseCase;

    @Override
    public Game startNewGame(int boardSize) {
        try {
            return saveGameUseCase.startNewGame(boardSize);
        } catch (RuntimeException exception) {
            throw new FailedToCreateNewGameException(exception);
        }
    }

    @Override
    public Game loadGame(UUID gameId) {
        var game = findGameUseCase.loadGame(gameId);

        if (game == null) {
            throw new GameNotExistException(gameId);
        }

        return game;
    }

    @Override
    public Game playRound(PlayRoundCommand play) {
        var game = loadGame(play.gameId());

        return saveGameUseCase.saveGameState(
                gamePlayService.playRound(game,
                        PlayerEnum.valueOf(play.player()),
                        new Position(play.row(), play.column()))
        );
    }
}
