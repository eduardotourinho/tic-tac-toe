package com.gamemechanics.tictactoe.application.services;

import com.gamemechanics.tictactoe.application.exceptions.FailedToCreateNewGameException;
import com.gamemechanics.tictactoe.application.domain.models.Game;
import com.gamemechanics.tictactoe.application.domain.models.PlayerEnum;
import com.gamemechanics.tictactoe.application.domain.services.GameTurnService;
import com.gamemechanics.tictactoe.application.exceptions.GameNotExistException;
import com.gamemechanics.tictactoe.application.ports.in.ManageGameUseCase;
import com.gamemechanics.tictactoe.application.ports.in.GamePlayUseCase;
import com.gamemechanics.tictactoe.application.ports.in.command.PlayRoundCommand;
import com.gamemechanics.tictactoe.application.ports.out.FindGameUseCase;
import com.gamemechanics.tictactoe.application.ports.out.SaveGameUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameManagerPlay implements ManageGameUseCase, GamePlayUseCase {

    private final GameTurnService gameTurnService;

    private final FindGameUseCase findGameUseCase;
    private final SaveGameUseCase saveGameUseCase;

    @Override
    public Game createGame(int boardSize) {
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

        return saveGameUseCase.saveGameState(tryRound(game, play));
    }

    @Override
    public Game tryRound(Game game, PlayRoundCommand play) {
        return gameTurnService.playTurn(game,
                PlayerEnum.valueOf(play.player()),
                play.playPosition());
    }
}
