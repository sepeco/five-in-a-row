package net.seancoyne.fiveinarowserver.model.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DisconnectUserRequest {
    private String username;
    private Integer gameId;
}
