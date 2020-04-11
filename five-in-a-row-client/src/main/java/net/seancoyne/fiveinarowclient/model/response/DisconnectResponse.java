package net.seancoyne.fiveinarowclient.model.response;

import lombok.Data;

@Data
public class DisconnectResponse {
    private ResponseState responseState;
    private String message;
}
