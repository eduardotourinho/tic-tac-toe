package com.gamemechanics.tictactoe.adapters.out.storage.mappers;

import com.gamemechanics.tictactoe.adapters.out.storage.entities.GameEntity;
import com.gamemechanics.tictactoe.adapters.out.storage.entities.PlayEntity;
import com.gamemechanics.tictactoe.application.domain.models.*;
import com.gamemechanics.tictactoe.application.domain.models.Game.State;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;


@Component
public class GameMapper {

    public PlayEntity playEntityFromDomain(@NonNull Game game) {
        var lastPlay = game.getLastPlay();

        return PlayEntity.builder()
                .id(UUID.randomUUID())
                .row(lastPlay.position().row())
                .column(lastPlay.position().column())
                .player(lastPlay.player().toString())
                .createdAt(OffsetDateTime.now())
                .build();
    }

    public Game fromStorage(@NonNull GameEntity gameEntity) {
        var emptyBoard = new Board(gameEntity.getBoardSize(), gameEntity.getBoardSize());
        var gameBuilder = Game.builder()
                .id(gameEntity.getId())
                .state(State.valueOf(gameEntity.getState()))
                .board(emptyBoard);

        if (gameEntity.getPlays() != null) {
            var board = generateBoard(emptyBoard, gameEntity.getPlays());
            var lastPlay = getLastPlay(gameEntity.getPlays());

            gameBuilder.board(board)
                    .lastPlay(lastPlay);
        }

        return gameBuilder.build();
    }

    private Board generateBoard(@NonNull final Board board, @NonNull List<PlayEntity> plays) {
        plays.forEach(playEntity -> {
            var player = PlayerEnum.valueOf(playEntity.getPlayer());
            var position = new Position(playEntity.getRow(), playEntity.getColumn());

            board.add(player, position);
        });

        return board;
    }

    private Play getLastPlay(@NonNull List<PlayEntity> plays) {
        return plays.stream()
            .max(Comparator.comparing(PlayEntity::getCreatedAt))
            .map(playEntity -> {
                var lastPlayer = PlayerEnum.valueOf(playEntity.getPlayer());
                var lastPosition = new Position(playEntity.getRow(), playEntity.getColumn());

                return new Play(lastPlayer, lastPosition);
            })
            .orElse(null);
    }
}
