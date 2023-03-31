package com.adsquare.tictactoe.adapters.in.rest.controller;

import com.adsquare.tictactoe.adapters.in.rest.models.ErrorResponse;
import com.adsquare.tictactoe.domain.exceptions.BoardGridNotEmptyException;
import com.adsquare.tictactoe.domain.exceptions.GameNotAvailableException;
import com.adsquare.tictactoe.domain.exceptions.PlayerAlreadyPlayedException;
import jakarta.annotation.Nonnull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;


import java.util.ArrayList;

@RestControllerAdvice
public class GameExceptionHandlingAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                      @Nonnull NativeWebRequest request) {
        final var problems = new ArrayList<String>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error -> problems.add(
                        String.format("%s: %s", error.getField(), error.getDefaultMessage())));

        var errorResponse = ErrorResponse.builder()
                .message("Some fields are not valid")
                .details(problems)
                .status(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler({PlayerAlreadyPlayedException.class, BoardGridNotEmptyException.class})
    public ResponseEntity<ErrorResponse> handlePlayerAlreadyPlayedException(RuntimeException ex,
                                                                            @Nonnull NativeWebRequest request) {

        var errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(GameNotAvailableException.class)
    public ResponseEntity<ErrorResponse> handleGameNotAvailableException(GameNotAvailableException ex,
                                                                         @Nonnull NativeWebRequest request) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponse> handleThrowableException(Throwable ex,
                                                                  @Nonnull NativeWebRequest request) {
        var errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();

        return ResponseEntity.internalServerError().body(errorResponse);
    }
}
