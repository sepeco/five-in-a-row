package net.seancoyne.fiveinarowserver.model.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateGameResponse {
    private ResponseState responseState;
    private Integer gameId;
    private String message;
}
