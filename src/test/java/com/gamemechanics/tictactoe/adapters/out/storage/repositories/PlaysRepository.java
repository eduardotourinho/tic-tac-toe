package com.gamemechanics.tictactoe.adapters.out.storage.repositories;

import com.gamemechanics.tictactoe.adapters.out.storage.entities.PlayEntity;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Profile("integration-test")
public interface PlaysRepository extends CrudRepository<PlayEntity, UUID> {

    @Query(value = "SELECT p FROM PlayEntity p WHERE p.game.id = ?1 AND p.row = ?2 AND p.column = ?3")
    PlayEntity findByGameRowAndColum(UUID gameId, int row, int column);
}
