package com.code_words.api.domain.exceptions;

public class PlayersNotFoundException extends RuntimeException {
    public PlayersNotFoundException(String message) {
        super(message);
    }
}
