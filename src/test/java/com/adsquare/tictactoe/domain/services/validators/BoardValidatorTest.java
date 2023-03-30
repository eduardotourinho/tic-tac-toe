package com.adsquare.tictactoe.domain.services.validators;

import com.adsquare.tictactoe.domain.models.Board;
import com.adsquare.tictactoe.domain.models.PlayerEnum;
import com.adsquare.tictactoe.domain.models.Position;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BoardValidatorTest {

    private BoardValidator subject;

    @BeforeEach
    public void setup() {
        subject = new BoardValidator();
    }

    @Test
    public void shouldReturnTrueOnValidPosition() {
        var board = new Board(3, 3);
        var position = new Position(1, 1);

        assertTrue(subject.playIsValid(board, position));
    }

    @ParameterizedTest
    @MethodSource("outOfBoundsTestParams")
    public void shouldReturnFalseOnOutOfBoundsPosition(int playRow, int playCol) {
        var board = new Board(3, 3);
        var position = new Position(playRow, playCol);

        assertFalse(subject.playIsValid(board, position));
    }

    @Test
    public void shouldReturnFalseOnInvalidPosition() {
        var board = new Board(3, 3);
        var position = new Position(1, 1);

        board.add(PlayerEnum.X, position);

        assertFalse(subject.playIsValid(board, position));
    }

    public static Stream<Arguments> outOfBoundsTestParams() {
        return Stream.of(
                Arguments.of(-1, -1),
                Arguments.of(1, -1),
                Arguments.of(-1, 0),
                Arguments.of(1, 4),
                Arguments.of(4, 2),
                Arguments.of(6, 5)
        );
    }
}