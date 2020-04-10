package net.seancoyne.fiveinarowserver.controller;

import lombok.AllArgsConstructor;
import net.seancoyne.fiveinarowserver.model.request.CreateGameRequest;
import net.seancoyne.fiveinarowserver.model.request.MoveRequest;
import net.seancoyne.fiveinarowserver.model.request.RegisterRequest;
import net.seancoyne.fiveinarowserver.service.ApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class GameController {

    private ApiService apiService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "Welcome to 5 in a row!!";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        return apiService.registerPlayer(registerRequest);
    }

    @RequestMapping(value = "/createGame", method = RequestMethod.GET)
    public ResponseEntity<?> createGame(@RequestBody CreateGameRequest createGameRequest) {
        return apiService.createGame(createGameRequest);
    }

    @RequestMapping(value = "/gameState/{gameId}/user/{userName}", method = RequestMethod.GET)
    public ResponseEntity<?> getGameState(@PathVariable("userName") Integer gameId,
                                          @PathVariable("userName") String userName) {
        return apiService.getGameState(gameId, userName);
    }

    @RequestMapping(value = "/makeMove", method = RequestMethod.PUT)
    public ResponseEntity<?> makeMove(@RequestBody MoveRequest moveRequest) {
        return apiService.makeMove(moveRequest);
    }

    @RequestMapping(value = "/disconnect/{gameId}/user/{userName}", method = RequestMethod.DELETE)
    public ResponseEntity<?> disconnectUser(@PathVariable("userName") Integer gameId,
                                            @PathVariable("userName") String userName) {
        return apiService.disconnectUser(gameId, userName);
    }
}
