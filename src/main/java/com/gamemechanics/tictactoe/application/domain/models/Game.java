package com.gamemechanics.tictactoe.application.domain.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
public class Game {

    UUID id;
    State state;
    Board board;
    Play lastPlay;

    public enum State {
        PLAYING, FINISHED, WIN
    }
}
