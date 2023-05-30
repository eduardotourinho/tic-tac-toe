package com.gamemechanics.tictactoe.application.ports.out;

import com.gamemechanics.tictactoe.application.domain.models.Game;

public interface SaveGameUseCase {

    Game startNewGame(int boardSize);

    Game saveGameState(Game game);
}
