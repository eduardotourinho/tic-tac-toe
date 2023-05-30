package com.gamemechanics.tictactoe.adapters.in.rest.models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.validation.annotation.Validated;

@Value
@Validated
@AllArgsConstructor
public class PlayRequest {

    @Pattern(regexp = "[X|O]", message = "Player must be 'X' or 'O'")
    String player;

    @Min(value = 0)
    @Max(value = 2)
    int row;

    @Min(value = 0)
    @Max(value = 2)
    int column;

}
