package com.adsquare.tictactoe.application.ports.out;


import com.adsquare.tictactoe.application.domain.models.Game;

import java.util.UUID;

public interface FindGameUseCase {

    Game loadGame(UUID gameId);
}
