package com.adsquare.tictactoe.domain.services;

import com.adsquare.tictactoe.domain.exceptions.BoardGridNotEmptyException;
import com.adsquare.tictactoe.domain.exceptions.GameNotAvailableException;
import com.adsquare.tictactoe.domain.exceptions.PlayerAlreadyPlayedException;
import com.adsquare.tictactoe.domain.models.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@ActiveProfiles("integration-test")
class GameServiceIntegrationTest {

    @Autowired
    private GameService subject;

    @Test
    public void shouldCreateANewGameWithEmptyBoard() {
        var game = Game.builder()
                .id(UUID.randomUUID())
                .state(Game.State.PLAYING)
                .board(new Board(3, 3))
                .build();

        var actualGame = subject.startGame(3);

        assertEquals(game.getBoard(), actualGame.getBoard());
        assertEquals(game.getLastPlay(), actualGame.getLastPlay());
        assertTrue(actualGame.getBoard().getGrid()
                .values().stream()
                .allMatch(player -> player == PlayerEnum.EMPTY));
    }

    @Test
    public void shouldLoadTheGameSuccessfully() {
        var game = subject.startGame(3);
        final var gameId = game.getId();
        final var playPosition = new Position(1,1);

        assertDoesNotThrow(() -> subject.play(gameId, PlayerEnum.X, playPosition));

        var actualGame = subject.loadGame(gameId);

        assertEquals(gameId, actualGame.getId());
        assertEquals(PlayerEnum.X, actualGame.getLastPlay().player());
        assertEquals(Game.State.PLAYING, actualGame.getState());
        assertEquals(PlayerEnum.X, actualGame.getBoard().getPlayerAt(playPosition));
    }

    @Test
    public void shouldAllowPlayerPlayWithoutErrorsAndSaveGameState() {
        var game = subject.startGame(3);
        final var gameId = game.getId();
        final var playPosition = new Position(1, 1);

        var actualGame = subject.play(gameId, PlayerEnum.X, playPosition);

        assertEquals(gameId, actualGame.getId());
        assertEquals(PlayerEnum.X, actualGame.getLastPlay().player());
        assertEquals(Game.State.PLAYING, actualGame.getState());
        assertEquals(PlayerEnum.X, actualGame.getBoard().getPlayerAt(playPosition));
    }

    @Test
    public void shouldWinTheGameWithoutErrorsAndSaveGameState() {
        var game = subject.startGame(3);
        var gameId = game.getId();
        var playPosition = new Position(0, 2);

        assertDoesNotThrow(() -> {
            subject.play(gameId, PlayerEnum.X, new Position(0, 0));
            subject.play(gameId, PlayerEnum.O, new Position(1, 0));
            subject.play(gameId, PlayerEnum.X, new Position(0, 1));
            subject.play(gameId, PlayerEnum.O, new Position(1, 1));
        });

        var actualGame = subject.play(gameId, PlayerEnum.X, playPosition);

        assertEquals(gameId, actualGame.getId());
        assertEquals(PlayerEnum.X, actualGame.getLastPlay().player());
        assertEquals(Game.State.WIN, actualGame.getState());
        assertEquals(PlayerEnum.X, actualGame.getBoard().getPlayerAt(playPosition));
    }

    @Test
    public void shouldAllowPlayAndFinishGameWithoutErrorsAndSaveGameState() {
        var game = subject.startGame(3);
        var gameId = game.getId();
        var playPosition = new Position(2, 2);

        assertDoesNotThrow(() -> {
            subject.play(gameId, PlayerEnum.X, new Position(0, 0));
            subject.play(gameId, PlayerEnum.O, new Position(0, 1));
            subject.play(gameId, PlayerEnum.X, new Position(0, 2));
            subject.play(gameId, PlayerEnum.O, new Position(1, 1));
            subject.play(gameId, PlayerEnum.X, new Position(1, 0));
            subject.play(gameId, PlayerEnum.O, new Position(1, 2));
            subject.play(gameId, PlayerEnum.X, new Position(2, 1));
            subject.play(gameId, PlayerEnum.O, new Position(2, 0));
        });

        var actualGame = subject.play(gameId, PlayerEnum.X, playPosition);

        assertEquals(gameId, actualGame.getId());
        assertEquals(PlayerEnum.X, actualGame.getLastPlay().player());
        assertEquals(Game.State.FINISHED, actualGame.getState());
        assertEquals(PlayerEnum.X, actualGame.getBoard().getPlayerAt(playPosition));
    }

    @Test
    public void shouldThrowGameNotAvailableExceptionWhenGameDoesNotExists() {
        final var gameId = UUID.randomUUID();

        assertThrows(GameNotAvailableException.class,
                () -> subject.play(gameId, PlayerEnum.X, new Position(1,1)));
    }

    @Test
    public void shouldThrowPlayerAlreadyPlayedExceptionWhenPlayerAlreadyPlayed() {
        var game = subject.startGame(3);
        var gameId = game.getId();

        final var firstPlayPosition = new Position(0, 1);
        assertDoesNotThrow(() -> subject.play(gameId, PlayerEnum.X, firstPlayPosition));

        final var nextPlayPosition = new Position(1,1);
        assertThrows(PlayerAlreadyPlayedException.class,
                () -> subject.play(gameId, PlayerEnum.X, nextPlayPosition));
    }

    @Test
    public void shouldThrowBoardGridNotEmptyExceptionWhenPlayerPlaysOnFilledPosition() {
        var game = subject.startGame(3);
        final var gameId = game.getId();
        final var playPosition = new Position(1,1);

        assertDoesNotThrow(() -> subject.play(gameId, PlayerEnum.X, playPosition));
        assertThrows(BoardGridNotEmptyException.class,
                () -> subject.play(gameId, PlayerEnum.O, playPosition));
    }
}
