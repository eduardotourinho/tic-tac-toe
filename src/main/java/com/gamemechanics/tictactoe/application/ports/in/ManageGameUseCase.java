package com.gamemechanics.tictactoe.application.ports.in;

import com.gamemechanics.tictactoe.application.domain.models.Game;

import java.util.UUID;

public interface ManageGameUseCase {

    Game startNewGame(int boardSize);

    Game loadGame(UUID gameId);
}
