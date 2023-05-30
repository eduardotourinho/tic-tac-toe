package com.gamemechanics.tictactoe.adapters.in.rest.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "tic-tac-toe")
public class TicTacToeConfig {

    int boardSize;
}
