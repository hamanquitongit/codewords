package com.code_words.api.domain.beans;

import com.code_words.api.domain.constants.Difficulty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameState {
    String user;
    String gameId;
    String word;
    String display;
    Difficulty level;
    int attempts;
}
