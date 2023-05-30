package com.gamemechanics.tictactoe.adapters.in.rest.mappers;

import com.gamemechanics.tictactoe.adapters.in.rest.models.GameResponse;
import com.gamemechanics.tictactoe.application.domain.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class ResponseMapperTest {

    private ResponseMapper subject;

    @BeforeEach
    public void setUp() {
        subject = new ResponseMapper();
    }

    @Test
    public void shouldMapResponseForOngoingGame() {
        var board = new Board(3, 3);

        var game = Game.builder()
                .id(UUID.randomUUID())
                .state(Game.State.PLAYING)
                .board(board)
                .lastPlay(null)
                .build();

        var response = subject.gameResponseMapper(game);

        assertEquals(game.getState().name(), response.getState());
        assertEquals(game.getId().toString(), response.getId());
        assertNull(response.getWinner());
        assertEquals(board.getRows(), response.getBoard().getBoardSize());

        var expectedPlays = board.getGrid().entrySet().stream()
                .map(entry -> GameResponse.Play.builder()
                        .row(entry.getKey().row())
                        .column(entry.getKey().column())
                        .player(entry.getValue().name())
                        .build())
                .toList();
        assertIterableEquals(expectedPlays, response.getBoard().getGrid());
    }

    @Test
    public void shouldMapResponseForFinishedGame() {
        var board = new Board(3, 3);
        board.add(PlayerEnum.X, new Position(1,1));

        var game = Game.builder()
                .id(UUID.randomUUID())
                .state(Game.State.FINISHED)
                .board(board)
                .lastPlay(new Play(PlayerEnum.X, new Position(1,1)))
                .build();

        var response = subject.gameResponseMapper(game);

        assertEquals(game.getState().name(), response.getState());
        assertEquals(game.getId().toString(), response.getId());
        assertNull(response.getWinner());
        assertEquals(board.getRows(), response.getBoard().getBoardSize());

        var expectedPlays = board.getGrid().entrySet().stream()
                .map(entry -> GameResponse.Play.builder()
                        .row(entry.getKey().row())
                        .column(entry.getKey().column())
                        .player(entry.getValue().name())
                        .build())
                .toList();
        assertIterableEquals(expectedPlays, response.getBoard().getGrid());
    }

    @Test
    public void shouldMapResponseForWinnerGame() {
        var board = new Board(3, 3);
        board.add(PlayerEnum.X, new Position(1,1));

        var game = Game.builder()
                .id(UUID.randomUUID())
                .state(Game.State.WIN)
                .board(board)
                .lastPlay(new Play(PlayerEnum.X, new Position(1,1)))
                .build();

        var response = subject.gameResponseMapper(game);

        assertEquals(game.getState().name(), response.getState());
        assertEquals(game.getId().toString(), response.getId());
        assertEquals(PlayerEnum.X.name(), response.getWinner());
        assertEquals(board.getRows(), response.getBoard().getBoardSize());

        var expectedPlays = board.getGrid().entrySet().stream()
                .map(entry -> GameResponse.Play.builder()
                        .row(entry.getKey().row())
                        .column(entry.getKey().column())
                        .player(entry.getValue().name())
                        .build())
                .toList();
        assertIterableEquals(expectedPlays, response.getBoard().getGrid());
    }
}