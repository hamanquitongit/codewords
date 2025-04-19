package com.code_words.api.controller;

import com.code_words.api.domain.constants.APIStatusCodes;
import com.code_words.api.domain.exceptions.GameNotFoundException;
import com.code_words.api.domain.exceptions.InvalidDifficultyException;
import com.code_words.api.domain.exceptions.InvalidGuessException;
import com.code_words.api.domain.exceptions.PlayersNotFoundException;
import com.code_words.api.domain.responses.ExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GameControllerAdvice {

    @ExceptionHandler(InvalidDifficultyException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidDifficultyException(InvalidDifficultyException e) {
        ExceptionResponse exResponse = new ExceptionResponse(e.getClass().getSimpleName(), e.getMessage());
        return ResponseEntity.status(APIStatusCodes.INVALID_DIFFICULTY.getStatusCode())
                .body(exResponse);
    }

    @ExceptionHandler(InvalidGuessException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidGuessException(InvalidGuessException e) {
        ExceptionResponse exResponse = new ExceptionResponse(e.getClass().getSimpleName(), e.getMessage());
        return ResponseEntity.status(APIStatusCodes.BAD_REQUEST.getStatusCode())
                .body(exResponse);
    }

    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleGameNotFoundException(GameNotFoundException e) {
        ExceptionResponse exResponse = new ExceptionResponse(e.getClass().getSimpleName(), e.getMessage());
        return ResponseEntity.status(APIStatusCodes.GAME_NOT_FOUND.getStatusCode())
                .body(exResponse);
    }

    @ExceptionHandler(PlayersNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handlePlayersNotFoundException(PlayersNotFoundException e) {
        ExceptionResponse exResponse = new ExceptionResponse(e.getClass().getSimpleName(), e.getMessage());
        return ResponseEntity.status(APIStatusCodes.PLAYERS_NOT_FOUND.getStatusCode())
                .body(exResponse);
    }
}
