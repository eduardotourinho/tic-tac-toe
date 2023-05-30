package com.gamemechanics.tictactoe.application.domain.models;

import lombok.Value;

import java.util.HashMap;
import java.util.Map;

@Value
public class Board {

    int rows;
    int columns;

    Map<Position, PlayerEnum> grid = new HashMap<>();

    public Board(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;

        initBoard();
    }

    public void add(PlayerEnum player, Position position) {
        grid.put(position, player);
    }

    public void add(Play play) {
        grid.put(play.position(), play.player());
    }

    public PlayerEnum getPlayerAt(Position position) {
        return grid.get(position);
    }

    private void initBoard() {
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                add(PlayerEnum.EMPTY, new Position(row, column));
            }
        }
    }
}
