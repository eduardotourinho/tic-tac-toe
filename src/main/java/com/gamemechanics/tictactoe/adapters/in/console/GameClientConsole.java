package com.gamemechanics.tictactoe.adapters.in.console;

import com.gamemechanics.tictactoe.application.domain.models.Game;
import com.gamemechanics.tictactoe.application.ports.in.GameEnginePort;
import com.gamemechanics.tictactoe.application.ports.in.ManageGameUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("!integration-test")
public class GameClientConsole implements ApplicationListener<ApplicationReadyEvent> {

    private static final int GAME_BOARD_SIZE = 3;

    private final GameUI gameUI;
    private final ManageGameUseCase gameManager;
    private final GameEnginePort gameEnginePort;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        startNewGame();
    }

    private void startNewGame() {
        var config = gameUI.setupGame();
        var game = gameManager.createGame(GAME_BOARD_SIZE);

        game = gameEnginePort.startGame(config, game);

        if (hasGameFinished(game)) {
            gameUI.handleFinishedGame(game);
            if (gameUI.playAgain()) {
                startNewGame();
            }
        }
    }

    private boolean hasGameFinished(Game game) {
        return game.getState() != Game.State.PLAYING;
    }
}
