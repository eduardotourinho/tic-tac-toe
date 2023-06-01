package com.gamemechanics.tictactoe.application.domain.services;

import com.gamemechanics.tictactoe.application.domain.models.GameConfig;
import com.gamemechanics.tictactoe.application.domain.models.PlayerPersonaEnum;
import com.gamemechanics.tictactoe.application.domain.models.Game;
import com.gamemechanics.tictactoe.application.domain.models.PlayerEnum;
import com.gamemechanics.tictactoe.application.domain.models.Position;
import com.gamemechanics.tictactoe.application.ports.in.GameEnginePort;
import com.gamemechanics.tictactoe.application.domain.ai.AIPlayerPort;
import com.gamemechanics.tictactoe.application.ports.out.GameUIPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameEngine implements GameEnginePort {

    private final GameUIPort gameUIPort;
    private final GameTurnService gameTurn;
    private final AIPlayerPort aiPlayerPort;

    @Override
    public Game startGame(GameConfig config, Game game) {
        var currentPlayer = PlayerEnum.X;

        while (game.getState() == Game.State.PLAYING) {
            var nextPlayerMove = getNextPlayerMove(game, currentPlayer, config);
            game = gameTurn.playTurn(game, currentPlayer, nextPlayerMove);
            currentPlayer = getNextPlayer(currentPlayer);

            gameUIPort.updateBoard(game);
        }

        return game;
    }

    private Position getNextPlayerMove(Game game, PlayerEnum currentPlayer, GameConfig config) {
        if (isCurrentPlayerIsHuman(currentPlayer, config)) {
            return gameUIPort.fetchMove(currentPlayer);
        } else {
            return aiPlayerPort.generateMove(game, currentPlayer, PlayerPersonaEnum.AI);
        }
    }

    private boolean isCurrentPlayerIsHuman(PlayerEnum currentPlayer, GameConfig gameConfig) {
        return ((currentPlayer.equals(PlayerEnum.X) && gameConfig.xPlayer().equals(PlayerPersonaEnum.HUMAN)) ||
                (currentPlayer.equals(PlayerEnum.O) && gameConfig.oPlayer().equals(PlayerPersonaEnum.HUMAN)));
    }

    private PlayerEnum getNextPlayer(PlayerEnum currentPlayer) {
        if (currentPlayer.equals(PlayerEnum.X)) {
            return PlayerEnum.O;
        }
        return PlayerEnum.X;
    }
}
