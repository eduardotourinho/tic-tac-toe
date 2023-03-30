package com.adsquare.tictactoe.domain.services;


import com.adsquare.tictactoe.domain.exceptions.BoardGridNotEmptyException;
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
        var game = saveGameUseCase.startNewGame(boardSize);

        var board = new Board(boardSize, boardSize);
        game.setBoard(board);

        return game;
    }

    public Game loadGame(@NonNull UUID gameId) {
        var game = gameLoaderPort.loadGame(gameId);

        if (game == null) {
            throw new GameNotAvailableException(gameId);
        }

        return game;
    }

    public Game play(@NonNull UUID gameId, @NonNull PlayerEnum player, @NonNull Position position) {

        var game = loadGame(gameId);

        if (!gameValidator.gameIsValid(game)) {
            throw new GameNotAvailableException(gameId);
        }

        // 2. Check player turn
        if (!playValidator.playIsValid(game, player)) {
            throw new PlayerAlreadyPlayedException(player);
        }

        // 3. Check the board if the play position is already taken
        if (!boardValidator.playIsValid(game.getBoard(), position)) {
            throw new BoardGridNotEmptyException(position);
        }

        // 4. Update game state
        return updateGameState(game,player, position);
    }

    private Game updateGameState(@NonNull Game game, @NonNull PlayerEnum player, @NonNull Position position) {
        var lastPlay = new Play(player, position);

        game.setLastPlay(lastPlay);
        game.getBoard().add(lastPlay);

        if (playValidator.hasPlayerWon(game.getBoard(), player)) {
            game.setState(Game.State.FINISHED);
        }

        // 5. Save the board and return the new board state
        return saveGameUseCase.saveGameState(game);
    }
}
