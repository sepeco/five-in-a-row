package net.seancoyne.fiveinarowclient.model.response;

import lombok.Data;

@Data
public class MoveResponse {
    private ResponseState responseState;
    private String message;
    private boolean tryAgain;
}
