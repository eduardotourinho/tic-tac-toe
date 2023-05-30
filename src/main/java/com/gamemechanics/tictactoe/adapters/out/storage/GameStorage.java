package com.gamemechanics.tictactoe.adapters.out.storage;

import com.gamemechanics.tictactoe.adapters.out.storage.entities.GameEntity;
import com.gamemechanics.tictactoe.adapters.out.storage.exceptions.GameNotFoundException;
import com.gamemechanics.tictactoe.adapters.out.storage.mappers.GameMapper;
import com.gamemechanics.tictactoe.adapters.out.storage.repositories.GameRepository;
import com.gamemechanics.tictactoe.application.domain.models.Game;
import com.gamemechanics.tictactoe.application.ports.out.FindGameUseCase;
import com.gamemechanics.tictactoe.application.ports.out.SaveGameUseCase;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameStorage implements SaveGameUseCase, FindGameUseCase {

    private final GameMapper gameMapper;
    private final GameRepository gameRepository;

    @Override
    @Transactional
    public Game startNewGame(int boardSize) {
        var gameEntity = GameEntity.builder()
                .id(UUID.randomUUID())
                .state(Game.State.PLAYING.name())
                .boardSize(boardSize)
                .createdAt(OffsetDateTime.now())
                .build();

        return gameMapper.fromStorage(gameRepository.save(gameEntity));
    }

    @Override
    @Transactional
    public Game loadGame(@NonNull UUID gameId) {
        return gameRepository.findById(gameId)
                .map(gameMapper::fromStorage)
                .orElse(null);
    }

    @Override
    @Transactional
    public Game saveGameState(@NonNull Game game) {
        var gameEntity = gameRepository.findById(game.getId())
                .orElse(null);

        if (gameEntity == null) {
            throw new GameNotFoundException(game.getId());
        }

        var playEntity = gameMapper.playEntityFromDomain(game);
        playEntity.setGame(gameEntity);

        gameEntity.getPlays().add(playEntity);
        gameEntity.setState(game.getState().toString());
        gameEntity.setUpdatedAt(OffsetDateTime.now());

        return gameMapper.fromStorage(gameRepository.save(gameEntity));
    }
}
