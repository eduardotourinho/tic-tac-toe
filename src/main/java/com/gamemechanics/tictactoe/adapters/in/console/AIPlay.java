package com.gamemechanics.tictactoe.adapters.in.console;

import com.gamemechanics.tictactoe.application.domain.models.Game;
import com.gamemechanics.tictactoe.application.domain.models.PlayerEnum;
import com.gamemechanics.tictactoe.application.domain.models.Position;

public interface AIPlay {

    Position generateAIMove(Game game, PlayerEnum currentPlayer);
}
