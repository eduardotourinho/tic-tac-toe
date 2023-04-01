package com.adsquare.tictactoe.adapters.in.rest.mappers;

import com.adsquare.tictactoe.adapters.in.rest.models.GameResponse;
import com.adsquare.tictactoe.application.domain.models.Board;
import com.adsquare.tictactoe.application.domain.models.Game;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ResponseMapper {

    public GameResponse gameResponseMapper(Game game) {
        var response = GameResponse.builder()
                .id(game.getId().toString())
                .state(game.getState().name())
                .board(mapBoard(game));

        if (game.getState() == Game.State.WIN) {
            response.winner(game.getLastPlay().player().name());
        }

        return response.build();
    }

    private GameResponse.Board mapBoard(Game game) {
        var grid = mapBoardGrid(game.getBoard());
        return GameResponse.Board.builder()
                .boardSize(game.getBoard().getRows())
                .grid(grid)
                .build();
    }

    private List<GameResponse.Play> mapBoardGrid(Board board) {
        return board.getGrid().entrySet().stream()
                .map(gridEntry -> GameResponse.Play.builder()
                        .row(gridEntry.getKey().row())
                        .column(gridEntry.getKey().column())
                        .player(gridEntry.getValue().name())
                        .build())
                .toList();
    }
}
