package net.seancoyne.fiveinarowserver.service;

import net.seancoyne.fiveinarowserver.games.FiveInARowGame;
import net.seancoyne.fiveinarowserver.model.request.*;
import net.seancoyne.fiveinarowserver.model.response.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GameService {

    private Map<Integer, FiveInARowGame> createdGames;

    public GameService() {
        createdGames = new HashMap<>();
    }

    public CreateGameResponse createGame(CreateGameRequest createGameRequest) {
        FiveInARowGame newFiveInARowGame = new FiveInARowGame();

        RegisterResponse registerResponse = newFiveInARowGame.registerPlayer(
                createGameRequest.getUsername(),
                createGameRequest.getSelectedColour()
        );

        if (registerResponse.getResponseState().equals(ResponseState.SUCCESS)) {
            createdGames.put(newFiveInARowGame.getGameId(), newFiveInARowGame);

            return CreateGameResponse.builder()
                    .responseState(ResponseState.SUCCESS)
                    .gameId(newFiveInARowGame.getGameId())
                    .message("New game created! Send the game id to your friend")
                    .build();
        } else {
            return CreateGameResponse.builder()
                    .responseState(ResponseState.FAILED)
                    .gameId(newFiveInARowGame.getGameId())
                    .message("Could not register this user for the game")
                    .build();
        }
    }

    public RegisterResponse registerPlayer(RegisterRequest registerRequest) {

        FiveInARowGame selectedFiveInARowGame = createdGames.get(registerRequest.getGameId());

        if (selectedFiveInARowGame == null) {
            return RegisterResponse.builder()
                    .responseState(ResponseState.FAILED)
                    .message("No game with this ID exists")
                    .build();
        }

        return selectedFiveInARowGame.registerPlayer(registerRequest.getUserName(), registerRequest.getColour());
    }

    public MoveResponse makeMove(MoveRequest moveRequest) {

        FiveInARowGame selectedFiveInARowGame = createdGames.get(moveRequest.getGameId());

        if (selectedFiveInARowGame == null) {
            return MoveResponse.builder()
                    .responseState(ResponseState.FAILED)
                    .message("No game with this ID exists")
                    .build();
        }

        return selectedFiveInARowGame.makeMove(moveRequest);
    }

    public GameStateResponse getGameStateForUser(GameStateRequest gameStateRequest) {

        FiveInARowGame selectedFiveInARowGame = createdGames.get(gameStateRequest.getGameId());

        if (selectedFiveInARowGame == null) {
            return GameStateResponse.builder()
                    .responseState(ResponseState.FAILED)
                    .message("No game with this ID exists")
                    .build();
        }

        return selectedFiveInARowGame.getGameStateForUser(gameStateRequest);
    }

    public DisconnectResponse disconnectUser(DisconnectUserRequest disconnectUserRequest) {

        FiveInARowGame selectedFiveInARowGame = createdGames.get(disconnectUserRequest.getGameId());

        if (selectedFiveInARowGame == null) {
            return DisconnectResponse.builder()
                    .responseState(ResponseState.FAILED)
                    .message("No game with this ID exists")
                    .build();
        }

        return selectedFiveInARowGame.disconnectUser(disconnectUserRequest);
    }
}
