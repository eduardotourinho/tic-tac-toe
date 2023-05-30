package com.gamemechanics.tictactoe.application.services;

import com.gamemechanics.tictactoe.application.domain.models.*;
import com.gamemechanics.tictactoe.application.domain.services.GamePlayService;
import com.gamemechanics.tictactoe.application.ports.in.command.PlayRoundCommand;
import com.gamemechanics.tictactoe.application.ports.out.FindGameUseCase;
import com.gamemechanics.tictactoe.application.ports.out.SaveGameUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameManagerTest {

    @Mock
    private GamePlayService gamePlayServiceMock;

    @Mock
    private SaveGameUseCase saveGameUseCaseMock;

    @Mock
    private FindGameUseCase findGameUseCaseMock;

    @InjectMocks
    private GameManager subject;

    @Test
    public void shouldCreateANewGameWithEmptyBoard() {
        var game = Game.builder()
                .id(UUID.randomUUID())
                .state(Game.State.PLAYING)
                .board(new Board(3, 3))
                .build();

        when(saveGameUseCaseMock.startNewGame(3))
                .thenReturn(game);

        var actualGame = subject.startNewGame(3);

        assertEquals(game.getBoard(), actualGame.getBoard());
        assertEquals(game.getLastPlay(), actualGame.getLastPlay());
        assertTrue(actualGame.getBoard().getGrid()
                .values().stream()
                .allMatch(player -> player == PlayerEnum.EMPTY));

        verifyNoMoreInteractions(saveGameUseCaseMock);
        verifyNoInteractions(findGameUseCaseMock);
        verifyNoInteractions(gamePlayServiceMock);
    }

    @Test
    public void shouldLoadTheGameWithCorrectParameters() {
        final var gameId = UUID.randomUUID();
        final var lastPlay = new Play(PlayerEnum.X, new Position(1,1));

        final var board = new Board(3, 3);
        board.add(lastPlay);

        final var game = Game.builder()
                .id(gameId)
                .state(Game.State.PLAYING)
                .board(board)
                .lastPlay(lastPlay)
                .build();

        final var playPosition = new Position(1,1);

        when(findGameUseCaseMock.loadGame(gameId))
                .thenReturn(game);

        var actualGame = subject.loadGame(gameId);

        assertEquals(gameId, actualGame.getId());
        assertEquals(PlayerEnum.X, actualGame.getLastPlay().player());
        assertEquals(Game.State.PLAYING, actualGame.getState());
        assertEquals(PlayerEnum.X, actualGame.getBoard().getPlayerAt(playPosition));

        verifyNoMoreInteractions(findGameUseCaseMock);
        verifyNoInteractions(gamePlayServiceMock);
    }

    @Test
    public void shouldAllowPlayARoundSuccessfully() {
        var playPosition = new Position(1, 1);
        var board = new Board(3, 3);
        board.add(PlayerEnum.X, playPosition);

        var gameId = UUID.randomUUID();
        var lastPlay = new Play(PlayerEnum.X, playPosition);
        var game = Game.builder()
                .id(gameId)
                .state(Game.State.PLAYING)
                .board(board)
                .lastPlay(lastPlay)
                .build();

        when(findGameUseCaseMock.loadGame(gameId))
                .thenReturn(game);
        when(saveGameUseCaseMock.saveGameState(game))
                .thenReturn(game);
        when(gamePlayServiceMock.playRound(game, PlayerEnum.X, playPosition))
                .thenReturn(game);

        var playCommand = new PlayRoundCommand(gameId, "X", 1, 1);
        var actual = subject.playRound(playCommand);

        assertEquals(gameId, actual.getId());
        assertEquals(PlayerEnum.X, actual.getLastPlay().player());
        assertEquals(Game.State.PLAYING, actual.getState());
        assertEquals(PlayerEnum.X, actual.getBoard().getPlayerAt(playPosition));

        verifyNoMoreInteractions(findGameUseCaseMock);
        verifyNoMoreInteractions(saveGameUseCaseMock);
    }

}