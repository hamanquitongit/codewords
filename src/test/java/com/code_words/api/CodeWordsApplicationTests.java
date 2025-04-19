package com.code_words.api;

import com.code_words.api.domain.constants.GameConstants;
import com.code_words.api.domain.requests.GameStartRequest;
import com.code_words.api.domain.requests.GuessRequest;
import com.code_words.api.service.InMemoryService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
@AutoConfigureMockMvc
class CodeWordsApplicationTests {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	InMemoryService memService;

	private final String TEST_GAME_ID_1 = "game001";
	private static final ObjectMapper mapper = new ObjectMapper();


	@BeforeEach
	void clearInMemoryStorage() {
		memService.getPlayerStore().clear();
		memService.getActiveGameStore().clear();
	}

	@Test
	void contextLoads() {
	}


	@Test
	void testNewGame() throws Exception {
		mockMvc.perform(post("/game"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.gameId").value(TEST_GAME_ID_1))
				.andExpect(jsonPath("$.maskedWord").value("_ _ _"))
				.andExpect(jsonPath("$.remainingAttempts").value(20));
	}

	@Test
	void testNewGameWithUser() throws Exception {
		mockMvc.perform(post("/game")
					.contentType(MediaType.APPLICATION_JSON)
					.content(buildNewGameRequestStr("test", "easy")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.gameId").value(TEST_GAME_ID_1))
				.andExpect(jsonPath("$.maskedWord").value("_ _ _"))
				.andExpect(jsonPath("$.remainingAttempts").value(20));
	}

	@Test
	void testNewGameMediumDifficulty() throws Exception {
		mockMvc.perform(post("/game")
						.contentType(MediaType.APPLICATION_JSON)
						.content(buildNewGameRequestStr("test", "medium")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.gameId").value(TEST_GAME_ID_1))
				.andExpect(jsonPath("$.maskedWord").value("_ _ _ _"))
				.andExpect(jsonPath("$.remainingAttempts").value(15));
	}

	@Test
	void testNewGameHardDifficulty() throws Exception {
		mockMvc.perform(post("/game")
						.contentType(MediaType.APPLICATION_JSON)
						.content(buildNewGameRequestStr("test", "hard")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.gameId").value(TEST_GAME_ID_1))
				.andExpect(jsonPath("$.maskedWord").value("_ _ _ _ _"))
				.andExpect(jsonPath("$.remainingAttempts").value(10));
	}


	@Test
	void testForfeitGame() throws Exception {
		mockMvc.perform(post("/game")
				.contentType(MediaType.APPLICATION_JSON)
				.content(buildNewGameRequestStr("test", "hard")));

		mockMvc.perform(post("/game/" + TEST_GAME_ID_1 + "/forfeit"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.gameId").value(TEST_GAME_ID_1))
				.andExpect(jsonPath("$.ffMsg").value("Game Forfeited"));
	}

	@Test
	void testCorrectGuess() throws Exception {
		mockMvc.perform(post("/game")
				.contentType(MediaType.APPLICATION_JSON)
				.content(buildNewGameRequestStr("test", "easy")));

		String word = memService.getGameState(TEST_GAME_ID_1).getWord();

		mockMvc.perform(post("/game/"+ TEST_GAME_ID_1 + "/guess")
				.contentType(MediaType.APPLICATION_JSON)
				.content(buildGuessRequestStr(word)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.gameId").value(TEST_GAME_ID_1))
				.andExpect(jsonPath("$.status").value(GameConstants.STATUS_IN_PROGRESS_CORRECT));
	}

	@Test
	void testWrongGuess() throws Exception {
		mockMvc.perform(post("/game")
				.contentType(MediaType.APPLICATION_JSON)
				.content(buildNewGameRequestStr("test", "easy")));

		mockMvc.perform(post("/game/"+ TEST_GAME_ID_1 + "/guess")
						.contentType(MediaType.APPLICATION_JSON)
						.content(buildGuessRequestStr("F")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.gameId").value(TEST_GAME_ID_1))
				.andExpect(jsonPath("$.status").value(GameConstants.STATUS_IN_PROGRESS_WRONG));
	}


	//** Private methods **//
	private GameStartRequest buildNewGameRequest() {
		GameStartRequest mockRequest = new GameStartRequest();
		mockRequest.setUser("test");
		mockRequest.setDifficulty("easy");
		return mockRequest;
	}

	private String buildNewGameRequestStr(String user, String difficulty) throws JsonProcessingException {
		GameStartRequest request = new GameStartRequest();
		request.setUser(user);
		request.setDifficulty(difficulty);
		return mapper.writeValueAsString(request);
	}

	private String buildGuessRequestStr(String word) throws JsonProcessingException {
		GuessRequest request = new GuessRequest();
		request.setGuess(String.valueOf(word.charAt(0)));
		return mapper.writeValueAsString(request);
	}

}
