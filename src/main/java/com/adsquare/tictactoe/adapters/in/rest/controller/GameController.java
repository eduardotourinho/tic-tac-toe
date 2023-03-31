package com.adsquare.tictactoe.adapters.in.rest.controller;

import com.adsquare.tictactoe.adapters.in.rest.configs.TicTacToeConfig;
import com.adsquare.tictactoe.adapters.in.rest.models.GameResponse;
import com.adsquare.tictactoe.adapters.in.rest.models.PlayRequest;
import com.adsquare.tictactoe.adapters.in.rest.mappers.ResponseMapper;
import com.adsquare.tictactoe.domain.models.PlayerEnum;
import com.adsquare.tictactoe.domain.models.Position;
import com.adsquare.tictactoe.domain.services.GameService;
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
    private final GameService gameService;

    private final ResponseMapper responseMapper;

    @PostMapping(value = "/game")
    public GameResponse newGame() {
        return responseMapper.gameResponseMapper(gameService.startGame(config.getBoardSize()));
    }

    @GetMapping(value = "/game/{gameId}")
    public GameResponse getStatus(@Valid @PathVariable UUID gameId) {
        var game = gameService.loadGame(gameId);

        return responseMapper.gameResponseMapper(game);
    }

    @PostMapping(value = "/game/{gameId}/play")
    public GameResponse play(@PathVariable UUID gameId,
                             @Valid @RequestBody PlayRequest play) {

        var playPosition = new Position(play.getRow(), play.getColumn());
        var game = gameService.play(gameId, PlayerEnum.valueOf(play.getPlayer().toUpperCase()), playPosition);

        return responseMapper.gameResponseMapper(game);
    }
}
