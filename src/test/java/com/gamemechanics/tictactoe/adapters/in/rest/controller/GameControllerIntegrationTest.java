package com.gamemechanics.tictactoe.adapters.in.rest.controller;

import com.gamemechanics.tictactoe.adapters.in.rest.models.ErrorResponse;
import com.gamemechanics.tictactoe.adapters.in.rest.models.GameResponse;
import com.gamemechanics.tictactoe.adapters.in.rest.models.PlayRequest;
import com.gamemechanics.tictactoe.application.ports.in.ManageGameUseCase;
import com.gamemechanics.tictactoe.application.domain.models.Board;
import com.gamemechanics.tictactoe.application.domain.models.Game;
import com.gamemechanics.tictactoe.application.domain.models.PlayerEnum;
import com.gamemechanics.tictactoe.application.domain.models.Position;
import com.gamemechanics.tictactoe.application.ports.in.PlayGameUseCase;
import com.gamemechanics.tictactoe.application.ports.in.command.PlayRoundCommand;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.annotation.DirtiesContext;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
class GameControllerIntegrationTest {

    @Value(value = "${local.server.port}")
    private int port;

    @MockBean
    private ManageGameUseCase manageGameUseCaseMock;

    @MockBean
    private PlayGameUseCase playGameUseCase;

    @Autowired
    private TestRestTemplate restTemplate;

    private Game testGame;

    @BeforeEach
    public void setUp() {
        var uuid = UUID.randomUUID();
        testGame = Game.builder()
                .id(uuid)
                .state(Game.State.PLAYING)
                .board(new Board(3,3))
                .build();

        when(manageGameUseCaseMock.startNewGame(3))
                .thenReturn(testGame);

        when(manageGameUseCaseMock.loadGame(uuid))
                .thenReturn(testGame);
    }

    @Test
    public void shouldCreateANewGameAndReturnJson() {
        var gameResponse = restTemplate.postForEntity(url("/game"), null, GameResponse.class).getBody();

        assertNotNull(gameResponse);
        assertNotNull(gameResponse.getId());
        assertEquals("PLAYING", gameResponse.getState());
        assertEquals(3, gameResponse.getBoard().getBoardSize());
        assertTrue(() -> gameResponse.getBoard().getGrid().stream()
                .allMatch(grid -> grid.getPlayer().equals("EMPTY")));
    }

    @Test
    public void shouldLoadGameAndReturnJson() {
        var createUrl = url("/game");
        var gameResponse = restTemplate.postForEntity(createUrl, null, GameResponse.class).getBody();

        assertNotNull(gameResponse);

        var loadUrl = url(String.format("/game/%s", gameResponse.getId()));
        var loadResponse = restTemplate
                .getForEntity(loadUrl, GameResponse.class)
                .getBody();

        assertNotNull(loadResponse);
        assertNotNull(loadResponse.getId());
        assertEquals("PLAYING", loadResponse.getState());
        assertEquals(3, loadResponse.getBoard().getBoardSize());
        assertTrue(() -> gameResponse.getBoard().getGrid().stream()
                .allMatch(grid -> grid.getPlayer().equals("EMPTY")));
    }

    @Test
    public void shouldPlayerPlayGameAndReturnJson() {
        testGame.getBoard().add(PlayerEnum.X, new Position(1, 1));
        var playRoundCommand = new PlayRoundCommand(testGame.getId(), "X", 1, 1);
        when(playGameUseCase.playRound(playRoundCommand))
                .thenReturn(testGame);

        var createUrl = url("/game");
        var gameResponse = restTemplate.postForEntity(createUrl, null, GameResponse.class).getBody();
        assertNotNull(gameResponse);

        final var gameId = gameResponse.getId();
        var playRequest = new PlayRequest("X", 1, 1);
        var playUrl = url(String.format("/game/%s/play", gameId));

        var playResponse = restTemplate.postForEntity(playUrl, playRequest, GameResponse.class)
                .getBody();

        assertNotNull(playResponse);
        assertEquals(gameId, playResponse.getId());
        assertEquals("PLAYING", playResponse.getState());

        var actualPlay = gameResponse.getBoard().getGrid().stream()
                .filter(grid -> grid.getRow() == 1 && grid.getColumn() == 1)
                .findFirst();

        assertTrue(actualPlay.isPresent());
        assertEquals("X", actualPlay.get().getPlayer());
    }

    @ParameterizedTest
    @MethodSource("errorResponseParametersSource")
    public void shouldReturnErrorResponseOnValidationFail(String player, int row, int column, String expectedMessage) {
        var playRequest = new PlayRequest(player, row, column);
        var playUrl = url(String.format("/game/%s/play", UUID.randomUUID()));

        var errorResponse = restTemplate.postForEntity(playUrl, playRequest, ErrorResponse.class)
                .getBody();

        assertNotNull(errorResponse);
        assertEquals("Some fields are not valid", errorResponse.getMessage());
        assertEquals(400, errorResponse.getStatusCode());
        assertEquals(expectedMessage, errorResponse.getDetails().get(0));
    }

    public static Stream<Arguments> errorResponseParametersSource() {
        return Stream.of(
                Arguments.of("Invalid", 0, 0, "player: Player must be 'X' or 'O'"),
                Arguments.of("X", -1, 1, "row: must be greater than or equal to 0"),
                Arguments.of("X", 10, 1, "row: must be less than or equal to 2"),
                Arguments.of("O", 1, -1, "column: must be greater than or equal to 0"),
                Arguments.of("O", 1, 10, "column: must be less than or equal to 2")
        );
    }

    private String url(@NotNull final String path) {
        return String.format("http://localhost:%d/%s", port, path);
    }
}