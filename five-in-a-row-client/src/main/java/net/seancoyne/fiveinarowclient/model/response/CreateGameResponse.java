package net.seancoyne.fiveinarowclient.model.response;

import lombok.Data;

@Data
public class CreateGameResponse {
    private ResponseState responseState;
    private Integer gameId;
    private String message;
}
