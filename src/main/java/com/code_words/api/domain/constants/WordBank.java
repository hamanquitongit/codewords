package com.code_words.api.domain.constants;

import lombok.Getter;

import java.util.List;
import java.util.Random;

public enum WordBank {
    /**
     * Using enum class for simplicity.
     * Can be improved using external configurable files as source for word bank.
     *
     */

    EASY(List.of("CAT", "DOG", "TAX")),
    MEDIUM(List.of("BOOK", "MILK", "LAMP")),
    HARD(List.of("CHAIR", "APPLE", "BEACH"));

    @Getter
    private final List<String> wordSet;

    private final Random random = new Random();

    WordBank(List<String> wordSet) {
        this.wordSet = wordSet;
    }

    public String getRandomWord() {
        return wordSet.get(random.nextInt(wordSet.size()));
    }
}
