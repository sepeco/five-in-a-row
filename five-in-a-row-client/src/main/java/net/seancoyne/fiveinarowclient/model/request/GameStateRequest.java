package net.seancoyne.fiveinarowclient.model.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GameStateRequest {
    private String username;
    private Integer gameId;
}
