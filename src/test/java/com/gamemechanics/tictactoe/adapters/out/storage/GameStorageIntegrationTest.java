package com.gamemechanics.tictactoe.adapters.out.storage;

import com.gamemechanics.tictactoe.adapters.out.storage.repositories.GameRepository;
import com.gamemechanics.tictactoe.adapters.out.storage.repositories.PlaysRepository;
import com.gamemechanics.tictactoe.application.domain.models.Play;
import com.gamemechanics.tictactoe.application.domain.models.PlayerEnum;
import com.gamemechanics.tictactoe.application.domain.models.Position;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("integration-test")
class GameStorageIntegrationTest {

    @Autowired
    PlaysRepository playsRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameStorage subject;

    @Test
    public void shouldCreateAndStoreANewGame() {
        var game = subject.startNewGame(3);

        assertTrue(gameRepository.existsById(game.getId()));
    }

    @Test
    public void shouldReturnNullOnGameNotFound() {
        assertNull(subject.loadGame(UUID.randomUUID()));
    }

    @Test
    public void shouldSaveGameStateCorrectly() {
        var game = subject.startNewGame(3);

        var playPosition = new Position(1, 1);
        var newPlay = new Play(PlayerEnum.X, playPosition);
        game.getBoard().add(newPlay);
        game.setLastPlay(newPlay);

        subject.saveGameState(game);

        var actualPlay = playsRepository.findByGameRowAndColum(game.getId(), playPosition.row(), playPosition.column());

        assertNotNull(actualPlay);
        assertEquals(newPlay.player().toString(), actualPlay.getPlayer());
    }

    @Test
    public void shouldNotSaveDuplicatedPlays() {
        var game = subject.startNewGame(3);
        final var playPosition = new Position(1, 1);

        var newPlay = new Play(PlayerEnum.X, playPosition);
        game.getBoard().add(newPlay);
        game.setLastPlay(newPlay);

        game = subject.saveGameState(game);

        var actualPlay = playsRepository.findByGameRowAndColum(game.getId(), playPosition.row(), playPosition.column());

        assertNotNull(actualPlay);
        assertEquals(newPlay.player().toString(), actualPlay.getPlayer());

        var otherPlay = new Play(PlayerEnum.O, playPosition);
        game.setLastPlay(newPlay);
        game.setLastPlay(otherPlay);

        final var gameState = game;
        assertThrows(DataIntegrityViolationException.class,
                () -> subject.saveGameState(gameState));
    }
}
