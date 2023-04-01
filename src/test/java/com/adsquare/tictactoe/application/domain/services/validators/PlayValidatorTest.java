package com.adsquare.tictactoe.application.domain.services.validators;

import com.adsquare.tictactoe.application.domain.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayValidatorTest {

    private PlayValidator subject;

    @BeforeEach
    public void setUp() {
        subject = new PlayValidator();
    }

    @Test
    public void shouldReturnFalseWhenLastPlayerIsTheSame() {
        var board = new Board(3, 3);
        var lastPlay = new Play(PlayerEnum.O, new Position(1,1));
        var game = Game.builder()
                .state(Game.State.PLAYING)
                .board(board)
                .lastPlay(lastPlay)
                .build();

        assertFalse(subject.playIsValid(game, PlayerEnum.O));
    }

    @ParameterizedTest
    @EnumSource(value = PlayerEnum.class, names = { "X", "EMPTY" })
    public void shouldReturnTrueWhenLastPlayIsNotTheSameOrEmpty(PlayerEnum lastPlayer) {
        var board = new Board(3, 3);
        var lastPlay = new Play(lastPlayer, new Position(1,1));
        var game = Game.builder()
                .state(Game.State.PLAYING)
                .board(board)
                .lastPlay(lastPlay)
                .build();

        assertTrue(subject.playIsValid(game, PlayerEnum.O));
    }

    @Test
    public void shouldReturnTrueWhenLastPlayIsNull() {
        var board = new Board(3, 3);
        var game = Game.builder()
                .state(Game.State.PLAYING)
                .board(board)
                .lastPlay(null)
                .build();

        assertTrue(subject.playIsValid(game, PlayerEnum.O));
    }

    @ParameterizedTest
    @MethodSource("winGameRowTestParams")
    public void shouldWinTheGameWhenAllRowsAreEqual(List<Position> boardPositions, Position nextPlay) {
        final var board = new Board(3, 3);
        boardPositions.forEach(position -> board.add(PlayerEnum.X, position));

        board.add(PlayerEnum.X, nextPlay);

        assertTrue(subject.hasPlayerWon(board, PlayerEnum.X));
    }

    @ParameterizedTest
    @MethodSource("winGameRowTestParams")
    public void shouldNotWinTheGameWhenOneRowIsDifferent(List<Position> boardPositions, Position nextPlay) {
        final var board = new Board(3, 3);
        boardPositions.forEach(position -> board.add(PlayerEnum.X, position));

        board.add(PlayerEnum.O, nextPlay);

        assertFalse(subject.hasPlayerWon(board, PlayerEnum.O));
    }

    @ParameterizedTest
    @MethodSource("winGameColsTestParams")
    public void shouldWinTheGameWhenAllColumnsAreEqual(List<Position> boardPositions, Position nextPlay) {
        final var board = new Board(3, 3);
        boardPositions.forEach(position -> board.add(PlayerEnum.X, position));

        board.add(PlayerEnum.X, nextPlay);

        assertTrue(subject.hasPlayerWon(board, PlayerEnum.X));
    }

    @ParameterizedTest
    @MethodSource("winGameColsTestParams")
    public void shouldNotWinTheGameWhenOneColumnIsDifferent(List<Position> boardPositions, Position nextPlay) {
        final var board = new Board(3, 3);
        boardPositions.forEach(position -> board.add(PlayerEnum.X, position));

        board.add(PlayerEnum.O, nextPlay);

        assertFalse(subject.hasPlayerWon(board, PlayerEnum.O));
    }

    @ParameterizedTest
    @MethodSource("winGameDiagsTestParams")
    public void shouldWinTheGameWhenAllDiagonalsAreEqual(List<Position> boardPositions, Position nextPlay) {
        final var board = new Board(3, 3);
        boardPositions.forEach(position -> board.add(PlayerEnum.X, position));

        board.add(PlayerEnum.X, nextPlay);

        assertTrue(subject.hasPlayerWon(board, PlayerEnum.X));
    }

    @ParameterizedTest
    @MethodSource("winGameDiagsTestParams")
    public void shouldNotWinTheGameWhenAllDiagonalsAreDifferent(List<Position> boardPositions, Position nextPlay) {
        final var board = new Board(3, 3);
        boardPositions.forEach(position -> board.add(PlayerEnum.X, position));

        board.add(PlayerEnum.O, nextPlay);

        assertFalse(subject.hasPlayerWon(board, PlayerEnum.O));
    }

    public static Stream<Arguments> winGameRowTestParams() {
        return Stream.of(
                Arguments.of(
                        List.of(new Position(0,0), new Position(0, 1)),
                        new Position(0,2)
                ),
                Arguments.of(
                        List.of(new Position(1,0), new Position(1, 1)),
                        new Position(1,2)
                ),
                Arguments.of(
                        List.of(new Position(2,0), new Position(2, 1)),
                        new Position(2,2)
                )
        );
    }

    public static Stream<Arguments> winGameColsTestParams() {
        return Stream.of(
                Arguments.of(
                        List.of(new Position(0,0), new Position(1, 0)),
                        new Position(2,0)
                ),
                Arguments.of(
                        List.of(new Position(0,1), new Position(1, 1)),
                        new Position(2,1)
                ),
                Arguments.of(
                        List.of(new Position(0,2), new Position(1, 2)),
                        new Position(2,2)
                )
        );
    }

    public static Stream<Arguments> winGameDiagsTestParams() {
        return Stream.of(
                Arguments.of(
                        List.of(new Position(0,0), new Position(1, 1)),
                        new Position(2,2)
                ),
                Arguments.of(
                        List.of(new Position(0,2), new Position(1, 1)),
                        new Position(2,0)
                )
        );
    }

}
