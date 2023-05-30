package com.gamemechanics.tictactoe.application.services;

import com.gamemechanics.tictactoe.application.exceptions.FailedToCreateNewGameException;
import com.gamemechanics.tictactoe.application.domain.models.Game;
import com.gamemechanics.tictactoe.application.domain.models.PlayerEnum;
import com.gamemechanics.tictactoe.application.domain.models.Position;
import com.gamemechanics.tictactoe.application.domain.services.GamePlayService;
import com.gamemechanics.tictactoe.application.exceptions.GameNotExistException;
import com.gamemechanics.tictactoe.application.ports.in.ManageGameUseCase;
import com.gamemechanics.tictactoe.application.ports.in.PlayGameUseCase;
import com.gamemechanics.tictactoe.application.ports.in.command.PlayRoundCommand;
import com.gamemechanics.tictactoe.application.ports.out.FindGameUseCase;
import com.gamemechanics.tictactoe.application.ports.out.SaveGameUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameManager implements ManageGameUseCase, PlayGameUseCase {

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
        return saveGameUseCase.saveGameState(tryRound(play));
    }

    @Override
    public Game tryRound(PlayRoundCommand play) {
        var game = loadGame(play.gameId());

        return gamePlayService.playRound(game,
                PlayerEnum.valueOf(play.player()),
                new Position(play.row(), play.column()));
    }
}
