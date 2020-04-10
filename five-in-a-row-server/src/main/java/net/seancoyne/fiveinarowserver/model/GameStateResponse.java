package net.seancoyne.fiveinarowserver.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GameStateResponse {
    private ResponseState responseState;
    private GameState gameState;
    private int[][] currentBoard;
    private String message;
}
