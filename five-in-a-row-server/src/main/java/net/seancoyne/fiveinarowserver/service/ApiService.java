package net.seancoyne.fiveinarowserver.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.seancoyne.fiveinarowserver.model.request.*;
import net.seancoyne.fiveinarowserver.model.response.*;
import net.seancoyne.fiveinarowserver.validate.RequestDetailsValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@AllArgsConstructor
public class ApiService {

    private RequestDetailsValidator requestDetailsValidator;
    private GameService gameService;

    public ResponseEntity<?> registerPlayer(RegisterRequest registerRequest) {

        if (requestDetailsValidator.requiredParametersIsInvalid(registerRequest)) {
            log.warn("Provided parameters are not valid: {}", registerRequest);
            return new ResponseEntity<>("Required Parameters Are Invalid", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(gameService.registerPlayer(registerRequest), HttpStatus.OK);
    }

    public ResponseEntity<?> createGame(CreateGameRequest createGameRequest) {

        if (requestDetailsValidator.requiredParametersIsInvalid(createGameRequest)) {
            log.warn("Provided parameters are not valid: {}", createGameRequest);
            return new ResponseEntity<>("Required Parameters Are Invalid", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(gameService.createGame(createGameRequest), HttpStatus.OK);
    }

    public ResponseEntity<?> getGameState(Integer gameId, String userName) {

        if (requestDetailsValidator.requiredParametersIsInvalid(gameId, userName)) {
            log.warn("Provided parameters are not valid: {}, {}", gameId, userName);
            return new ResponseEntity<>("Required Parameters Are Invalid", HttpStatus.BAD_REQUEST);
        }

        GameStateRequest gameStateRequest = GameStateRequest.builder()
                .gameId(gameId)
                .username(userName)
                .build();

        return new ResponseEntity<>(gameService.getGameStateForUser(gameStateRequest), HttpStatus.OK);
    }

    public ResponseEntity<?> makeMove(MoveRequest moveRequest) {

        if (requestDetailsValidator.requiredParametersIsInvalid(moveRequest)) {
            log.warn("Provided parameters are not valid: {}", moveRequest);
            return new ResponseEntity<>("Required Parameters Are Invalid", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(gameService.makeMove(moveRequest), HttpStatus.OK);
    }

    public ResponseEntity<?> disconnectUser(Integer gameId, String username) {

        if (requestDetailsValidator.requiredParametersIsInvalid(gameId, username)) {
            log.warn("Provided parameters are not valid: {}, {}", gameId, username);
            return new ResponseEntity<>("Required Parameters Are Invalid", HttpStatus.BAD_REQUEST);
        }

        DisconnectUserRequest disconnectUserRequest = DisconnectUserRequest.builder()
                .gameId(gameId)
                .username(username)
                .build();

        return new ResponseEntity<>(gameService.disconnectUser(disconnectUserRequest), HttpStatus.OK);
    }
}
