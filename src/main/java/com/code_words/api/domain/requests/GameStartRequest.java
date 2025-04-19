package com.code_words.api.domain.requests;

import lombok.Data;

@Data
public class GameStartRequest {
    private String user;
    private String difficulty;
}
