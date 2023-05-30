package com.gamemechanics.tictactoe.application.ports.in;

import com.gamemechanics.tictactoe.application.domain.models.Game;
import com.gamemechanics.tictactoe.application.ports.in.command.PlayRoundCommand;

public interface PlayGameUseCase {

    Game playRound(PlayRoundCommand play);

    Game tryRound(PlayRoundCommand play);
}
