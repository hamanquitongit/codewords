package com.code_words.api.service;

import com.code_words.api.domain.beans.GameState;
import com.code_words.api.domain.beans.Player;
import com.code_words.api.utils.Utility;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Slf4j
@Service
public class InMemoryService {

    private final Map<Integer, GameState> activeGameStore = new ConcurrentHashMap<>();

    private final Map<String, Player> playerStore = new ConcurrentHashMap<>();

    public void savePlayer(Player player) {
        log.info("Saving new Player to memory: {}", player.getUsername());
        player.setScore(0L);
        playerStore.put(player.getUsername(), player);
    }

    public Player getPlayer(String username) {
        log.info("Fetching player with username: {}", username);
        return playerStore.get(username);
    }

    public List<Player> getTopPlayers() {
        return playerStore.values()
                .stream()
                .sorted(Comparator.comparingLong(Player::getScore).reversed())
                .limit(3)
                .toList();
    }

    public void updatePlayerScore(String username, long score) {
        if(playerStore.get(username) == null) {
            savePlayer(Player.builder()
                    .username(username)
                    .score(0L)
                    .build());
        }
        Player player = playerStore.get(username);
        player.setScore(player.getScore() + score);
        playerStore.put(username, player);
    }

    public void cacheGame(GameState game) {
        log.info("Caching Game: {}", game.getGameId());
        activeGameStore.put(Utility.extractGameIdNumeric(game.getGameId()), game);
        log.info("Total Active Games: {}", activeGameStore.size());
    }

    public void removeGame(String gameId) {
        log.info("Removing Game: {}", gameId);
        activeGameStore.remove(Utility.extractGameIdNumeric(gameId));
    }

    public Integer getNumActiveGames() {
        return activeGameStore.size();
    }

    public boolean gameIdHasDuplicate(String gameId) {
        return activeGameStore.containsKey(Utility.extractGameIdNumeric(gameId));
    }

    public GameState getGameState(String gameId) {
        log.info("Fetching Game State: {}", gameId);
        return activeGameStore.get(Utility.extractGameIdNumeric(gameId));
    }

    public void updateGameState(GameState game) {
        log.info("Updating Game: {}", game.getGameId());
        activeGameStore.put(Utility.extractGameIdNumeric(game.getGameId()), game);
    }


}
