package net.seancoyne.fiveinarowserver.model.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GameStateResponse {
    private ResponseState responseState;
    private GameState gameState;
    private String[][] currentBoard;
    private String message;
    private boolean winner;
}
