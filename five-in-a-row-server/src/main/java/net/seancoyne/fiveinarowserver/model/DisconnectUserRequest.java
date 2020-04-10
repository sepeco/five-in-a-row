package net.seancoyne.fiveinarowserver.model;

import lombok.Data;

@Data
public class DisconnectUserRequest {
    private String username;
    private Integer gameId;
}
