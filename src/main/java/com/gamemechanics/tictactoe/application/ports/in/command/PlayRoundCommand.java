package com.gamemechanics.tictactoe.application.ports.in.command;

import com.gamemechanics.tictactoe.application.domain.models.Position;

import java.util.UUID;

public record PlayRoundCommand(UUID gameId, String player, Position playPosition) {
}
