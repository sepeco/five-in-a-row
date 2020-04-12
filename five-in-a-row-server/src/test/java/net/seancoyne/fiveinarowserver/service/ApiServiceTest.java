package net.seancoyne.fiveinarowserver.service;

import net.seancoyne.fiveinarowserver.model.request.*;
import net.seancoyne.fiveinarowserver.model.response.*;
import net.seancoyne.fiveinarowserver.validate.RequestDetailsValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ApiServiceTest {

    private RequestDetailsValidator requestDetailsValidator;
    private GameService gameService;
    private ApiService testInstance;


    @BeforeEach
    public void setup() {
        requestDetailsValidator = Mockito.mock(RequestDetailsValidator.class);
        gameService = Mockito.mock(GameService.class);

        testInstance = new ApiService(requestDetailsValidator, gameService);
    }

    @Test
    public void test_registerPlayer_success() {
        // Given
        RegisterRequest registerRequest = new RegisterRequest();
        RegisterResponse registerResponse = RegisterResponse.builder()
                .responseState(ResponseState.SUCCESS)
                .build();
        when(requestDetailsValidator.requiredParametersIsInvalid(registerRequest)).thenReturn(false);
        when(gameService.registerPlayer(registerRequest)).thenReturn(registerResponse);

        // When
        ResponseEntity responseEntity = testInstance.registerPlayer(registerRequest);

        // Then
        assertNotNull(responseEntity);

        assertEquals(registerResponse, responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        verify(requestDetailsValidator, times(1)).requiredParametersIsInvalid(registerRequest);
        verify(gameService, times(1)).registerPlayer(registerRequest);
    }

    @Test
    public void test_registerPlayer_failed_validation() {
        // Given
        RegisterRequest registerRequest = new RegisterRequest();
        RegisterResponse registerResponse = RegisterResponse.builder()
                .responseState(ResponseState.SUCCESS)
                .build();
        when(requestDetailsValidator.requiredParametersIsInvalid(registerRequest)).thenReturn(true);

        // When
        ResponseEntity responseEntity = testInstance.registerPlayer(registerRequest);

        // Then
        assertNotNull(responseEntity);

        assertEquals("Required Parameters Are Invalid", responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        verify(requestDetailsValidator, times(1)).requiredParametersIsInvalid(registerRequest);
        verify(gameService, times(0)).registerPlayer(registerRequest);
    }

    @Test
    public void test_createGame_success() {
        // Given
        CreateGameRequest createGameRequest = new CreateGameRequest();
        CreateGameResponse createGameResponse = CreateGameResponse.builder()
                .responseState(ResponseState.SUCCESS)
                .build();

        when(requestDetailsValidator.requiredParametersIsInvalid(createGameRequest)).thenReturn(false);
        when(gameService.createGame(createGameRequest)).thenReturn(createGameResponse);

        // When
        ResponseEntity responseEntity = testInstance.createGame(createGameRequest);

        // Then
        assertNotNull(responseEntity);

        assertEquals(createGameResponse, responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        verify(requestDetailsValidator, times(1)).requiredParametersIsInvalid(createGameRequest);
        verify(gameService, times(1)).createGame(createGameRequest);
    }

    @Test
    public void test_createGame_failed_validation() {
        // Given
        CreateGameRequest createGameRequest = new CreateGameRequest();
        CreateGameResponse createGameResponse = CreateGameResponse.builder()
                .responseState(ResponseState.SUCCESS)
                .build();

        when(requestDetailsValidator.requiredParametersIsInvalid(createGameRequest)).thenReturn(true);

        // When
        ResponseEntity responseEntity = testInstance.createGame(createGameRequest);

        // Then
        assertNotNull(responseEntity);

        assertEquals("Required Parameters Are Invalid", responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        verify(requestDetailsValidator, times(1)).requiredParametersIsInvalid(createGameRequest);
        verify(gameService, times(0)).createGame(createGameRequest);
    }

    @Test
    public void test_getGameState_success() {
        // Given
        Integer gameId = 0;
        String username = "username";

        GameStateResponse gameStateResponse = GameStateResponse.builder()
                .responseState(ResponseState.SUCCESS)
                .build();

        when(requestDetailsValidator.requiredParametersIsInvalid(gameId, username)).thenReturn(false);
        when(gameService.getGameStateForUser(any(GameStateRequest.class))).thenReturn(gameStateResponse);

        // When
        ResponseEntity responseEntity = testInstance.getGameState(gameId, username);

        // Then
        assertNotNull(responseEntity);

        assertEquals(gameStateResponse, responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        verify(requestDetailsValidator, times(1)).requiredParametersIsInvalid(gameId, username);

        ArgumentCaptor<GameStateRequest> stateCaptor = ArgumentCaptor.forClass(GameStateRequest.class);
        verify(gameService, times(1)).getGameStateForUser(stateCaptor.capture());

        List<GameStateRequest> capturedRequests = stateCaptor.getAllValues();
        assertEquals(username, capturedRequests.get(0).getUsername());
        assertEquals(gameId, capturedRequests.get(0).getGameId());
    }

    @Test
    public void test_getGameState_failed_validation() {
        // Given
        Integer gameId = 0;
        String username = "username";

        GameStateResponse gameStateResponse = GameStateResponse.builder()
                .responseState(ResponseState.SUCCESS)
                .build();

        when(requestDetailsValidator.requiredParametersIsInvalid(gameId, username)).thenReturn(true);

        // When
        ResponseEntity responseEntity = testInstance.getGameState(gameId, username);

        // Then
        assertNotNull(responseEntity);

        assertEquals("Required Parameters Are Invalid", responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        verify(requestDetailsValidator, times(1)).requiredParametersIsInvalid(gameId, username);
        verify(gameService, times(0)).getGameStateForUser(any(GameStateRequest.class));
    }

    @Test
    public void test_makeMove_success() {
        // Given
        MoveRequest moveRequest = new MoveRequest();
        MoveResponse moveResponse = MoveResponse.builder()
                .responseState(ResponseState.SUCCESS)
                .build();

        when(requestDetailsValidator.requiredParametersIsInvalid(moveRequest)).thenReturn(false);
        when(gameService.makeMove(moveRequest)).thenReturn(moveResponse);

        // When
        ResponseEntity responseEntity = testInstance.makeMove(moveRequest);

        // Then
        assertNotNull(responseEntity);
        assertEquals(moveResponse, responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        verify(requestDetailsValidator, times(1)).requiredParametersIsInvalid(moveRequest);
        verify(gameService, times(1)).makeMove(moveRequest);
    }

    @Test
    public void test_makeMove_failed_validation() {
        // Given
        MoveRequest moveRequest = new MoveRequest();
        MoveResponse moveResponse = MoveResponse.builder()
                .responseState(ResponseState.SUCCESS)
                .build();

        when(requestDetailsValidator.requiredParametersIsInvalid(moveRequest)).thenReturn(true);

        // When
        ResponseEntity responseEntity = testInstance.makeMove(moveRequest);

        // Then
        assertNotNull(responseEntity);
        assertEquals("Required Parameters Are Invalid", responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        verify(requestDetailsValidator, times(1)).requiredParametersIsInvalid(moveRequest);
        verify(gameService, times(0)).makeMove(moveRequest);
    }

    @Test
    public void test_disconnectUser_success() {
        // Given
        Integer gameId = 0;
        String username = "username";

        DisconnectResponse response = DisconnectResponse.builder()
                .responseState(ResponseState.SUCCESS)
                .build();

        when(requestDetailsValidator.requiredParametersIsInvalid(gameId, username)).thenReturn(false);
        when(gameService.disconnectUser(any(DisconnectUserRequest.class))).thenReturn(response);

        // When
        ResponseEntity responseEntity = testInstance.disconnectUser(gameId, username);

        // Then
        assertNotNull(responseEntity);

        assertEquals(response, responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        verify(requestDetailsValidator, times(1)).requiredParametersIsInvalid(gameId, username);

        ArgumentCaptor<DisconnectUserRequest> stateCaptor = ArgumentCaptor.forClass(DisconnectUserRequest.class);
        verify(gameService, times(1)).disconnectUser(stateCaptor.capture());

        List<DisconnectUserRequest> capturedRequests = stateCaptor.getAllValues();
        assertEquals(username, capturedRequests.get(0).getUsername());
        assertEquals(gameId, capturedRequests.get(0).getGameId());
    }

    @Test
    public void test_disconnectUser_failed_validation() {
        // Given
        Integer gameId = 0;
        String username = "username";

        DisconnectResponse response = DisconnectResponse.builder()
                .responseState(ResponseState.SUCCESS)
                .build();

        when(requestDetailsValidator.requiredParametersIsInvalid(gameId, username)).thenReturn(true);

        // When
        ResponseEntity responseEntity = testInstance.disconnectUser(gameId, username);

        // Then
        assertNotNull(responseEntity);

        assertEquals("Required Parameters Are Invalid", responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        verify(requestDetailsValidator, times(1)).requiredParametersIsInvalid(gameId, username);
        verify(gameService, times(0)).disconnectUser(any(DisconnectUserRequest.class));
    }
}