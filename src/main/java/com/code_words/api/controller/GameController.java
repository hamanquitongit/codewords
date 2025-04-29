package com.code_words.api.controller;

import com.code_words.api.domain.beans.GameState;
import com.code_words.api.domain.beans.GuessResult;
import com.code_words.api.domain.constants.GameConstants;
import com.code_words.api.domain.exceptions.*;
import com.code_words.api.domain.requests.GameStartRequest;
import com.code_words.api.domain.requests.GuessRequest;
import com.code_words.api.domain.responses.ForfeitResponse;
import com.code_words.api.domain.responses.GameStartResponse;
import com.code_words.api.domain.responses.GuessResponse;
import com.code_words.api.domain.responses.LeaderboardResponse;
import com.code_words.api.service.InMemoryService;
import com.code_words.api.service.MainGameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/game")
public class GameController {

    @Autowired
    InMemoryService memService;

    @Autowired
    MainGameService gameService;

    @PostMapping
    public GameStartResponse gameStart() throws InvalidDifficultyException {
        GameState newGame = gameService.generateInitialGameState("easy");
        memService.cacheGame(newGame);
        return new GameStartResponse(newGame.getGameId(), newGame.getDisplay(), newGame.getAttempts());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public GameStartResponse gameStartWithUser(@RequestBody GameStartRequest request) throws InvalidDifficultyException {
        GameState newGame = gameService.generateInitialGameState(request.getDifficulty());
        newGame.setUser(request.getUser());
        memService.cacheGame(newGame);
        return new GameStartResponse(newGame.getGameId(), newGame.getDisplay(), newGame.getAttempts());
    }

    @PostMapping("/leaderboard")
    public LeaderboardResponse getLeaderboard() throws PlayersNotFoundException {
        return new LeaderboardResponse(gameService.buildLeaderboardStr(memService.getTopPlayers()));
    }

    @PostMapping("/{gameId}/forfeit")
    public ForfeitResponse forfeitGame(@PathVariable String gameId) {
        memService.removeGame(gameId);
        return new ForfeitResponse(gameId, GameConstants.DEFAULT_FF_MSG);
    }

    @PostMapping("/{gameId}/guess")
    public GuessResponse takeAGuess(@RequestBody GuessRequest request, @PathVariable String gameId) throws InvalidGuessException, GameNotFoundException {
        GameState game = memService.getGameState(gameId);
        GuessResult result;
        try {
            result = gameService.processGuess(request.getGuess(), game.getWord(), game.getDisplay());
        } catch(RepeatGuessException e) {
            return new GuessResponse(gameId, game.getDisplay(), game.getAttempts(), GameConstants.STATUS_IN_PROGRESS_REPEAT);
        } catch(NullPointerException e) {
            throw new GameNotFoundException("ERROR: Invalid - Game not found!");
        }
        if(result.hasMatch()) {
            log.info(" Processing: CORRECT GUESS");
            game.setDisplay(result.newDisplay());

            if(gameService.checkWin(game.getDisplay())) {
                memService.updatePlayerScore(game.getUser(), game.getLevel().getScoreVal());
                memService.removeGame(game.getGameId());
                return new GuessResponse(gameId, game.getDisplay(), game.getAttempts(), GameConstants.STATUS_WIN);
            }

            memService.updateGameState(game);
            return new GuessResponse(gameId, game.getDisplay(), game.getAttempts(), GameConstants.STATUS_IN_PROGRESS_CORRECT);
        } else {
            log.info(" Processing: INCORRECT GUESS");
            game.setAttempts(game.getAttempts() - 1);

            if(gameService.checkLose(game.getAttempts())) {
                memService.removeGame(game.getGameId());
                return new GuessResponse(gameId, game.getDisplay(), game.getAttempts(), GameConstants.STATUS_LOSE);
            }

            memService.updateGameState(game);
            return new GuessResponse(game.getGameId(), game.getDisplay(), game.getAttempts(), GameConstants.STATUS_IN_PROGRESS_WRONG);
        }
    }
}
