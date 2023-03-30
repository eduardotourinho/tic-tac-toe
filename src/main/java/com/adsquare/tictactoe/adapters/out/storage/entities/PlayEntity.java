package com.adsquare.tictactoe.adapters.out.storage.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name="plays")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private String player;

    @ManyToOne(fetch = FetchType.LAZY)
    private GameEntity game;

    @Column(name = "play_row")
    private int row;
    @Column(name = "play_column")
    private int column;

    @CreatedDate
    private OffsetDateTime createdAt;
}
