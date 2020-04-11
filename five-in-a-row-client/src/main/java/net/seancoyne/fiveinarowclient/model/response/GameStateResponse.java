package net.seancoyne.fiveinarowclient.model.response;

import lombok.Data;

@Data
public class GameStateResponse {
    private ResponseState responseState;
    private GameState gameState;
    private String[][] currentBoard;
    private String message;
    private boolean winner;
}
