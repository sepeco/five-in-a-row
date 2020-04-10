package net.seancoyne.fiveinarowserver.controller;

import lombok.AllArgsConstructor;
import net.seancoyne.fiveinarowserver.model.MoveRequest;
import net.seancoyne.fiveinarowserver.model.RegisterRequest;
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
        return apiService.register(registerRequest);
    }

    @RequestMapping(value = "/checkTurn/{userName}", method = RequestMethod.GET)
    public ResponseEntity<?> checkTurn(@PathVariable("userName") String userName) {
        return apiService.checkTurn(userName);
    }

    @RequestMapping(value = "/gameState/{userName}", method = RequestMethod.GET)
    public ResponseEntity<?> getGameState(@PathVariable("userName") String userName) {
        return apiService.getGameState(userName);
    }

    @RequestMapping(value = "/makeMove/{userName}", method = RequestMethod.PUT)
    public ResponseEntity<?> makeMove(@PathVariable("userName") String userName, @RequestBody MoveRequest moveRequest) {
        return apiService.makeMove(userName, moveRequest);
    }

    @RequestMapping(value = "/disconnect/{userName}", method = RequestMethod.DELETE)
    public ResponseEntity<?> disconnectUser(@PathVariable("userName") String userName) {
        return apiService.disconnectUser(userName);
    }
}
