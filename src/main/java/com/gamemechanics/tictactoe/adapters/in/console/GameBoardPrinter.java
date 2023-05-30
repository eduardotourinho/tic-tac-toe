package com.gamemechanics.tictactoe.adapters.in.console;

import com.gamemechanics.tictactoe.application.domain.models.Board;
import com.gamemechanics.tictactoe.application.domain.models.PlayerEnum;
import com.gamemechanics.tictactoe.application.domain.models.Position;
import org.springframework.stereotype.Component;

@Component
public class GameBoardPrinter {

    public void printGameBoard(Board board) {
        System.out.println();
        System.out.println("Game state:");
        System.out.println();

        for (int row = 0; row < board.getRows(); row++) {
            System.out.println();

            for (int col = 0; col < board.getColumns(); col++) {
                var player = board.getPlayerAt(new Position(row, col));

                if (player == PlayerEnum.X) {
                    System.out.print(" X ");
                } else if (player == PlayerEnum.O) {
                    System.out.print(" O ");
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
