package com.gamemechanics.tictactoe.application.domain.services;

import com.gamemechanics.tictactoe.application.domain.exceptions.GridPositionNotEmpty;
import com.gamemechanics.tictactoe.application.domain.exceptions.GameAlreadyFinishedException;
import com.gamemechanics.tictactoe.application.domain.exceptions.PlayerAlreadyPlayedException;
import com.gamemechanics.tictactoe.application.domain.models.Board;
import com.gamemechanics.tictactoe.application.domain.models.Game;
import com.gamemechanics.tictactoe.application.domain.models.PlayerEnum;
import com.gamemechanics.tictactoe.application.domain.models.Position;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@ActiveProfiles("integration-test")
class GamePlayServiceIntegrationTest {

    @Autowired
    private GamePlayService subject;

    @Test
    public void shouldAllowPlayerPlayWithoutErrorsAndSaveGameState() {
        var gameId = UUID.randomUUID();
        var game = Game.builder()
                .id(gameId)
                .state(Game.State.PLAYING)
                .board(new Board(3,3))
                .lastPlay(null)
                .build();
        var playPosition = new Position(1, 1);

        var actualGame = subject.playRound(game, PlayerEnum.X, playPosition);

        assertEquals(gameId, actualGame.getId());
        assertEquals(PlayerEnum.X, actualGame.getLastPlay().player());
        assertEquals(Game.State.PLAYING, actualGame.getState());
        assertEquals(PlayerEnum.X, actualGame.getBoard().getPlayerAt(playPosition));
    }

    @Test
    public void shouldWinTheGameWithoutErrorsAndSaveGameState() {
        var gameId = UUID.randomUUID();
        var game = Game.builder()
                .id(gameId)
                .state(Game.State.PLAYING)
                .board(new Board(3,3))
                .lastPlay(null)
                .build();
        var playPosition = new Position(0, 2);

        assertDoesNotThrow(() -> {
            subject.playRound(game, PlayerEnum.X, new Position(0, 0));
            subject.playRound(game, PlayerEnum.O, new Position(1, 0));
            subject.playRound(game, PlayerEnum.X, new Position(0, 1));
            subject.playRound(game, PlayerEnum.O, new Position(1, 1));
        });

        var actualGame = subject.playRound(game, PlayerEnum.X, playPosition);

        assertEquals(gameId, actualGame.getId());
        assertEquals(PlayerEnum.X, actualGame.getLastPlay().player());
        assertEquals(Game.State.WIN, actualGame.getState());
        assertEquals(PlayerEnum.X, actualGame.getBoard().getPlayerAt(playPosition));
    }

    @Test
    public void shouldAllowPlayAndFinishGameWithoutErrorsAndSaveGameState() {
        var gameId = UUID.randomUUID();
        var game = Game.builder()
                .id(gameId)
                .state(Game.State.PLAYING)
                .board(new Board(3,3))
                .lastPlay(null)
                .build();
        var playPosition = new Position(2, 2);

        assertDoesNotThrow(() -> {
            subject.playRound(game, PlayerEnum.X, new Position(0, 0));
            subject.playRound(game, PlayerEnum.O, new Position(0, 1));
            subject.playRound(game, PlayerEnum.X, new Position(0, 2));
            subject.playRound(game, PlayerEnum.O, new Position(1, 1));
            subject.playRound(game, PlayerEnum.X, new Position(1, 0));
            subject.playRound(game, PlayerEnum.O, new Position(1, 2));
            subject.playRound(game, PlayerEnum.X, new Position(2, 1));
            subject.playRound(game, PlayerEnum.O, new Position(2, 0));
        });

        var actualGame = subject.playRound(game, PlayerEnum.X, playPosition);

        assertEquals(gameId, actualGame.getId());
        assertEquals(PlayerEnum.X, actualGame.getLastPlay().player());
        assertEquals(Game.State.FINISHED, actualGame.getState());
        assertEquals(PlayerEnum.X, actualGame.getBoard().getPlayerAt(playPosition));
    }

    @ParameterizedTest
    @EnumSource(value = Game.State.class, names = { "WIN", "FINISHED" })
    public void shouldThrowGameAlreadyFinishedException(Game.State playState) {
        var game = Game.builder()
                .id(UUID.randomUUID())
                .state(playState)
                .board(new Board(3,3))
                .lastPlay(null)
                .build();

        assertThrows(GameAlreadyFinishedException.class,
                () -> subject.playRound(game, PlayerEnum.X, new Position(1,1)));
    }

    @Test
    public void shouldThrowPlayerAlreadyPlayedExceptionWhenPlayerAlreadyPlayed() {
        var game = Game.builder()
                .id(UUID.randomUUID())
                .state(Game.State.PLAYING)
                .board(new Board(3,3))
                .lastPlay(null)
                .build();

        final var firstPlayPosition = new Position(0, 1);
        assertDoesNotThrow(() -> subject.playRound(game, PlayerEnum.X, firstPlayPosition));

        final var nextPlayPosition = new Position(1,1);
        assertThrows(PlayerAlreadyPlayedException.class,
                () -> subject.playRound(game, PlayerEnum.X, nextPlayPosition));
    }

    @Test
    public void shouldThrowGridPositionNotEmptyExceptionWhenPlayerPlaysOnFilledPosition() {
        var game = Game.builder()
                .id(UUID.randomUUID())
                .state(Game.State.PLAYING)
                .board(new Board(3,3))
                .lastPlay(null)
                .build();
        var playPosition = new Position(1,1);

        assertDoesNotThrow(() -> subject.playRound(game, PlayerEnum.X, playPosition));
        assertThrows(GridPositionNotEmpty.class,
                () -> subject.playRound(game, PlayerEnum.O, playPosition));
    }
}
