package net.seancoyne.fiveinarowserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GameController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "Welcome to 5 in a row!!";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody String data) {
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @RequestMapping(value = "/checkTurn/{userName}", method = RequestMethod.GET)
    public ResponseEntity<?> checkTurn(@PathVariable("userName") String userName) {
        return new ResponseEntity<>(userName, HttpStatus.OK);
    }

    @RequestMapping(value = "/gameState", method = RequestMethod.GET)
    public ResponseEntity<?> getGameState() {
        return new ResponseEntity<>("state", HttpStatus.OK);
    }

    @RequestMapping(value = "/makeMove/{userName}", method = RequestMethod.PUT)
    public ResponseEntity<?> makeMove(@PathVariable("userName") String userName, @RequestBody String data) {
        return new ResponseEntity<>(userName, HttpStatus.OK);
    }

    @RequestMapping(value = "/disconnect/{userName}", method = RequestMethod.DELETE)
    public ResponseEntity<?> disconnectUser(@PathVariable("userName") String userName) {
        return new ResponseEntity<>(userName, HttpStatus.OK);
    }
}
