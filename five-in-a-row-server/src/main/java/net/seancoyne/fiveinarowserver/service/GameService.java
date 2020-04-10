package net.seancoyne.fiveinarowserver.service;

import net.seancoyne.fiveinarowserver.model.*;
import net.seancoyne.fiveinarowserver.model.request.*;
import net.seancoyne.fiveinarowserver.model.response.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GameService {

    private Map<Integer, Game> createdGames;

    public GameService() {
        createdGames = new HashMap<>();
    }

    public CreateGameResponse createGame(CreateGameRequest createGameRequest) {
        Game newGame = new Game();

        RegisterResponse registerResponse = newGame.registerPlayer(
                createGameRequest.getUsername(),
                createGameRequest.getSelectedColour()
        );

        if (registerResponse.getResponseState().equals(ResponseState.SUCCESS)) {
            createdGames.put(newGame.getGameId(), newGame);

            return CreateGameResponse.builder()
                    .responseState(ResponseState.SUCCESS)
                    .gameId(newGame.getGameId())
                    .message("New game created! Send the game id to your friend")
                    .build();
        } else {
            return CreateGameResponse.builder()
                    .responseState(ResponseState.FAILED)
                    .gameId(newGame.getGameId())
                    .message("Could not create a new game for this user")
                    .build();
        }
    }

    public RegisterResponse registerPlayer(RegisterRequest registerRequest) {

        Game selectedGame = createdGames.get(registerRequest.getGameId());

        if (selectedGame == null) {
            return RegisterResponse.builder()
                    .responseState(ResponseState.FAILED)
                    .message("No game with this ID exists")
                    .build();
        }

        return selectedGame.registerPlayer(registerRequest.getUserName(), registerRequest.getColour());
    }

    public MoveResponse makeMove(MoveRequest moveRequest) {

        Game selectedGame = createdGames.get(moveRequest.getGameId());

        if (selectedGame == null) {
            return MoveResponse.builder()
                    .responseState(ResponseState.FAILED)
                    .message("No game with this ID exists")
                    .build();
        }

        return selectedGame.makeMove(moveRequest);
    }

    public GameStateResponse getGameStateForUser(GameStateRequest gameStateRequest) {

        Game selectedGame = createdGames.get(gameStateRequest.getGameId());

        if (selectedGame == null) {
            return GameStateResponse.builder()
                    .responseState(ResponseState.FAILED)
                    .message("No game with this ID exists")
                    .build();
        }

        return selectedGame.getGameStateForUser(gameStateRequest);
    }

    public DisconnectResponse disconnectUser(DisconnectUserRequest disconnectUserRequest) {

        Game selectedGame = createdGames.get(disconnectUserRequest.getGameId());

        if (selectedGame == null) {
            return DisconnectResponse.builder()
                    .responseState(ResponseState.FAILED)
                    .message("No game with this ID exists")
                    .build();
        }

        return selectedGame.disconnectUser(disconnectUserRequest);
    }
}
