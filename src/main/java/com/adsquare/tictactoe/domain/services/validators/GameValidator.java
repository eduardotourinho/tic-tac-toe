package com.adsquare.tictactoe.domain.services.validators;

import com.adsquare.tictactoe.domain.models.Game;
import org.springframework.stereotype.Component;

@Component
public class GameValidator {

    public boolean gameIsValid(Game game) {
        return game.getState() == Game.State.PLAYING;
    }
}
