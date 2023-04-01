package com.adsquare.tictactoe.application.domain.services.validators;

import com.adsquare.tictactoe.application.domain.models.Board;
import com.adsquare.tictactoe.application.domain.models.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameValidatorTest {

    private GameValidator subject;

    @BeforeEach
    void setUp() {
        subject = new GameValidator();
    }

    @Test
    void shouldReturnTrueIfGameIsInProgress() {
        var game = Game.builder()
                .board(new Board(3,3))
                .state(Game.State.PLAYING)
                .build();

        assertTrue(subject.gameIsValid(game));
    }

    @Test
    void shouldReturnFalseIfGameIsFinished() {
        var game = Game.builder()
                .board(new Board(3,3))
                .state(Game.State.FINISHED)
                .build();

        assertFalse(subject.gameIsValid(game));
    }
}
