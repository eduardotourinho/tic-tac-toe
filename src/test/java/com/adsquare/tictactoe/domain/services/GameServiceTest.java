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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameValidator gameValidatorMock;
    @Mock
    private BoardValidator boardValidatorMock;
    @Mock
    private PlayValidator playValidatorMock;
    @Mock
    private GameLoaderPort gameLoaderPortMock;
    @Mock
    private SaveGameUseCase saveGameUseCaseMock;

    @InjectMocks
    private GameService subject;

    @Test
    public void shouldCreateANewGameWithEmptyBoard() {
        var game = Game.builder()
                .id(UUID.randomUUID())
                .state(Game.State.PLAYING)
                .board(new Board(3, 3))
                .build();

        when(saveGameUseCaseMock.startNewGame(3))
                .thenReturn(game);

        var actualGame = subject.startGame(3);

        assertEquals(game.getBoard(), actualGame.getBoard());
        assertEquals(game.getLastPlay(), actualGame.getLastPlay());
        assertTrue(actualGame.getBoard().getGrid()
                .values().stream()
                .allMatch(player -> player == PlayerEnum.EMPTY));
    }

    @Test
    public void shouldLoadTheGameSuccessfully() {
        var gameId = UUID.randomUUID();
        var board = new Board(3,3);
        var lastPlay = new Play(PlayerEnum.X, new Position(1,1));
        board.add(lastPlay.player(), lastPlay.position());

        var game = Game.builder()
                .id(gameId)
                .state(Game.State.PLAYING)
                .board(board)
                .lastPlay(lastPlay)
                .build();

        when(gameLoaderPortMock.loadGame(gameId))
                .thenReturn(game);

        var actualGame = subject.loadGame(gameId);

        assertEquals(gameId, actualGame.getId());
        assertEquals(PlayerEnum.X, actualGame.getLastPlay().player());
        assertEquals(Game.State.PLAYING, actualGame.getState());
        assertEquals(board, actualGame.getBoard());
    }

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

        when(gameLoaderPortMock.loadGame(gameId))
                .thenReturn(game);
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
        when(saveGameUseCaseMock.saveGameState(game))
                .thenReturn(game);

        var actualGame = subject.play(gameId, PlayerEnum.X, playPosition);

        assertEquals(gameId, actualGame.getId());
        assertEquals(PlayerEnum.X, actualGame.getLastPlay().player());
        assertEquals(Game.State.PLAYING, actualGame.getState());
        assertEquals(PlayerEnum.X, actualGame.getBoard().getPlayerAt(playPosition));

        verify(gameLoaderPortMock, times(1))
                .loadGame(gameId);
        verify(saveGameUseCaseMock, times(1))
                .saveGameState(game);
        verifyNoMoreInteractions(saveGameUseCaseMock);
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

        when(gameLoaderPortMock.loadGame(gameId))
                .thenReturn(game);
        when(gameValidatorMock.gameIsValid(game))
                .thenReturn(true);
        when(playValidatorMock.playIsValid(game, PlayerEnum.X))
                .thenReturn(true);
        when(boardValidatorMock.playIsValid(game.getBoard(), playPosition))
                .thenReturn(true);
        when(playValidatorMock.hasPlayerWon(game.getBoard(), PlayerEnum.X))
                .thenReturn(true);
        when(saveGameUseCaseMock.saveGameState(game))
                .thenReturn(game);

        var actualGame = subject.play(gameId, PlayerEnum.X, playPosition);

        assertEquals(gameId, actualGame.getId());
        assertEquals(PlayerEnum.X, actualGame.getLastPlay().player());
        assertEquals(Game.State.WIN, actualGame.getState());
        assertEquals(PlayerEnum.X, actualGame.getBoard().getPlayerAt(playPosition));

        verify(gameLoaderPortMock, times(1))
                .loadGame(gameId);
        verify(saveGameUseCaseMock, times(1))
                .saveGameState(game);
        verifyNoMoreInteractions(saveGameUseCaseMock);
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

        when(gameLoaderPortMock.loadGame(gameId))
                .thenReturn(game);
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
        when(saveGameUseCaseMock.saveGameState(game))
                .thenReturn(game);

        var actualGame = subject.play(gameId, PlayerEnum.X, playPosition);

        assertEquals(gameId, actualGame.getId());
        assertEquals(PlayerEnum.X, actualGame.getLastPlay().player());
        assertEquals(Game.State.FINISHED, actualGame.getState());
        assertEquals(PlayerEnum.X, actualGame.getBoard().getPlayerAt(playPosition));

        verify(gameLoaderPortMock, times(1))
                .loadGame(gameId);
        verify(saveGameUseCaseMock, times(1))
                .saveGameState(game);
        verifyNoMoreInteractions(saveGameUseCaseMock);
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

        when(gameLoaderPortMock.loadGame(gameId))
                .thenReturn(game);
        when(gameValidatorMock.gameIsValid(game))
                .thenReturn(false);

        assertThrows(GameNotAvailableException.class,
                () -> subject.play(gameId, PlayerEnum.X, new Position(1,1)));
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

        when(gameLoaderPortMock.loadGame(gameId))
                .thenReturn(game);
        when(gameValidatorMock.gameIsValid(game))
                .thenReturn(true);
        when(boardValidatorMock.playIsValid(board, nextPlayPosition))
                .thenReturn(true);
        when(playValidatorMock.playIsValid(game, PlayerEnum.X))
                .thenReturn(false);

        assertThrows(PlayerAlreadyPlayedException.class,
                () -> subject.play(gameId, PlayerEnum.X, nextPlayPosition));
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


        when(gameLoaderPortMock.loadGame(gameId))
                .thenReturn(game);
        when(gameValidatorMock.gameIsValid(game))
                .thenReturn(true);
        when(boardValidatorMock.playIsValid(game.getBoard(), playPosition))
                .thenReturn(false);

        assertThrows(BoardGridNotEmptyException.class,
                () -> subject.play(gameId, PlayerEnum.X, playPosition));
    }
}
