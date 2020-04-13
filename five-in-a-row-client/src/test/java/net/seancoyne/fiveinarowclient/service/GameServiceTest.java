package net.seancoyne.fiveinarowclient.service;

import net.seancoyne.fiveinarowclient.client.FiveInARowClient;
import net.seancoyne.fiveinarowclient.model.Player;
import net.seancoyne.fiveinarowclient.model.response.*;
import net.seancoyne.fiveinarowclient.util.UserInteraction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class GameServiceTest {
    private FiveInARowClient client;
    private UserInteraction userInteraction;

    private GameService testInstance;

    @BeforeEach
    void setup() {
        client = Mockito.mock(FiveInARowClient.class);
        userInteraction = Mockito.mock(UserInteraction.class);

        testInstance = new GameService(client, userInteraction);
    }

    @Test
    void createNewGame_success() {
        // Given
        Player player = new Player();
        CreateGameResponse createGameResponse = new CreateGameResponse();
        createGameResponse.setResponseState(ResponseState.SUCCESS);
        createGameResponse.setGameId(1);

        when(client.createGame(player)).thenReturn(createGameResponse);

        // When
        boolean result = testInstance.createNewGame(player);

        // Then
        assertTrue(result);

        verify(userInteraction, times(1)).displayPlayerMessage("Your Game ID is 1 send this to your friend");
        verify(client, times(1)).createGame(player);
    }

    @Test
    void createNewGame_failed_with_response() {
        // Given
        Player player = new Player();
        CreateGameResponse createGameResponse = new CreateGameResponse();
        createGameResponse.setResponseState(ResponseState.FAILED);
        createGameResponse.setGameId(1);
        createGameResponse.setMessage("message");

        when(client.createGame(player)).thenReturn(createGameResponse);

        // When
        boolean result = testInstance.createNewGame(player);

        // Then
        assertFalse(result);

        verify(userInteraction, times(1)).displayPlayerMessage("Could not create game. Message From the Server: message");
        verify(client, times(1)).createGame(player);
    }

    @Test
    void createNewGame_failed_no_response() {
        // Given
        Player player = new Player();

        // When
        boolean result = testInstance.createNewGame(player);

        // Then
        assertFalse(result);

        verify(userInteraction, times(1)).displayPlayerMessage("Request to the server has failed, please check the connection");
        verify(client, times(1)).createGame(player);
    }

    @Test
    void joinExistingGame_success() {
        // Given
        Player player = new Player();
        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setResponseState(ResponseState.SUCCESS);

        when(client.registerPlayer(player)).thenReturn(registerResponse);

        // When
        boolean result = testInstance.joinExistingGame(player);

        // Then
        assertTrue(result);

        verify(client, times(1)).registerPlayer(player);
        verify(userInteraction, times(0)).displayPlayerMessage(anyString());
    }

    @Test
    void joinExistingGame_failed_with_response() {
        // Given
        Player player = new Player();
        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setResponseState(ResponseState.FAILED);
        registerResponse.setMessage("message");

        when(client.registerPlayer(player)).thenReturn(registerResponse);

        // When
        boolean result = testInstance.joinExistingGame(player);

        // Then
        assertFalse(result);

        verify(client, times(1)).registerPlayer(player);
        verify(userInteraction, times(1)).displayPlayerMessage("Could not join game. Message From the Server: message");
    }

    @Test
    void joinExistingGame_failed_no_response() {
        // Given
        Player player = new Player();

        // When
        boolean result = testInstance.joinExistingGame(player);

        // Then
        assertFalse(result);

        verify(client, times(1)).registerPlayer(player);
        verify(userInteraction, times(1)).displayPlayerMessage("Request to the server has failed, please check the connection");
    }

    @Test
    void test_play_with_no_response() throws IOException, InterruptedException {
        // Given
        Player player = new Player();
        CreateGameResponse createGameResponse = new CreateGameResponse();
        createGameResponse.setResponseState(ResponseState.SUCCESS);
        createGameResponse.setGameId(1);

        when(client.createGame(player)).thenReturn(createGameResponse);
        testInstance.createNewGame(player);

        // When
        testInstance.play();

        // Then
        verify(client, times(1)).getGameState(player);
        verify(userInteraction, times(1)).displayPlayerMessage("Request to the server has failed, please check the connection");
    }

    @Test
    void test_play_game_over_winner() throws IOException, InterruptedException {
        // Given
        Player player = new Player();
        CreateGameResponse createGameResponse = new CreateGameResponse();
        createGameResponse.setResponseState(ResponseState.SUCCESS);
        createGameResponse.setGameId(1);

        when(client.createGame(player)).thenReturn(createGameResponse);
        testInstance.createNewGame(player);

        GameStateResponse gameStateResponse = new GameStateResponse();
        gameStateResponse.setGameState(GameState.GAME_OVER);
        gameStateResponse.setWinner(true);
        gameStateResponse.setCurrentBoard(new String[6][9]);

        when(client.getGameState(player)).thenReturn(gameStateResponse);

        // When
        testInstance.play();

        // Then
        verify(client, times(1)).getGameState(player);
        verify(userInteraction, times(1)).displayPlayerMessage("Game over. You are the winner");
    }

    @Test
    void test_play_game_over_loser() throws IOException, InterruptedException {
        // Given
        Player player = new Player();
        CreateGameResponse createGameResponse = new CreateGameResponse();
        createGameResponse.setResponseState(ResponseState.SUCCESS);
        createGameResponse.setGameId(1);

        when(client.createGame(player)).thenReturn(createGameResponse);
        testInstance.createNewGame(player);

        GameStateResponse gameStateResponse = new GameStateResponse();
        gameStateResponse.setGameState(GameState.GAME_OVER);
        gameStateResponse.setWinner(false);
        gameStateResponse.setCurrentBoard(new String[6][9]);

        when(client.getGameState(player)).thenReturn(gameStateResponse);

        // When
        testInstance.play();

        // Then
        verify(client, times(1)).getGameState(player);
        verify(userInteraction, times(1)).displayPlayerMessage("Game over. You didn't win");
    }

    @Test
    void test_play_game_user_disconnects() throws IOException, InterruptedException {
        // Given
        Player player = new Player();
        player.setUsername("username");
        CreateGameResponse createGameResponse = new CreateGameResponse();
        createGameResponse.setResponseState(ResponseState.SUCCESS);
        createGameResponse.setGameId(1);

        when(client.createGame(player)).thenReturn(createGameResponse);
        testInstance.createNewGame(player);

        GameStateResponse gameStateResponse = new GameStateResponse();
        gameStateResponse.setGameState(GameState.YOUR_MOVE);
        gameStateResponse.setWinner(false);
        gameStateResponse.setCurrentBoard(new String[6][9]);

        when(client.getGameState(player)).thenReturn(gameStateResponse);

        DisconnectResponse disconnectResponse = new DisconnectResponse();
        disconnectResponse.setMessage("Message");
        when(client.disconnect(player)).thenReturn(disconnectResponse);

        when(userInteraction.getPlayerIntegerInputWithMessage("It’s your turn username, please enter column (1-9) or (-1 to disconnect)")).thenReturn(-1);

        // When
        testInstance.play();

        // Then
        verify(client, times(1)).disconnect(player);
        verify(client, times(1)).getGameState(player);
        verify(userInteraction, times(1)).displayPlayerMessage("Game over. You didn't win");
        verify(userInteraction, times(1)).displayPlayerMessage("Message");
    }
}