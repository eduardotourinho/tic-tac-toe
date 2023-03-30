package com.adsquare.tictactoe.domain.models;

public enum PlayerEnum {

    EMPTY(""),
    X("x"),
    O("o");

    private final String value;

    PlayerEnum(String value) {
        this.value = value;
    }
}
