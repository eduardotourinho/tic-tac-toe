package com.adsquare.tictactoe.application.ports.in;

import com.adsquare.tictactoe.application.domain.models.Game;
import com.adsquare.tictactoe.application.ports.in.command.PlayRoundCommand;

import java.util.UUID;

public interface ManageGameUseCase {

    Game startNewGame(int boardSize);

    Game loadGame(UUID gameId);

    Game playRound(PlayRoundCommand play);
}
