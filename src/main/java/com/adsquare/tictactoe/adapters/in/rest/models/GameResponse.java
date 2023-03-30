package com.adsquare.tictactoe.adapters.in.rest.models;


import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class GameResponse {

    String id;
    String state;
    Board board;

    @Value
    @Builder
    public static class Board {
        int boardSize;
        List<Play> grid;
    }

    @Value
    @Builder
    public static class Play {
        int row;
        int column;
        String player;
    }
}
