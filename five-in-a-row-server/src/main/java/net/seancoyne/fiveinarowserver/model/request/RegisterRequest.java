package net.seancoyne.fiveinarowserver.model.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private Integer gameId;
    private String userName;
    private String colour;
}
