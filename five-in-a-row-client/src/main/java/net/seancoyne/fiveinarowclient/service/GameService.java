package net.seancoyne.fiveinarowclient.service;

import lombok.RequiredArgsConstructor;
import net.seancoyne.fiveinarowclient.client.FiveInARowClient;
import net.seancoyne.fiveinarowclient.model.Player;
import net.seancoyne.fiveinarowclient.model.response.*;
import net.seancoyne.fiveinarowclient.util.UserInteraction;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class GameService {

    private final FiveInARowClient client;
    private final UserInteraction userInteraction;
    private Player player;

    public boolean createNewGame(Player player) {

        CreateGameResponse response = client.createGame(player);

        if (response == null) {
            userInteraction.displayPlayerMessage("Request to the server has failed, please check the connection");
            return false;
        }

        if (response.getResponseState().equals(ResponseState.FAILED)) {
            userInteraction.displayPlayerMessage("Could not create game. Message From the Server: " + response.getMessage());
            return false;
        }

        player.setGameId(response.getGameId());
        userInteraction.displayPlayerMessage("Your Game ID is " + response.getGameId() + " send this to your friend");

        this.player = player;
        return true;
    }

    public boolean joinExistingGame(Player player) {
        RegisterResponse response = client.registerPlayer(player);

        if (response == null) {
            userInteraction.displayPlayerMessage("Request to the server has failed, please check the connection");
            return false;
        }

        if (response.getResponseState().equals(ResponseState.FAILED)) {
            userInteraction.displayPlayerMessage("Could not join game. Message From the Server: " + response.getMessage());
            return false;
        }

        this.player = player;
        return true;
    }

    public void play() throws InterruptedException, IOException {

        boolean isWinner = false;
        boolean activeGame = true;
        GameState lastState = null;

        while (activeGame) {

            GameStateResponse response = client.getGameState(player);

            if (response == null) {
                userInteraction.displayPlayerMessage("Request to the server has failed, please check the connection");
                return;
            }

            // Has game state changed
            if (!response.getGameState().equals(lastState)) {
                lastState = response.getGameState();

                // Check is the game over
                if (GameState.GAME_OVER.equals(response.getGameState())) {
                    isWinner = response.isWinner();
                    activeGame = false;
                }

                //print out server message
                userInteraction.displayPlayerMessage(response.getMessage());

                //print out the board
                String[][] board = response.getCurrentBoard();
                if (board != null) {
                    printBoard(board);
                }

                // if your turn get input
                if (response.getGameState() == GameState.YOUR_MOVE) {

                    // allow repeat moves if invalid request
                    boolean tryToMakeMove = true;
                    while (tryToMakeMove) {
                        int nextMove = userInteraction.getPlayerIntegerInputWithMessage("It’s your turn " + player.getUsername() + ", please enter column (" + 1 + "-" + (board[0].length) + ")");
                        MoveResponse moveResponse = client.makeMove(player, nextMove);

                        if (moveResponse.getResponseState().equals(ResponseState.FAILED)) {
                            userInteraction.displayPlayerMessage("Move failed. Message From the Server: " + response.getMessage());
                        }
                        tryToMakeMove = moveResponse.isTryAgain();
                    }
                }
            }
            Thread.sleep(1000);
        }

        gameOver(isWinner);
    }

    private void gameOver(boolean winner) {
        userInteraction.displayPlayerMessage(winner ? "Game over. You are the winner" : "Game over. You didn't win");
    }

    private void printBoard(String[][] board) {
        int width = board.length;
        int height = board[0].length;

        for (int i = 0; i < width; i++) {
            StringBuilder builder = new StringBuilder();

            for (int j = 0; j < height; j++) {
                String[] row = board[i];

                String element = row[j];
                builder.append("[").append(element == null ? " " : element).append("]");
            }

            userInteraction.displayPlayerMessage(builder.toString());
        }
    }
}
