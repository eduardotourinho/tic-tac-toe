package com.adsquare.tictactoe.adapters.out.storage.repositories;

import com.adsquare.tictactoe.adapters.out.storage.entities.PlayEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlaysRepository extends CrudRepository<PlayEntity, UUID> {
}
