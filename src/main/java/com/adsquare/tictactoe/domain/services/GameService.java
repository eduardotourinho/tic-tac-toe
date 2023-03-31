package com.adsquare.tictactoe.domain.services;


import com.adsquare.tictactoe.domain.exceptions.BoardGridNotEmptyException;
import com.adsquare.tictactoe.domain.exceptions.FailedToCreateNewGameException;
import com.adsquare.tictactoe.domain.exceptions.GameNotAvailableException;
import com.adsquare.tictactoe.domain.exceptions.PlayerAlreadyPlayedException;
import com.adsquare.tictactoe.domain.models.*;
import com.adsquare.tictactoe.domain.ports.GameLoaderPort;
import com.adsquare.tictactoe.domain.ports.SaveGameUseCase;
import com.adsquare.tictactoe.domain.services.validators.BoardValidator;
import com.adsquare.tictactoe.domain.services.validators.GameValidator;
import com.adsquare.tictactoe.domain.services.validators.PlayValidator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameValidator gameValidator;
    private final BoardValidator boardValidator;
    private final PlayValidator playValidator;

    private final GameLoaderPort gameLoaderPort;
    private final SaveGameUseCase saveGameUseCase;

    public Game startGame(int boardSize) {
        try {
            return saveGameUseCase.startNewGame(boardSize);
        } catch (RuntimeException exception) {
            throw new FailedToCreateNewGameException(exception);
        }
    }

    public Game loadGame(@NonNull UUID gameId) {
        var game = gameLoaderPort.loadGame(gameId);

        if (game == null) {
            throw new GameNotAvailableException(gameId);
        }

        return game;
    }

    @Transactional(noRollbackFor = {GameNotAvailableException.class, PlayerAlreadyPlayedException.class, BoardGridNotEmptyException.class})
    public Game play(@NonNull UUID gameId, @NonNull PlayerEnum player, @NonNull Position position) {

        // 1. load
        var game = loadGame(gameId);

        if (!gameValidator.gameIsValid(game)) {
            throw new GameNotAvailableException(gameId);
        }

        // 2. Check the board if the play position is already taken or the board is full
        if (!boardValidator.playIsValid(game.getBoard(), position)) {
            throw new BoardGridNotEmptyException(position);
        }

        // 3. Check player turn
        if (!playValidator.playIsValid(game, player)) {
            throw new PlayerAlreadyPlayedException(player);
        }

        // 4. Update game state
        return updateGameState(game, player, position);
    }

    private Game updateGameState(@NonNull Game game, @NonNull PlayerEnum player, @NonNull Position position) {
        var lastPlay = new Play(player, position);

        game.setLastPlay(lastPlay);
        game.getBoard().add(lastPlay);

        if (playValidator.hasPlayerWon(game.getBoard(), player)) {
            game.setState(Game.State.WIN);
        } else if (!boardValidator.hasEmptySpaces(game.getBoard())) {
            game.setState(Game.State.FINISHED);
        }

        return saveGameUseCase.saveGameState(game);
    }
}
