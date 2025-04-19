package com.code_words.api.domain.constants;

import com.code_words.api.domain.exceptions.InvalidDifficultyException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Difficulty {

    EASY("easy", 1, 20),
    MEDIUM("medium", 2, 15),
    HARD("hard", 3, 10);

    private final String label;
    private final int scoreVal;
    private final int attempts;

    Difficulty(String label, int scoreVal, int attempts) {
        this.label = label;
        this.scoreVal = scoreVal;
        this.attempts = attempts;
    }

    public static Difficulty verifyDifficulty(String difficulty) throws InvalidDifficultyException {
        return Arrays.stream(Difficulty.values())
                .filter(d -> d.label.equalsIgnoreCase(difficulty))
                .findFirst()
                .orElseThrow(() -> new InvalidDifficultyException("Invalid Difficulty Input: " + difficulty));
    }

}
