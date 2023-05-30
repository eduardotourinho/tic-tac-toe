package com.gamemechanics.tictactoe.application.ports.in.command;

import java.util.UUID;

public record PlayRoundCommand(UUID gameId, String player, int row, int column) {
}
