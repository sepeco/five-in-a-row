package net.seancoyne.fiveinarowclient.model.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RegisterRequest {
    private Integer gameId;
    private String userName;
    private String colour;
}
