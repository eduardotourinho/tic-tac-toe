package com.gamemechanics.tictactoe.adapters.out.storage.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="games")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameEntity {

    @Id
    @NotNull
    private UUID id;

    @NotNull
    private int boardSize;

    @NotNull
    private String state;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlayEntity> plays;

    @CreatedDate
    private OffsetDateTime createdAt;
    @LastModifiedDate
    private OffsetDateTime updatedAt;
}
