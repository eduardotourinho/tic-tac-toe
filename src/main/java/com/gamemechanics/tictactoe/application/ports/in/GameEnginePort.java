package com.gamemechanics.tictactoe.application.ports.in;

import com.gamemechanics.tictactoe.application.domain.models.Game;
import com.gamemechanics.tictactoe.application.domain.models.GameConfig;

public interface GameEnginePort {

    Game startGame(GameConfig config, Game game);
}
