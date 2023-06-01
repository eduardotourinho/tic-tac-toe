package com.gamemechanics.tictactoe.application.domain.ai;

import com.gamemechanics.tictactoe.application.domain.models.PlayerPersonaEnum;
import com.gamemechanics.tictactoe.application.domain.models.Game;
import com.gamemechanics.tictactoe.application.domain.models.PlayerEnum;
import com.gamemechanics.tictactoe.application.domain.models.Position;

public interface AIPlayerPort {

    Position generateMove(Game game, PlayerEnum currentPlayer, PlayerPersonaEnum currentPlayerPersona);
}
