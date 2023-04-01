package com.adsquare.tictactoe.application.domain.services.validators;

import com.adsquare.tictactoe.application.domain.models.Game;
import org.springframework.stereotype.Component;

@Component
public class GameValidator {

    public boolean gameIsValid(Game game) {
        return game.getState() == Game.State.PLAYING;
    }
}
