package net.seancoyne.fiveinarowclient.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.seancoyne.fiveinarowclient.model.Player;
import net.seancoyne.fiveinarowclient.model.request.CreateGameRequest;
import net.seancoyne.fiveinarowclient.model.request.MoveRequest;
import net.seancoyne.fiveinarowclient.model.request.RegisterRequest;
import net.seancoyne.fiveinarowclient.model.response.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FiveInARowClientTest {

    private RestTemplate restTemplate;
    private HttpHeaders httpHeaders;
    private ObjectMapper objectMapper;

    private FiveInARowClient testInstance;

    @BeforeEach
    void setup() {
        restTemplate = Mockito.mock(RestTemplate.class);
        httpHeaders = Mockito.mock(HttpHeaders.class);
        objectMapper = Mockito.mock(ObjectMapper.class);

        testInstance = new FiveInARowClient(restTemplate, httpHeaders, objectMapper);
    }

    @Test
    void createGame() throws JsonProcessingException {
        // Given
        String username = "username";
        String gameColour = "X";

        Player player = new Player();
        player.setUsername(username);
        player.setGameId(null);
        player.setColourChoice(gameColour);

        ResponseEntity mockResponseEntity = Mockito.mock(ResponseEntity.class);
        CreateGameResponse createGameResponse = new CreateGameResponse();

        when(restTemplate.exchange(
                eq("http://localhost:8080/createGame"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(CreateGameResponse.class)
        )).thenReturn(mockResponseEntity);

        when(mockResponseEntity.getBody()).thenReturn(createGameResponse);

        // When
        CreateGameResponse response = testInstance.createGame(player);

        // Then
        assertEquals(createGameResponse, response);

        verify(restTemplate, times(1)).exchange(
                eq("http://localhost:8080/createGame"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(CreateGameResponse.class)
        );

        ArgumentCaptor<CreateGameRequest> stateCaptor = ArgumentCaptor.forClass(CreateGameRequest.class);
        verify(objectMapper, times(1)).writeValueAsString(stateCaptor.capture());

        List<CreateGameRequest> capturedRequests = stateCaptor.getAllValues();
        assertEquals(username, capturedRequests.get(0).getUsername());
        assertEquals(gameColour, capturedRequests.get(0).getSelectedColour());
    }

    @Test
    public void registerPlayer() throws JsonProcessingException {
        // Given
        String username = "username";
        String gameColour = "X";

        Player player = new Player();
        player.setUsername(username);
        player.setGameId(1);
        player.setColourChoice(gameColour);

        ResponseEntity mockResponseEntity = Mockito.mock(ResponseEntity.class);
        RegisterResponse registerResponse = new RegisterResponse();

        when(restTemplate.exchange(
                eq("http://localhost:8080/register"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(RegisterResponse.class)
        )).thenReturn(mockResponseEntity);

        when(mockResponseEntity.getBody()).thenReturn(registerResponse);

        // When
        RegisterResponse response = testInstance.registerPlayer(player);

        // Then
        assertEquals(registerResponse, response);

        verify(restTemplate, times(1)).exchange(
                eq("http://localhost:8080/register"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(RegisterResponse.class)
        );

        ArgumentCaptor<RegisterRequest> stateCaptor = ArgumentCaptor.forClass(RegisterRequest.class);
        verify(objectMapper, times(1)).writeValueAsString(stateCaptor.capture());

        List<RegisterRequest> capturedRequests = stateCaptor.getAllValues();
        assertEquals(username, capturedRequests.get(0).getUserName());
        assertEquals(gameColour, capturedRequests.get(0).getColour());
    }

    @Test
    public void makeMove() throws JsonProcessingException {
        // Given
        String username = "username";
        String gameColour = "X";
        Integer move = 5;

        Player player = new Player();
        player.setUsername(username);
        player.setGameId(1);
        player.setColourChoice(gameColour);

        ResponseEntity mockResponseEntity = Mockito.mock(ResponseEntity.class);
        MoveResponse registerResponse = new MoveResponse();

        when(restTemplate.exchange(
                eq("http://localhost:8080/makeMove"),
                eq(HttpMethod.PUT),
                any(HttpEntity.class),
                eq(MoveResponse.class)
        )).thenReturn(mockResponseEntity);

        when(mockResponseEntity.getBody()).thenReturn(registerResponse);

        // When
        MoveResponse response = testInstance.makeMove(player, move);

        // Then
        assertEquals(registerResponse, response);

        verify(restTemplate, times(1)).exchange(
                eq("http://localhost:8080/makeMove"),
                eq(HttpMethod.PUT),
                any(HttpEntity.class),
                eq(MoveResponse.class)
        );

        ArgumentCaptor<MoveRequest> stateCaptor = ArgumentCaptor.forClass(MoveRequest.class);
        verify(objectMapper, times(1)).writeValueAsString(stateCaptor.capture());

        List<MoveRequest> capturedRequests = stateCaptor.getAllValues();
        assertEquals(username, capturedRequests.get(0).getUsername());
        assertEquals(move, capturedRequests.get(0).getColumn());
        assertEquals(1, capturedRequests.get(0).getGameId());
    }

    @Test
    public void getGameState() {
        // Given
        String username = "username";

        Player player = new Player();
        player.setUsername(username);
        player.setGameId(1);

        ResponseEntity mockResponseEntity = Mockito.mock(ResponseEntity.class);
        GameStateResponse gameStateResponse = new GameStateResponse();

        when(restTemplate.exchange(
                eq("http://localhost:8080/gameState/1/user/username"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(GameStateResponse.class)
        )).thenReturn(mockResponseEntity);

        when(mockResponseEntity.getBody()).thenReturn(gameStateResponse);

        // When
        GameStateResponse response = testInstance.getGameState(player);

        // Then
        assertEquals(gameStateResponse, response);

        verify(restTemplate, times(1)).exchange(
                eq("http://localhost:8080/gameState/1/user/username"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(GameStateResponse.class)
        );
    }

    @Test
    public void disconnect() {
        // Given
        String username = "username";

        Player player = new Player();
        player.setUsername(username);
        player.setGameId(1);

        ResponseEntity mockResponseEntity = Mockito.mock(ResponseEntity.class);
        DisconnectResponse gameStateResponse = new DisconnectResponse();

        when(restTemplate.exchange(
                eq("http://localhost:8080/disconnect/1/user/username"),
                eq(HttpMethod.DELETE),
                any(HttpEntity.class),
                eq(DisconnectResponse.class)
        )).thenReturn(mockResponseEntity);

        when(mockResponseEntity.getBody()).thenReturn(gameStateResponse);

        // When
        DisconnectResponse response = testInstance.disconnect(player);

        // Then
        assertEquals(gameStateResponse, response);

        verify(restTemplate, times(1)).exchange(
                eq("http://localhost:8080/disconnect/1/user/username"),
                eq(HttpMethod.DELETE),
                any(HttpEntity.class),
                eq(DisconnectResponse.class)
        );
    }
}