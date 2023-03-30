package com.adsquare.tictactoe.domain.ports;


import com.adsquare.tictactoe.domain.models.Game;

import java.util.UUID;

public interface GameLoaderPort {

    Game loadGame(UUID gameId);
}
