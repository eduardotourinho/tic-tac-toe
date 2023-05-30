package com.gamemechanics.tictactoe.adapters.in.rest.mappers;

import com.gamemechanics.tictactoe.adapters.in.rest.models.PlayRequest;
import com.gamemechanics.tictactoe.application.ports.in.command.PlayRoundCommand;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RequestMapper {

    public PlayRoundCommand toCommand(UUID gameId, PlayRequest playRequest) {
        return new PlayRoundCommand(gameId, playRequest.getPlayer(), playRequest.getRow(), playRequest.getColumn());
    }
}
