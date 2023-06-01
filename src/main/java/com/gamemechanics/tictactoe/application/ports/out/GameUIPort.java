package com.gamemechanics.tictactoe.application.ports.out;

import com.gamemechanics.tictactoe.application.domain.models.Game;
import com.gamemechanics.tictactoe.application.domain.models.PlayerEnum;
import com.gamemechanics.tictactoe.application.domain.models.Position;

public interface GameUIPort {

    void updateBoard(Game game);

    Position fetchMove(PlayerEnum currentPlayer);
}
