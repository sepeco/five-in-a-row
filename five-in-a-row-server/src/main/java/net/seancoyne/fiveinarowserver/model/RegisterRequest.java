package net.seancoyne.fiveinarowserver.model;

import lombok.Data;

@Data
public class RegisterRequest {
    private Integer gameId;
    private String userName;
    private String colour;
}
