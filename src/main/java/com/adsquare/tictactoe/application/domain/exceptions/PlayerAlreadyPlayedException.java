package com.adsquare.tictactoe.application.domain.exceptions;

import com.adsquare.tictactoe.application.domain.models.PlayerEnum;

public class PlayerAlreadyPlayedException extends RuntimeException {

    public PlayerAlreadyPlayedException(PlayerEnum player) {
        super(String.format("Player '%s' already played", player));
    }
}
