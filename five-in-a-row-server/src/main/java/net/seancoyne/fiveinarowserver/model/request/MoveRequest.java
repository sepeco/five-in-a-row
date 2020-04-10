package net.seancoyne.fiveinarowserver.model.request;

import lombok.Data;

@Data
public class MoveRequest {
    private Integer column;
    private Integer gameId;
    private String username;
}
