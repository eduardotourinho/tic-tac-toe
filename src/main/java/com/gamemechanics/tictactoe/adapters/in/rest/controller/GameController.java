package com.gamemechanics.tictactoe.adapters.in.rest.controller;

import com.gamemechanics.tictactoe.adapters.in.rest.configs.TicTacToeConfig;
import com.gamemechanics.tictactoe.adapters.in.rest.mappers.RequestMapper;
import com.gamemechanics.tictactoe.adapters.in.rest.models.GameResponse;
import com.gamemechanics.tictactoe.adapters.in.rest.models.PlayRequest;
import com.gamemechanics.tictactoe.adapters.in.rest.mappers.ResponseMapper;
import com.gamemechanics.tictactoe.application.ports.in.ManageGameUseCase;
import com.gamemechanics.tictactoe.application.ports.in.GamePlayUseCase;
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
    private final ManageGameUseCase manageGameUseCase;
    private final GamePlayUseCase gamePlayUseCase;

    private final RequestMapper requestMapper;
    private final ResponseMapper responseMapper;

    @PostMapping(value = "/game")
    public GameResponse newGame() {
        return responseMapper.gameResponseMapper(manageGameUseCase.createGame(config.getBoardSize()));
    }

    @GetMapping(value = "/game/{gameId}")
    public GameResponse getStatus(@Valid @PathVariable UUID gameId) {
        var game = manageGameUseCase.loadGame(gameId);

        return responseMapper.gameResponseMapper(game);
    }

    @PostMapping(value = "/game/{gameId}/play")
    public GameResponse play(@PathVariable UUID gameId,
                             @Valid @RequestBody PlayRequest play) {
        var game = gamePlayUseCase.playRound(requestMapper.toCommand(gameId, play));

        return responseMapper.gameResponseMapper(game);
    }
}
