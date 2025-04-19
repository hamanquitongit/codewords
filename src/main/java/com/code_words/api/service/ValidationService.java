package com.code_words.api.service;

import com.code_words.api.domain.exceptions.InvalidGuessException;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {

    public void validateGuess(String guess) throws InvalidGuessException {
        if(guess.length() != 1) throw new InvalidGuessException("Error: Guess should only contain 1 character");
        if(guess.matches(".*[^a-zA-Z].*")) throw new InvalidGuessException("Error: Guess should only contain letters");
    }
}
