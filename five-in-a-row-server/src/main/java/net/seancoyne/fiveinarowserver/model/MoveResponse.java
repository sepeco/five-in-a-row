package net.seancoyne.fiveinarowserver.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MoveResponse {
    private ResponseState responseState;
    private String message;
    private boolean tryAgain;
    private boolean winningMove;
}
