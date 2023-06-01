package com.gamemechanics.tictactoe.adapters.in.console;

import com.gamemechanics.tictactoe.application.domain.models.*;
import com.gamemechanics.tictactoe.application.ports.out.GameUIPort;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@Component
public class GameUI implements GameUIPort {

    private final Scanner scanner = new Scanner(System.in);

    public GameConfig setupGame() {
        var scanner = new Scanner(System.in);

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

        return new GameConfig(xPlayer, oPlayer);
    }

    @Override
    public void updateBoard(Game game) {
        System.out.println();
        System.out.printf("Game %s:", game.getId());
        System.out.println();

        printGameBoard(game.getBoard());
    }

    @Override
    public Position fetchMove(PlayerEnum currentPlayer) {
        System.out.printf("Make your move player %s: ", currentPlayer.name());
        return parsePlay(scanner.next());
    }

    public void handleFinishedGame(Game game) {
        if (game.getState() == Game.State.WIN) {
            System.out.printf("PLAYER '%s' WON!", game.getLastPlay().player().name());
            System.out.println();
        } else {
            System.out.println("GAME IS A DRAW!");
        }
    }

    public boolean playAgain() {
        var scanner = new Scanner(System.in);
        System.out.println("Start new game? (y|n)");

        return scanner.next().equals("y") || scanner.next().isEmpty();
    }

    private Position parsePlay(String coords) {
        if (coords.isBlank() || coords.isEmpty()) {
            throw new IllegalArgumentException("Illegal player move");
        }

        List<Integer> playerMove = Arrays.stream(coords.trim().split(","))
                .mapToInt(Integer::parseInt)
                .boxed().toList();

        if (playerMove.size() != 2) {
            throw new IllegalArgumentException("Illegal player move");
        }

        return new Position(playerMove.get(0), playerMove.get(1));
    }

    private void printGameBoard(Board board) {
        for (int row = 0; row < board.getRows(); row++) {
            System.out.println();

            for (int col = 0; col < board.getColumns(); col++) {
                var player = board.getPlayerAt(new Position(row, col));

                if (player == PlayerEnum.X || player == PlayerEnum.O) {
                    System.out.printf(" %s ", player.name());
                } else {
                    System.out.print("   ");
                }

                if (col < board.getColumns() - 1) {
                    System.out.print("|");
                }
            }

            System.out.println();
            if (row < board.getRows() - 1) {
                for (int col = 0; col < board.getColumns(); col++) {
                    System.out.print("____");
                }
            }
        }
        System.out.println();
    }
}
