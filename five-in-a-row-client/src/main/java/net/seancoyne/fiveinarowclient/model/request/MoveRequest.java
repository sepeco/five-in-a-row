package net.seancoyne.fiveinarowclient.model.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MoveRequest {
    private Integer column;
    private Integer gameId;
    private String username;
}
