package com.adsquare.tictactoe.adapters.in.rest.models;

import com.adsquare.tictactoe.domain.models.Position;

public record PlayRequest(String player, Position position) {
}
