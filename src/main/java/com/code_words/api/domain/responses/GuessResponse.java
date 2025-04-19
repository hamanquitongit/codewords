package com.code_words.api.domain.responses;

public record GuessResponse(String gameId, String maskedWord, int remainingAttempts, String status) {
}
