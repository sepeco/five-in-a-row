package net.seancoyne.fiveinarowserver.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.seancoyne.fiveinarowserver.model.*;
import net.seancoyne.fiveinarowserver.validate.RequestDetailsValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@AllArgsConstructor
public class ApiService {

    private RequestDetailsValidator requestDetailsValidator;
    private BizService bizService;

    public ResponseEntity<?> register(RegisterRequest registerRequest) {

        if (requestDetailsValidator.requiredParametersIsInvalid(registerRequest)) {
            log.warn("Provided parameters are not valid: {}", registerRequest);
            return new ResponseEntity<>("Required Parameters Are Invalid", HttpStatus.BAD_REQUEST);
        }

        RegisterResponse response = bizService.register(registerRequest);

        if (ResponseState.FAILED.equals(response.getResponseState())) {
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> checkTurn(String userName) {

        if (requestDetailsValidator.requiredParametersIsInvalid(userName)) {
            log.warn("Provided parameters are not valid: {}", userName);
            return new ResponseEntity<>("Required Parameters Are Invalid", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(userName, HttpStatus.OK);
    }

    public ResponseEntity<?> getGameState(String userName) {

        if (requestDetailsValidator.requiredParametersIsInvalid(userName)) {
            log.warn("Provided parameters are not valid: {}", userName);
            return new ResponseEntity<>("Required Parameters Are Invalid", HttpStatus.BAD_REQUEST);
        }

        GameStateResponse response = bizService.getGameState(userName);

        if (ResponseState.FAILED.equals(response.getResponseState())) {
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> makeMove(String userName, MoveRequest moveRequest) {

        if (requestDetailsValidator.requiredParametersIsInvalid(userName, moveRequest)) {
            log.warn("Provided parameters are not valid: {}, {}", userName, moveRequest);
            return new ResponseEntity<>("Required Parameters Are Invalid", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(userName, HttpStatus.OK);
    }

    public ResponseEntity<?> disconnectUser(String userName) {

        if (requestDetailsValidator.requiredParametersIsInvalid(userName)) {
            log.warn("Provided parameters are not valid: {}", userName);
            return new ResponseEntity<>("Required Parameters Are Invalid", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(userName, HttpStatus.OK);
    }
}