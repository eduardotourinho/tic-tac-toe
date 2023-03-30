package com.adsquare.tictactoe.domain.ports;

import com.adsquare.tictactoe.domain.models.Game;

public interface SaveGameUseCase {

    Game startNewGame(int boardSize);

    Game saveGameState(Game game);
}
