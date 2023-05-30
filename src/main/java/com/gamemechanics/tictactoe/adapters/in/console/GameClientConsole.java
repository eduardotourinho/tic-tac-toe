package com.gamemechanics.tictactoe.adapters.in.console;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameClientConsole implements ApplicationListener<ApplicationReadyEvent> {

    private final GameEngine gameEngine;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        gameEngine.setupGame();
    }
}
