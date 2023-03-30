package com.adsquare.tictactoe.adapters.in.rest.controller;

import com.adsquare.tictactoe.adapters.in.rest.configs.TicTacToeConfig;
import com.adsquare.tictactoe.adapters.in.rest.models.GameResponse;
import com.adsquare.tictactoe.adapters.in.rest.models.PlayRequest;
import com.adsquare.tictactoe.adapters.in.rest.mappers.ResponseMapper;
import com.adsquare.tictactoe.domain.models.PlayerEnum;
import com.adsquare.tictactoe.domain.services.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
public class GameController {

    private final TicTacToeConfig config;
    private final GameService gameService;

    private final ResponseMapper responseMapper;

    @PostMapping(value = "/game", produces = APPLICATION_JSON_VALUE)
    public GameResponse newGame() {
        return responseMapper.gameResponseMapper(gameService.startGame(config.getBoardSize()));
    }

    @GetMapping(value = "/game/{gameId}", produces = APPLICATION_JSON_VALUE)
    public GameResponse getStatus(@Validated @PathVariable String gameId) {
        var game = gameService.loadGame(UUID.fromString(gameId));
        return responseMapper.gameResponseMapper(game);
    }

    @PostMapping(value = "/game/{gameId}/play", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public GameResponse play(@PathVariable String gameId,
                             @Validated @RequestBody PlayRequest play) {
        var game = gameService.play(UUID.fromString(gameId), PlayerEnum.valueOf(play.player()), play.position());
        return responseMapper.gameResponseMapper(game);
    }
}
