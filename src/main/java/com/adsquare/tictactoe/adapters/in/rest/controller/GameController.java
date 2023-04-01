package com.adsquare.tictactoe.adapters.in.rest.controller;

import com.adsquare.tictactoe.adapters.in.rest.configs.TicTacToeConfig;
import com.adsquare.tictactoe.adapters.in.rest.mappers.RequestMapper;
import com.adsquare.tictactoe.adapters.in.rest.models.GameResponse;
import com.adsquare.tictactoe.adapters.in.rest.models.PlayRequest;
import com.adsquare.tictactoe.adapters.in.rest.mappers.ResponseMapper;
import com.adsquare.tictactoe.application.services.GameManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
public class GameController {

    private final TicTacToeConfig config;
    private final GameManager gameManager;

    private final RequestMapper requestMapper;
    private final ResponseMapper responseMapper;

    @PostMapping(value = "/game")
    public GameResponse newGame() {
        return responseMapper.gameResponseMapper(gameManager.startNewGame(config.getBoardSize()));
    }

    @GetMapping(value = "/game/{gameId}")
    public GameResponse getStatus(@Valid @PathVariable UUID gameId) {
        var game = gameManager.loadGame(gameId);

        return responseMapper.gameResponseMapper(game);
    }

    @PostMapping(value = "/game/{gameId}/play")
    public GameResponse play(@PathVariable UUID gameId,
                             @Valid @RequestBody PlayRequest play) {
        var game = gameManager.playRound(requestMapper.toCommand(gameId, play));

        return responseMapper.gameResponseMapper(game);
    }
}
