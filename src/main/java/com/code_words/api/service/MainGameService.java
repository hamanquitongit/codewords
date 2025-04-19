package com.code_words.api.service;

import com.code_words.api.domain.beans.GameState;
import com.code_words.api.domain.beans.GuessResult;
import com.code_words.api.domain.beans.Player;
import com.code_words.api.domain.constants.Difficulty;
import com.code_words.api.domain.constants.GameConstants;
import com.code_words.api.domain.constants.WordBank;
import com.code_words.api.domain.exceptions.InvalidDifficultyException;
import com.code_words.api.domain.exceptions.InvalidGuessException;
import com.code_words.api.domain.exceptions.PlayersNotFoundException;
import com.code_words.api.domain.exceptions.RepeatGuessException;
import com.code_words.api.utils.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MainGameService {

    @Autowired
    ValidationService validationService;

    @Autowired
    InMemoryService memService;

    public GuessResult processGuess(String guess, String word, String display) throws InvalidGuessException, RepeatGuessException {
        log.info("Processing...");
        validationService.validateGuess(guess);
        List<Character> charList = wordToList(word);
        char guessChar = guess.toUpperCase().charAt(0);
        List<Integer> correctList = getCorrectIndices(charList, guessChar);

        if(correctList.isEmpty()) return new GuessResult(false, display);

        List<Character> displayList = new ArrayList<>(wordToList(display));
        String updatedDisplay = updateDisplay(displayList, correctList, guessChar);
        return new GuessResult(true, updatedDisplay);
    }

    public boolean checkWin(String display) {
        List<Character> dispList = wordToList(display);
        int count = 0;
        int correctAns = dispList.size();
        for(char c : dispList) {
            if(c != '_') count++;
        }
        return count == correctAns;
    }

    public boolean checkLose(int attempts) {
        return attempts == 0;
    }

    public GameState generateInitialGameState(String difficulty) throws InvalidDifficultyException {
        String randomWord = getRandomWord(difficulty);
        GameState newGame = GameState.builder()
                .gameId(generateValidGameId())
                .word(randomWord)
                .display(Utility.maskWord(randomWord))
                .attempts(getAttempts(difficulty))
                .level(Difficulty.verifyDifficulty(difficulty))
                .build();

        log.info("CREATING NEW GAME STATE >> GameID: {} || RandomWord: {} || Difficulty: {}", newGame.getGameId(), newGame.getWord(), difficulty);
        return newGame;

    }

    public String buildLeaderboardStr(List<Player> playerlist) {
        if(playerlist.isEmpty()) throw new PlayersNotFoundException("ERROR : No Players Registered Yet");
        String title = "TOP SCORERS {PLAYER : SCORE} >>> ";
        StringBuilder sb = new StringBuilder();
        sb.append(title);
        playerlist.forEach(player -> {
            sb.append(player.getUsername()).append(" : ").append(player.getScore());
            sb.append(" || ");
        });
        return sb.toString();
    }


    //** PRIVATE METHODS **//

    private String getRandomWord(String difficulty) throws InvalidDifficultyException {
       Difficulty level = Difficulty.verifyDifficulty(difficulty);
        return switch (level) {
            case EASY -> WordBank.EASY.getRandomWord();
            case MEDIUM -> WordBank.MEDIUM.getRandomWord();
            case HARD -> WordBank.HARD.getRandomWord();
        };
    }

    private int getAttempts(String difficulty) throws InvalidDifficultyException {
        Difficulty level = Difficulty.verifyDifficulty(difficulty);
        return level.getAttempts();
    }

    private String generateValidGameId() {
        String gameId = GameConstants.GAME + Utility.generateIdString(memService.getNumActiveGames());
        boolean hasDuplicate = true;
        int counter = 0;
        while(hasDuplicate) {
            if(memService.gameIdHasDuplicate(gameId)) {
                counter++;
                gameId = GameConstants.GAME + Utility.generateIdString(memService.getNumActiveGames() + counter);
            } else {
                hasDuplicate = false;
            }
        }
        return gameId;
    }

    private List<Character> wordToList(String word) {
        String wordRemovedWs = word.replaceAll("\\s", "");
        return wordRemovedWs.chars().mapToObj(c -> (char) c)
                .toList();
    }

    private List<Integer> getCorrectIndices(List<Character> charList, char guess) {
        List<Integer> correctList = new ArrayList<>();
        int idx = 0;
        for(char c: charList) {
            if(c == guess) {
                correctList.add(idx);
            }
            idx++;
        }
        return correctList;
    }

    private String updateDisplay(List<Character> displayList, List<Integer> correctList, char guess) throws RepeatGuessException {
        for(Integer idx : correctList) {
            if(displayList.get(idx) != '_') throw new RepeatGuessException("Repeated Guess");
            displayList.set(idx, guess);
        }

        String updatedDisplay = Utility.buildStringFromCharList(displayList);
        log.info("Updated Display: {}", updatedDisplay);
        return  updatedDisplay;
    }
}
