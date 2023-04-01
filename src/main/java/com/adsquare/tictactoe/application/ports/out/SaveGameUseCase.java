package com.adsquare.tictactoe.application.ports.out;

import com.adsquare.tictactoe.application.domain.models.Game;

public interface SaveGameUseCase {

    Game startNewGame(int boardSize);

    Game saveGameState(Game game);
}
