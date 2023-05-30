package com.gamemechanics.tictactoe.application.domain.services;

import com.gamemechanics.tictactoe.application.domain.exceptions.GridPositionNotEmpty;
import com.gamemechanics.tictactoe.application.domain.exceptions.GameAlreadyFinishedException;
import com.gamemechanics.tictactoe.application.domain.exceptions.PlayerAlreadyPlayedException;
import com.gamemechanics.tictactoe.application.domain.models.*;
import com.gamemechanics.tictactoe.application.domain.services.validators.BoardValidator;
import com.gamemechanics.tictactoe.application.domain.services.validators.GameValidator;
import com.gamemechanics.tictactoe.application.domain.services.validators.PlayValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GamePlayServiceTest {

    @Mock
    private GameValidator gameValidatorMock;
    @Mock
    private BoardValidator boardValidatorMock;
    @Mock
    private PlayValidator playValidatorMock;

    @InjectMocks
    private GamePlayService subject;

    @Test
    public void shouldAllowPlayerPlayWithoutErrorsAndSaveGameState() {
        final var gameId = UUID.randomUUID();
        final var playPosition = new Position(1, 1);

        var game = Game.builder()
                .id(gameId)
                .state(Game.State.PLAYING)
                .board(new Board(3,3))
                .lastPlay(null)
                .build();

        when(gameValidatorMock.gameIsValid(game))
                .thenReturn(true);
        when(playValidatorMock.playIsValid(game, PlayerEnum.X))
                .thenReturn(true);
        when(boardValidatorMock.playIsValid(game.getBoard(), playPosition))
                .thenReturn(true);
        when(playValidatorMock.hasPlayerWon(game.getBoard(), PlayerEnum.X))
                .thenReturn(false);
        when(boardValidatorMock.hasEmptySpaces(game.getBoard()))
                .thenReturn(true);

        var actualGame = subject.playRound(game, PlayerEnum.X, playPosition);

        assertEquals(gameId, actualGame.getId());
        assertEquals(PlayerEnum.X, actualGame.getLastPlay().player());
        assertEquals(Game.State.PLAYING, actualGame.getState());
        assertEquals(PlayerEnum.X, actualGame.getBoard().getPlayerAt(playPosition));
    }

    @Test
    public void shouldWinGameWithoutErrorsAndSaveGameState() {
        final var gameId = UUID.randomUUID();
        final var playPosition = new Position(1, 1);

        var game = Game.builder()
                .id(gameId)
                .state(Game.State.PLAYING)
                .board(new Board(3,3))
                .lastPlay(null)
                .build();

        when(gameValidatorMock.gameIsValid(game))
                .thenReturn(true);
        when(playValidatorMock.playIsValid(game, PlayerEnum.X))
                .thenReturn(true);
        when(boardValidatorMock.playIsValid(game.getBoard(), playPosition))
                .thenReturn(true);
        when(playValidatorMock.hasPlayerWon(game.getBoard(), PlayerEnum.X))
                .thenReturn(true);

        var actualGame = subject.playRound(game, PlayerEnum.X, playPosition);

        assertEquals(gameId, actualGame.getId());
        assertEquals(PlayerEnum.X, actualGame.getLastPlay().player());
        assertEquals(Game.State.WIN, actualGame.getState());
        assertEquals(PlayerEnum.X, actualGame.getBoard().getPlayerAt(playPosition));
    }

    @Test
    public void shouldFinishGameWithoutErrorsAndSaveGameState() {
        final var gameId = UUID.randomUUID();
        final var playPosition = new Position(1, 1);

        var game = Game.builder()
                .id(gameId)
                .state(Game.State.PLAYING)
                .board(new Board(3,3))
                .lastPlay(null)
                .build();

        when(gameValidatorMock.gameIsValid(game))
                .thenReturn(true);
        when(playValidatorMock.playIsValid(game, PlayerEnum.X))
                .thenReturn(true);
        when(boardValidatorMock.playIsValid(game.getBoard(), playPosition))
                .thenReturn(true);
        when(playValidatorMock.hasPlayerWon(game.getBoard(), PlayerEnum.X))
                .thenReturn(false);
        when(boardValidatorMock.hasEmptySpaces(game.getBoard()))
                .thenReturn(false);

        var actualGame = subject.playRound(game, PlayerEnum.X, playPosition);

        assertEquals(gameId, actualGame.getId());
        assertEquals(PlayerEnum.X, actualGame.getLastPlay().player());
        assertEquals(Game.State.FINISHED, actualGame.getState());
        assertEquals(PlayerEnum.X, actualGame.getBoard().getPlayerAt(playPosition));
    }


    @Test
    public void shouldThrowGameNotAvailableExceptionWhenPlayingInAFinishedGame() {
        final var gameId = UUID.randomUUID();

        var game = Game.builder()
                .id(gameId)
                .state(Game.State.FINISHED)
                .board(new Board(3,3))
                .lastPlay(null)
                .build();

        when(gameValidatorMock.gameIsValid(game))
                .thenReturn(false);

        assertThrows(GameAlreadyFinishedException.class,
                () -> subject.playRound(game, PlayerEnum.X, new Position(1,1)));
    }

    @Test
    public void shouldThrowPlayerAlreadyPlayedExceptionWhenPlayerAlreadyPlayed() {
        final var gameId = UUID.randomUUID();
        final var nextPlayPosition = new Position(1,1);
        var board = new Board(3,3);
        board.add(PlayerEnum.X, new Position(0, 1));

        var game = Game.builder()
                .id(gameId)
                .state(Game.State.FINISHED)
                .board(board)
                .lastPlay(null)
                .build();

        when(gameValidatorMock.gameIsValid(game))
                .thenReturn(true);
        when(boardValidatorMock.playIsValid(board, nextPlayPosition))
                .thenReturn(true);
        when(playValidatorMock.playIsValid(game, PlayerEnum.X))
                .thenReturn(false);

        assertThrows(PlayerAlreadyPlayedException.class,
                () -> subject.playRound(game, PlayerEnum.X, nextPlayPosition));
    }

    @Test
    public void shouldThrowBoardGridNotEmptyExceptionWhenPlayerPlaysOnFilledGrid() {
        final var gameId = UUID.randomUUID();
        final var playPosition = new Position(1,1);

        var board = new Board(3,3);
        board.add(PlayerEnum.X, new Position(1,1));

        var game = Game.builder()
                .id(gameId)
                .state(Game.State.FINISHED)
                .board(board)
                .lastPlay(null)
                .build();

        when(gameValidatorMock.gameIsValid(game))
                .thenReturn(true);
        when(boardValidatorMock.playIsValid(game.getBoard(), playPosition))
                .thenReturn(false);

        assertThrows(GridPositionNotEmpty.class,
                () -> subject.playRound(game, PlayerEnum.X, playPosition));
    }
}
