package com.gamemechanics.tictactoe.application.ports.out;


import com.gamemechanics.tictactoe.application.domain.models.Game;

import java.util.UUID;

public interface FindGameUseCase {

    Game loadGame(UUID gameId);
}
