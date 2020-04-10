package net.seancoyne.fiveinarowserver.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DisconnectResponse {
    private ResponseState responseState;
    private String message;
}
