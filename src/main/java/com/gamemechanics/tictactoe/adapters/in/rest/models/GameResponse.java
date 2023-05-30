package com.gamemechanics.tictactoe.adapters.in.rest.models;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class GameResponse {

    String id;
    String state;
    Board board;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    String winner;

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
