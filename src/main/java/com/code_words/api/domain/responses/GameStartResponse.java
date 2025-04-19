package com.code_words.api.domain.responses;

public record GameStartResponse
        (String gameId, String maskedWord, int remainingAttempts) {
}
