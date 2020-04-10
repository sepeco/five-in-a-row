package net.seancoyne.fiveinarowserver.model;

import lombok.Data;

@Data
public class CreateGameRequest {
    private String username;
    private String selectedColour;
}
