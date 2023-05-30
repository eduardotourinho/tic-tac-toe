package com.gamemechanics.tictactoe.adapters.out.storage.repositories;

import com.gamemechanics.tictactoe.adapters.out.storage.entities.GameEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GameRepository extends CrudRepository<GameEntity, UUID> {
}
