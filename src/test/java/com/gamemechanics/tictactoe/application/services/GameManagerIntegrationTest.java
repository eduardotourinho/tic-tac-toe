package com.gamemechanics.tictactoe.application.services;

import com.gamemechanics.tictactoe.application.domain.models.*;
import com.gamemechanics.tictactoe.application.exceptions.GameNotExistException;
import com.gamemechanics.tictactoe.application.ports.in.command.PlayRoundCommand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("integration-test")
class GameManagerIntegrationTest {

    @Autowired
    private GameManager subject;

    @Test
    public void shouldCreateANewGameWithEmptyBoard() {
        var actualGame = subject.startNewGame(3);

        assertNotNull(actualGame.getBoard());
        assertNull(actualGame.getLastPlay());
        assertTrue(actualGame.getBoard().getGrid()
                .values().stream()
                .allMatch(player -> player == PlayerEnum.EMPTY));
    }

    @Test
    public void shouldLoadTheGame() {
        var newGame = subject.startNewGame(3);

        var actualGame = subject.loadGame(newGame.getId());

        assertEquals(newGame.getId(), actualGame.getId());
        assertEquals(Game.State.PLAYING, actualGame.getState());
    }

    @Test
    public void shouldThrowGameNotExistException() {
        assertThrows(GameNotExistException.class,
                () -> subject.loadGame(UUID.randomUUID()));
    }

    @Test
    public void shouldAllowPlayARoundSuccessfullyAndReturnNewGameState() {
        var newGame = subject.startNewGame(3);

        var playCommand = new PlayRoundCommand(newGame.getId(), "X", 1, 1);
        var actual = subject.playRound(playCommand);

        assertEquals(newGame.getId(), actual.getId());
        assertEquals(PlayerEnum.X, actual.getLastPlay().player());
        assertEquals(Game.State.PLAYING, actual.getState());
        assertEquals(PlayerEnum.X, actual.getBoard().getPlayerAt(new Position(1, 1)));
    }
}
