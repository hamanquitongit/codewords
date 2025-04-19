package com.code_words.api.domain.constants;

import lombok.Getter;

@Getter
public enum APIStatusCodes {

    SUCCESS(200, "Success"),
    BAD_REQUEST(400, "Missing/Invalid Request Params"),
    GAME_NOT_FOUND(404, "Game Not Found"),
    PLAYERS_NOT_FOUND(405, "Players not found"),
    INTERNAL_ERROR(500, "Internal Error Encountered"),
    INVALID_DIFFICULTY(501, "Invalid Difficulty Value");

    private final int statusCode;
    private final String desc;

    APIStatusCodes(int statusCode, String desc) {
        this.statusCode = statusCode;
        this.desc = desc;
    }
}
