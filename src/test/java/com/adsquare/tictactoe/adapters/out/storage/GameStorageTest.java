package com.adsquare.tictactoe.adapters.out.storage;

import com.adsquare.tictactoe.adapters.out.storage.repositories.GameRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("integration-test")
class GameStorageTest {

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

        subject.saveGameState(game);
    }
}