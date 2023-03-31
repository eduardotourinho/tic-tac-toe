package com.adsquare.tictactoe.adapters.in.rest.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ErrorResponse {

    String message;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<String> details;
    String status;
    int statusCode;
}
