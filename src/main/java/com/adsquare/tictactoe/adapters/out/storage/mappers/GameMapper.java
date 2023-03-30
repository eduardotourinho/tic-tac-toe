package com.adsquare.tictactoe.adapters.out.storage.mappers;

import com.adsquare.tictactoe.adapters.out.storage.entities.GameEntity;
import com.adsquare.tictactoe.adapters.out.storage.entities.PlayEntity;
import com.adsquare.tictactoe.domain.models.*;
import com.adsquare.tictactoe.domain.models.Game.State;
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
        var gameBuilder = Game.builder()
                .id(gameEntity.getId())
                .state(State.valueOf(gameEntity.getState()));

        if (gameEntity.getPlays() != null) {
            var board = generateBoard(gameEntity.getBoardSize(), gameEntity.getPlays());
            var lastPlay = getLastPlay(gameEntity.getPlays());

            gameBuilder.board(board)
                    .lastPlay(lastPlay);
        }

        return gameBuilder.build();
    }

    private Board generateBoard(int boardSize, @NonNull List<PlayEntity> plays) {
        final var board = new Board(boardSize, boardSize);

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