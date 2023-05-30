package com.gamemechanics.tictactoe.adapters.out.storage.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="plays",
        uniqueConstraints = @UniqueConstraint(name = "key_plays_game_row_column",
                columnNames = {"game_id", "play_row", "play_column"}))
public class PlayEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @NotBlank
    @NotNull
    private String player;

    @ManyToOne(fetch = FetchType.LAZY)
    private GameEntity game;

    @NotNull
    @Column(name = "play_row")
    private int row;

    @NotNull
    @Column(name = "play_column")
    private int column;

    @CreatedDate
    private OffsetDateTime createdAt;
}
