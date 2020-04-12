package net.seancoyne.fiveinarowserver.service;

import net.seancoyne.fiveinarowserver.model.request.CreateGameRequest;
import net.seancoyne.fiveinarowserver.model.request.GameStateRequest;
import net.seancoyne.fiveinarowserver.model.request.MoveRequest;
import net.seancoyne.fiveinarowserver.model.request.RegisterRequest;
import net.seancoyne.fiveinarowserver.model.response.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    GameService testInstance;

    @BeforeEach
    public void setup() {
        testInstance = new GameService();
    }

    @Test
    void test_createGame_success() {
        // Given
        String username = "someUsername";
        String colour = "x";

        CreateGameRequest createGameRequest = new CreateGameRequest();
        createGameRequest.setUsername(username);
        createGameRequest.setSelectedColour(colour);

        // When
        CreateGameResponse response = testInstance.createGame(createGameRequest);

        // Then
        assertNotNull(response);

        assertEquals(ResponseState.SUCCESS, response.getResponseState());
        assertEquals("New game created! Send the game id to your friend", response.getMessage());
        assertNotNull(response.getGameId());
    }

    @Test
    void test_registerPlayer_success() {
        // Given
        String username = "someUsername";
        String newUsername = "newUsername";

        CreateGameRequest createGameRequest = new CreateGameRequest();
        createGameRequest.setUsername(username);
        createGameRequest.setSelectedColour("X");
        CreateGameResponse createGameResponse = testInstance.createGame(createGameRequest);

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUserName(newUsername);
        registerRequest.setColour("O");
        registerRequest.setGameId(createGameResponse.getGameId());

        // When
        RegisterResponse registerResponse = testInstance.registerPlayer(registerRequest);

        // Then
        assertNotNull(registerResponse);

        assertEquals(ResponseState.SUCCESS, registerResponse.getResponseState());
        assertEquals("User registered for game", registerResponse.getMessage());
    }

    @Test
    void test_registerPlayer_failed_unknown_game() {
        // Given
        String username = "someUsername";

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUserName(username);
        registerRequest.setColour("O");
        registerRequest.setGameId(0);

        // When
        RegisterResponse registerResponse = testInstance.registerPlayer(registerRequest);

        // Then
        assertNotNull(registerResponse);

        assertEquals(ResponseState.FAILED, registerResponse.getResponseState());
        assertEquals("No game with this ID exists", registerResponse.getMessage());
    }

    @Test
    void test_makeMove_success() {
        // Given
        String username = "someUsername";
        String newUsername = "newUsername";

        CreateGameRequest createGameRequest = new CreateGameRequest();
        createGameRequest.setUsername(username);
        createGameRequest.setSelectedColour("X");
        CreateGameResponse createGameResponse = testInstance.createGame(createGameRequest);

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUserName(newUsername);
        registerRequest.setColour("O");
        registerRequest.setGameId(createGameResponse.getGameId());
        testInstance.registerPlayer(registerRequest);

        MoveRequest moveRequest = new MoveRequest();
        moveRequest.setColumn(1);
        moveRequest.setGameId(createGameResponse.getGameId());
        moveRequest.setUsername(username);

        // When
        MoveResponse moveResponse = testInstance.makeMove(moveRequest);

        // Then
        assertNotNull(moveResponse);

        assertEquals(ResponseState.SUCCESS, moveResponse.getResponseState());
        assertEquals("Move made", moveResponse.getMessage());
        assertFalse(moveResponse.isTryAgain());
    }

    @Test
    void test_makeMove_failed_unknown_game() {
        // Given
        String username = "someUsername";
        String newUsername = "newUsername";

        MoveRequest moveRequest = new MoveRequest();
        moveRequest.setColumn(1);
        moveRequest.setGameId(0);
        moveRequest.setUsername(username);

        // When
        MoveResponse moveResponse = testInstance.makeMove(moveRequest);

        // Then
        assertNotNull(moveResponse);

        assertEquals(ResponseState.FAILED, moveResponse.getResponseState());
        assertEquals("No game with this ID exists", moveResponse.getMessage());
        assertFalse(moveResponse.isTryAgain());
    }

    @Test
    void test_getGameStateForUser_success() {
        // Given
        String playerOne = "someUsername";
        String playerTwo = "newUsername";

        CreateGameRequest createGameRequest = new CreateGameRequest();
        createGameRequest.setUsername(playerOne);
        createGameRequest.setSelectedColour("X");
        CreateGameResponse createGameResponse = testInstance.createGame(createGameRequest);

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUserName(playerTwo);
        registerRequest.setColour("O");
        registerRequest.setGameId(createGameResponse.getGameId());
        testInstance.registerPlayer(registerRequest);

        MoveRequest moveRequest = new MoveRequest();
        moveRequest.setColumn(1);
        moveRequest.setGameId(createGameResponse.getGameId());
        moveRequest.setUsername(playerOne);
        testInstance.makeMove(moveRequest);

        GameStateRequest playerOneGameStateRequest = GameStateRequest.builder()
                .gameId(createGameResponse.getGameId())
                .username(playerOne)
                .build();

        // When
        GameStateResponse playerOneStateResponse = testInstance.getGameStateForUser(playerOneGameStateRequest);

        // Then
        assertNotNull(playerOneStateResponse);

        assertEquals(ResponseState.SUCCESS, playerOneStateResponse.getResponseState());
        assertEquals(GameState.WAITING_FOR_NEXT_PLAYER_TO_MAKE_MOVE, playerOneStateResponse.getGameState());
        assertEquals("Waiting for other player to make move", playerOneStateResponse.getMessage());
        assertNotNull(playerOneStateResponse.getCurrentBoard());
        assertFalse(playerOneStateResponse.isWinner());
    }

    @Test
    void test_getGameStateForUser_failed_unknown_game() {
        // Given
        String playerOne = "someUsername";

        GameStateRequest playerOneGameStateRequest = GameStateRequest.builder()
                .gameId(0)
                .username(playerOne)
                .build();

        // When
        GameStateResponse playerOneStateResponse = testInstance.getGameStateForUser(playerOneGameStateRequest);

        // Then
        assertNotNull(playerOneStateResponse);

        assertEquals(ResponseState.FAILED, playerOneStateResponse.getResponseState());
        assertEquals("No game with this ID exists", playerOneStateResponse.getMessage());
        assertNull(playerOneStateResponse.getGameState());
        assertNull(playerOneStateResponse.getCurrentBoard());
        assertFalse(playerOneStateResponse.isWinner());
    }
}