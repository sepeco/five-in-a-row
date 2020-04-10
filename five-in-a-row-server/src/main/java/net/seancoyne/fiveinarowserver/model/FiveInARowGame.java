package net.seancoyne.fiveinarowserver.model;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import net.seancoyne.fiveinarowserver.model.request.DisconnectUserRequest;
import net.seancoyne.fiveinarowserver.model.request.GameStateRequest;
import net.seancoyne.fiveinarowserver.model.request.MoveRequest;
import net.seancoyne.fiveinarowserver.model.response.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Data
@Log4j2
public class FiveInARowGame {
    private Integer gameId;
    private Map<String, String> registeredPlayers;
    private String lastMoveByPlayer;
    private String winningPlayer;
    private String[][] board;
    private boolean activeGame;

    public FiveInARowGame() {
        gameId = new Random().nextInt(1000);
        registeredPlayers = new HashMap<>();
        board = new String[6][9];
        activeGame = true;
    }

    public int playerCount() {
        return registeredPlayers.size();
    }

    public RegisterResponse registerPlayer(String username, String playerColour) {

        if (!activeGame) {
            return RegisterResponse.builder()
                    .responseState(ResponseState.FAILED)
                    .message("This game is over. Try creating a new game")
                    .build();
        }

        if (registeredPlayers.containsKey(username)) {
            return RegisterResponse.builder()
                    .responseState(ResponseState.FAILED)
                    .message("Username already registered for this game")
                    .build();
        }

        if (registeredPlayers.containsValue(playerColour)) {
            return RegisterResponse.builder()
                    .responseState(ResponseState.FAILED)
                    .message("Selected colour is not available for this game")
                    .build();
        }

        if (playerCount() >= 2) {
            return RegisterResponse.builder()
                    .responseState(ResponseState.FAILED)
                    .message("Selected game is already full. Try creating a new game")
                    .build();
        }

        registeredPlayers.put(username, playerColour);

        return RegisterResponse.builder()
                .responseState(ResponseState.SUCCESS)
                .message("User registered for game")
                .build();
    }

    public GameStateResponse getGameStateForUser(GameStateRequest gameStateRequest) {

        String username = gameStateRequest.getUsername();

        // check user is registered
        if (!registeredPlayers.containsKey(username)) {
            return GameStateResponse.builder()
                    .responseState(ResponseState.FAILED)
                    .message("Username is not registered")
                    .build();
        }

        // check if there is already a winner for the game
        if (winningPlayer != null) {
            boolean thisIsTheWinningPlayer = winningPlayer.equals(username);

            return GameStateResponse.builder()
                    .responseState(ResponseState.SUCCESS)
                    .gameState(GameState.GAME_OVER)
                    .winner(thisIsTheWinningPlayer)
                    .message(thisIsTheWinningPlayer ? "Game Over. You Win" : "Game Over. You Lose")
                    .build();
        }

        if (!activeGame) {
            return GameStateResponse.builder()
                    .responseState(ResponseState.SUCCESS)
                    .gameState(GameState.GAME_OVER)
                    .message("This game is over. Try creating a new game")
                    .build();
        }

        // check the game has started
        if (registeredPlayers.size() == 1) {
            return GameStateResponse.builder()
                    .responseState(ResponseState.SUCCESS)
                    .gameState(GameState.WAITING_FOR_NEXT_PLAYER_TO_REGISTER)
                    .message("Waiting for the next player")
                    .build();
        }

        // is it this users turn to make a move
        if (!username.equals(lastMoveByPlayer)) {
            return GameStateResponse.builder()
                    .responseState(ResponseState.SUCCESS)
                    .gameState(GameState.YOUR_MOVE)
                    .currentBoard(board)
                    .message("Make your move")
                    .build();
        } else {
            return GameStateResponse.builder()
                    .responseState(ResponseState.SUCCESS)
                    .gameState(GameState.WAITING_FOR_NEXT_PLAYER_TO_MAKE_MOVE)
                    .currentBoard(board)
                    .message("Waiting for other player to make move")
                    .build();
        }
    }

    public MoveResponse makeMove(MoveRequest moveRequest) {

        String username = moveRequest.getUsername();

        // check user is registered
        if (!registeredPlayers.containsKey(username)) {
            return MoveResponse.builder()
                    .responseState(ResponseState.FAILED)
                    .message("Username is not registered")
                    .build();
        }

        if (!activeGame) {
            return MoveResponse.builder()
                    .responseState(ResponseState.FAILED)
                    .message("This game is over. Try creating a new game")
                    .build();
        }

        // did this user make the last move as well
        if (username.equals(lastMoveByPlayer)) {
            return MoveResponse.builder()
                    .responseState(ResponseState.FAILED)
                    .tryAgain(false)
                    .message("Its not your move yet")
                    .build();
        }

        int width = board.length;
        int height = board[0].length;

        if (moveRequest.getColumn() > width || moveRequest.getColumn() < 0) {
            return MoveResponse.builder()
                    .responseState(ResponseState.FAILED)
                    .tryAgain(true)
                    .message("The selected column is out of bounds of the board")
                    .build();
        }

        // if the column is full return an error
        if (board[moveRequest.getColumn() - 1][0] != null) {
            return MoveResponse.builder()
                    .responseState(ResponseState.FAILED)
                    .tryAgain(true)
                    .message("The selected column is full")
                    .build();
        }

        // starting at the bottom of the column put their disk at the next available space
        for (int i = height - 1; i >= 0; i--) {

            // if the space is free place their disk in that space and break the loop
            if (board[moveRequest.getColumn() - 1][i] == null) {
                board[moveRequest.getColumn() - 1][i] = registeredPlayers.get(username);
                break;
            }
        }

        lastMoveByPlayer = username;

        // check if this was the winning move and save the winner
        String result = checkForWinningColour();

        if (result != null && registeredPlayers.get(username).equals(result)) {
            winningPlayer = username;
            activeGame = false;
        }

        return MoveResponse.builder()
                .responseState(ResponseState.SUCCESS)
                .tryAgain(false)
                .message("Move made")
                .build();
    }

    public DisconnectResponse disconnectUser(DisconnectUserRequest disconnectUserRequest) {

        String username = disconnectUserRequest.getUsername();

        if (!registeredPlayers.containsKey(username)) {
            return DisconnectResponse.builder()
                    .responseState(ResponseState.FAILED)
                    .message("Username is not registered")
                    .build();
        }

        if (!activeGame) {
            return DisconnectResponse.builder()
                    .responseState(ResponseState.FAILED)
                    .message("This game is over. Try creating a new game")
                    .build();
        }

        registeredPlayers.remove(username);
        activeGame = false;

        return DisconnectResponse.builder()
                .responseState(ResponseState.SUCCESS)
                .message("User disconnected, game over")
                .build();
    }

    private String checkForWinningColour() {
        final int HEIGHT = board.length;
        final int WIDTH = board[0].length;
        final String EMPTY_SLOT = null;
        for (int r = 0; r < HEIGHT; r++) { // iterate rows, bottom to top
            for (int c = 0; c < WIDTH; c++) { // iterate columns, left to right
                String colour = board[r][c];
                if (colour == EMPTY_SLOT)
                    continue; // don't check empty slots

                if (c + 4 < WIDTH &&
                        colour == board[r][c + 1] && // look right
                        colour == board[r][c + 2] &&
                        colour == board[r][c + 3] &&
                        colour == board[r][c + 4])
                    return colour;
                if (r + 4 < HEIGHT) {
                    if (colour == board[r + 1][c] && // look up
                            colour == board[r + 2][c] &&
                            colour == board[r + 3][c] &&
                            colour == board[r + 4][c])
                        return colour;
                    if (c + 4 < WIDTH &&
                            colour == board[r + 1][c + 1] && // look up & right
                            colour == board[r + 2][c + 2] &&
                            colour == board[r + 3][c + 3] &&
                            colour == board[r + 3][c + 4])
                        return colour;
                    if (c - 4 >= 0 &&
                            colour == board[r + 1][c - 1] && // look up & left
                            colour == board[r + 2][c - 2] &&
                            colour == board[r + 3][c - 3] &&
                            colour == board[r + 3][c - 4])
                        return colour;
                }
            }
        }
        return EMPTY_SLOT; // no winner found
    }
}
