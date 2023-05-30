package com.gamemechanics.tictactoe.adapters.in.console;

import com.gamemechanics.tictactoe.application.domain.models.Game;
import com.gamemechanics.tictactoe.application.domain.models.PlayerEnum;
import com.gamemechanics.tictactoe.application.domain.models.Position;
import com.gamemechanics.tictactoe.application.ports.in.PlayGameUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AIPlayMinimax implements AIPlay {

    private final PlayGameUseCase playGameUseCase;

    @Override
    public Position generateAIMove(Game game, PlayerEnum currentPlayer) {
        throw new RuntimeException("Not yet implemented");
    }
}
