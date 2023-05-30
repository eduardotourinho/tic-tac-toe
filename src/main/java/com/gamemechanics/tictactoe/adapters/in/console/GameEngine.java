package com.gamemechanics.tictactoe.adapters.in.console;

import com.gamemechanics.tictactoe.adapters.in.console.models.GameConfig;
import com.gamemechanics.tictactoe.adapters.in.console.models.PlayerPersonaEnum;
import com.gamemechanics.tictactoe.application.domain.models.Game;
import com.gamemechanics.tictactoe.application.domain.models.PlayerEnum;
import com.gamemechanics.tictactoe.application.domain.models.Position;
import com.gamemechanics.tictactoe.application.ports.in.ManageGameUseCase;
import com.gamemechanics.tictactoe.application.ports.in.PlayGameUseCase;
import com.gamemechanics.tictactoe.application.ports.in.command.PlayRoundCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@Service
@RequiredArgsConstructor
public class GameEngine {

    private final static int GAME_BOARD_SIZE = 3;

    private final ManageGameUseCase manageGameUseCase;
    private final PlayGameUseCase playGameUseCase;
    private final GameBoardPrinter boardPrinter;
    private final AIPlay aiPlay;

    private final Scanner scanner = new Scanner(System.in);

    public void setupGame() {
        System.out.println("Choose first player (X):");
        System.out.println("\t1. Human");
        System.out.println("\t2. AI");

        var xPlayer = PlayerPersonaEnum.AI;
        if (scanner.nextInt() == 1) {
            xPlayer = PlayerPersonaEnum.HUMAN;
        }

        System.out.println("Choose second player (O):");
        System.out.println("\t1. Human");
        System.out.println("\t2. AI");

        var oPlayer = PlayerPersonaEnum.AI;
        if (scanner.nextInt() == 1) {
            oPlayer = PlayerPersonaEnum.HUMAN;
        }

        var game = manageGameUseCase.startNewGame(GAME_BOARD_SIZE);
        boardPrinter.printGameBoard(game.getBoard());

        playGame(new GameConfig(xPlayer, oPlayer), game);
    }


    private void playGame(GameConfig config, Game game) {
        var currentPlayer = PlayerEnum.X;

        while (game.getState() == Game.State.PLAYING) {
            var nextPlayPosition = getNextPlayPosition(game, currentPlayer, config);
            game = playGameRound(game, currentPlayer, nextPlayPosition);
            currentPlayer = getNextPlayer(currentPlayer);

            boardPrinter.printGameBoard(game.getBoard());
        }

        handleFinishedGame(game);
    }

    private Position getNextPlayPosition(Game game, PlayerEnum currentPlayer, GameConfig config) {
        if (isCurrentPlayerIsHuman(currentPlayer, config)) {
            System.out.printf("Make your move player %s:", currentPlayer.name());
            return parsePlay(scanner.nextLine());
        } else {
            return aiPlay.generateAIMove(game, currentPlayer);
        }
    }

    private Position parsePlay(String coords) {
        List<Integer> playerMove = Arrays.stream(coords.trim().split(","))
                .mapToInt(Integer::parseInt)
                .boxed().toList();

        if (playerMove.size() != 2) {
            throw new IllegalArgumentException("Illegal player move");
        }

        return new Position(playerMove.get(0), playerMove.get(1));
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

    private Game playGameRound(Game game, PlayerEnum currentPlayer, Position nextPlayPosition) {
        var playCommand = new PlayRoundCommand(game.getId(), currentPlayer.name(), nextPlayPosition.row(), nextPlayPosition.column());
        return playGameUseCase.playRound(playCommand);
    }

    private void handleFinishedGame(Game game) {
        if (game.getState() == Game.State.WIN) {
            System.out.printf("PLAYER '%s' WON!\n", game.getLastPlay().player().name());
        } else {
            System.out.println("GAME IS A DRAW!");
        }

        System.out.println("Start new game?");
        if (scanner.nextBoolean()) {
            setupGame();
        }
    }
}
